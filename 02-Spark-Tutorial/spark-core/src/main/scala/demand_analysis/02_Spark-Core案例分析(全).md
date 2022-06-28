# Spark-core算子综合练习
[TOC]
<div style="page-break-after: always; break-after: page;"></div>

## 1、统计出`每个省份每个广告被点击数量`排行的Top3

> 数据格式 : 时间戳，省份，城市，用户，广告      --- 中间字段使用空格分隔
>
> ```scala
> 1516609143867 6 7 64 16
> ```

### 1.1、 不使用`预聚合`功能

```scala
def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)
    val data: RDD[String] = sc.textFile("../Spark-core/input/agent.log")
    /*
        TODO 分析 : 按照省份作为key进行groupByKey()分组,再对组内的数据进行格式转化、打标记并根据广告分组
     */
    // 1.数据清洗,仅仅保留省份,广告
    val provAndAdvers: RDD[(String, String)] = data.map(
        line => {
            val datas: Array[String] = line.split(" ")
            val province: String = datas(1)
            val advertise: String = datas(datas.length - 1)
            (province, advertise)
        }
    ) //(6,16) (9,18) ...

    // 2.按照省份进行分组 (8,CompactBuffer(29, 27, 11, 18, 14, 25, 3, 7, 4, 5 .... )
    val groupByprov: RDD[(String, Iterable[String])] = provAndAdvers.groupByKey()
	
    groupByprov.mapValues(
        iter => {
            //将根据省份分组后的数据的values,也就是所有的广告 (29,27,11...) ==>((29,1),(27,1),(11,1) ...),再根据广告分组
            val wordList: Map[String, Iterable[(String, Int)]] = iter.map((_, 1)).groupBy(_._1)
            //获取到广告对应的点击数量 (29,20),() .....
            val wordCtn: Map[String, Int] = wordList.mapValues(_.size)
            //根据广告的点击数,进行降序排序,并取到前3
            wordCtn.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
        }
    ).collect().foreach(println)
}
```

```
//结果
(8,List((2,27), (20,23), (11,22)))
(4,List((12,25), (16,22), (2,22)))
(6,List((16,23), (24,21), (22,20)))
(0,List((2,29), (24,25), (26,24)))
(2,List((6,24), (21,23), (29,20)))
(7,List((16,26), (26,25), (1,23)))
(5,List((14,26), (12,21), (21,21)))
(9,List((1,31), (28,21), (0,20)))
(3,List((14,28), (28,27), (22,25)))
(1,List((3,25), (6,23), (5,22)))
```

### 1.2、 使用`预聚合`功能

```scala
def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)
    val data: RDD[String] = sc.textFile("../Spark-core/input/agent.log")
    /*
        TODO 分析 : 将省份 + 广告 作为分组条件,考虑到性能,尽可能使用预聚合功能,再分组统计
          1.数据清洗,设计清洗后的数据格式
          2.根据(省份,广告)作为聚合条件,使用reduceByKey统计广告点击次数
          3.调整数据格式为 ===> (省份,(广告,点击次数)),并根据省份分组
          4.分组后,根据广告的点击量,在分组中进行排序,最后取前三
     */
    // 1.数据清洗
    val ETLData: RDD[((String, String), Int)] = data.map(
        line => {
            val datas: Array[String] = line.split(" ")
            val province: String = datas(1)
            val advertise: String = datas(datas.length - 1)
            ((province, advertise), 1)
        }
    ) // ((省份,广告),1)
    // 2.可以直接使用reduceBykey进行统计
    val provinceAndAdverties: RDD[((String, String), Int)] = ETLData.reduceByKey(
        (t1, t2) => {
            t1 + t2
        }
    ) //((1,25),15)
    // 3.调整 ((省份,广告),数量) 的数据格式,并按照省份分组
    val groupByProv: RDD[(String, Iterable[(String, Int)])] = provinceAndAdverties.map {
        case ((prv, ad), sum) => {
            (prv, (ad, sum))
        }
    }.groupByKey() // (4,CompactBuffer((12,25), (25,11), ....)
    // 4.给每个省份中的广告排序,排序规则:根据每个广告的点击量降序,并取前三
    val result: RDD[(String, List[(String, Int)])] = groupByProv.mapValues(
        iter => {
            //这个排序是单点排序
            iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
        }
    )
    result.collect().foreach(println)
}
```

```
//结果
(4,List((12,25), (2,22), (16,22)))
(8,List((2,27), (20,23), (11,22)))
(6,List((16,23), (24,21), (22,20)))
(0,List((2,29), (24,25), (26,24)))
(2,List((6,24), (21,23), (29,20)))
(7,List((16,26), (26,25), (1,23)))
(5,List((14,26), (21,21), (12,21)))
(9,List((1,31), (28,21), (0,20)))
(3,List((14,28), (28,27), (22,25)))
(1,List((3,25), (6,23), (5,22)))
```

### 1.3、排序时,考虑分布式

**<strong style="color:#ff0000;">错判写法</strong>** 

```scala
def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO 统计出每一个省份每个广告被点击数量排行的Top3
    // 1. 读取原始数据
    val fileDatas = sc.textFile("../Spark-core/input/agent.log")

    // 2. 过滤数据 (word)
    val prvToAdCount = fileDatas.map(
        data => {
            val datas = data.split(" ")
            ((datas(1), datas(4)), 1)
        }
    ).reduceByKey(_ + _)

    val groupRDD: RDD[(String, Iterable[(String, Int)])] = prvToAdCount.map {
        case ((prv, ad), sum) => {
            (prv, (ad, sum))
        }
    }.groupByKey()
	//分布式排序,排好了,再通过map的方式取top3
    val provCount: RDD[(String, Iterable[(String, Int)])] = groupRDD.sortBy(_._2)
    val result: RDD[(String, List[(String, Int)])] = provCount.mapValues(
        iter => iter.toList.take(3)
    )
    result.collect().foreach(println)
    sc.stop()
}
```

```
结果
(5,List((10,15), (11,15), (15,15)))
(4,List((12,25), (25,11), (27,13)))
(2,List((2,19), (15,17), (3,14)))
(1,List((25,15), (16,12), (2,20)))
(7,List((27,21), (24,15), (12,14)))
(3,List((4,12), (3,18), (8,17)))
(0,List((7,5), (18,15), (3,16)))
(6,List((7,17), (16,23), (5,19)))
(8,List((8,18), (13,19), (16,10)))
(9,List((8,18), (15,15), (24,11)))
```

**<strong style="color:#ff0000;">解决思路</strong>** 

```scala
def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO 统计出每一个省份每个广告被点击数量排行的Top3
    // 1. 读取原始数据
    val fileDatas = sc.textFile("../Spark-core/input/agent.log")

    // 2. 过滤数据 (word)
    val prvToAdCount = fileDatas.map(
        data => {
            val datas = data.split(" ")
            ((datas(1), datas(4)), 1)
        }
    ).reduceByKey(_ + _)

    val groupRDD: RDD[(String, Iterable[(String, Int)])] = prvToAdCount.map {
        case ((prv, ad), sum) => {
            (prv, (ad, sum))
        }
    }.groupByKey()

    /*
      TODO 下面这种排序方式,会产生问题,因为 _._2 获取到的是一个迭代器,并没有排里面的数据.
      	解决思路 : 可以先通过分区内进行排序,取前三条数据,再通过分区间排序,最终取到每个省份的前三
        这种感觉很像是归并的思想. 也可以回想以下Hadoop中的MR的shuffle前后对数据的处理
        因此,最好的解决思路是直接使用 aggregateByKey
   */
    //val provCount: RDD[(String, Iterable[(String, Int)])] = groupRDD.sortBy(_._2)

    sc.stop()

}
```

<div style="page-break-after: always; break-after: page;"></div>

## 2、统计商品的热门品类Top10

在实际的工作中如何使用这些API实现具体的需求: 这些需求是电商网站的真实需求，所以在实现功能前，咱们必须先将数据准备好。

### **数据准备**以及数据说明

> **每行数据的基本说明**
>
> ![image-20220624095924357](https://tiancy-images.oss-cn-beijing.aliyuncs.com/img/202206240959488.png) 
>
> **详细字段说明**
>
> | 编号 | 字段名称           | 字段类型 | 字段含义                   |
> | ---- | ------------------ | -------- | -------------------------- |
> | 1    | date               | String   | 用户点击行为的日期         |
> | 2    | user_id            | Long     | 用户的ID                   |
> | 3    | session_id         | String   | Session的ID                |
> | 4    | page_id            | Long     | 某个页面的ID               |
> | 5    | action_time        | String   | 动作的时间点               |
> | 6    | search_keyword     | String   | 用户搜索的关键词           |
> | 7    | click_category_id  | Long     | 某一个商品品类的ID         |
> | 8    | click_product_id   | Long     | 某一个商品的ID             |
> | 9    | order_category_ids | String   | 一次订单中所有品类的ID集合 |
> | 10   | order_product_ids  | String   | 一次订单中所有商品的ID集合 |
> | 11   | pay_category_ids   | String   | 一次支付中所有品类的ID集合 |
> | 12   | pay_product_ids    | String   | 一次支付中所有商品的ID集合 |
> | 13   | city_id            | Long     | 城市 id                    |

```sql
-- 部分数据展示
点击时间(1)、用户id(2)、sessionID(3)、页面ID(4)、动作时间(5)、搜索关键字(6)、某商品品类ID(7)、商品ID(8)、一次订单中所有品类ID集合(9)、一次订单所有商品ID集合(10)、一次订单中所有品类ID支付集合(11)、一次订单中所有商品ID支付集合(12)、城市ID(13)
-- 搜索
2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_37_2019-07-17 00:00:02_手机_-1_-1_null_null_null_null_3
-- 点击行为
2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_48_2019-07-17 00:00:10_null_16_98_null_null_null_null_19
-- 下单行为
2019-07-17_39_e17469bf-0aa1-4658-9f76-309859dcd641_47_2019-07-17 00:02:59_null_-1_-1_15,9,3_30_null_null_21
-- 付款行为
2019-07-17_39_e17469bf-0aa1-4658-9f76-309859dcd641_4_2019-07-17 00:02:56_null_-1_-1_null_null_15,1,16_52,77_6
```

> **上面的数据图是从数据文件中截取的一部分内容，表示为电商网站的用户行为数据，主要包含用户的4种行为：<strong style="color:#ff0000;">搜索，点击，下单，支付</strong> 。数据规则如下：**
>
> - 每一行数据表示用户的一次行为，这个行为只能是4种行为的一种
>
> - 如果搜索关键字为null,表示数据不是搜索数据
>
> - 如果点击的品类ID和产品ID为-1，表示数据不是点击数据
>
> - 针对于下单行为，一次可以下单多个商品，所以品类ID和产品ID可以是多个，id之间采用逗号分隔，如果本次不是下单行为，则数据采用null表示
>
> - 支付行为和下单行为类似



### 需求描述

> **统计出每个品类ID的点击数、下单数、支付数.并按照 点击数、下单数、支付数这个顺序,进行降序排序. 结果取前10条**



### 代码实现

[Spark01_Req_HotCategoryTop10_1.scala - Spark01_Req_HotCategoryTop10_4.scala](https://github.com/tiancy-space/Big-Data-Tutorial-Code/tree/master/02-Spark-Tutorial/spark-core/src/main/scala/demand_analysis)

<div style="page-break-after: always; break-after: page;"></div>

## 3、Top10热门品类中每个品类的Top10活跃Session统计

### 需求描述

**在需求一的基础上，增加Top10品类中用户session的<strong style="color:#ff0000;">点击</strong> 统计** 

> 需要在需求二的基础上,进行后续开发. 并且需要注意 <strong style="color:#ff0000;">**点击**</strong>行为.  



## 4、页面单跳转换率统计

### 1）页面单跳转化率

计算页面单跳转化率，什么是页面单跳转换率，比如一个用户在一次 Session 过程中访问的页面路径 3,5,7,9,10,21，那么页面 3 跳到页面 5 叫一次单跳，7-9 也叫一次单跳，那么单跳转化率就是要统计页面点击的概率。

比如：计算 3-5 的单跳转化率，先获取符合条件的 Session 对于页面 3 的访问次数（PV）为 A，然后获取符合条件的 Session 中访问了页面 3 又紧接着访问了页面 5 的次数为 B，那么 B/A 就是 3-5 的页面单跳转化率。

![img](https://tiancy-images.oss-cn-beijing.aliyuncs.com/img/202206262330124.jpg) 

### 2）统计页面单跳转化率意义

产品经理和运营总监，可以根据这个指标，去尝试分析，整个网站，产品，各个页面的表现怎么样，是不是需要去优化产品的布局；吸引用户最终可以进入最后的支付页面。

数据分析师，可以此数据做更深一步的计算和分析。

企业管理层，可以看到整个公司的网站，各个页面的之间的跳转的表现如何，可以适当调整公司的经营战略或策略。



[上述需求的代码可以直接在代码仓库中查看 ](https://github.com/tiancy-space/Big-Data-Tutorial-Code/tree/master/02-Spark-Tutorial) 

02-Spark-Tutorial/src/scala/demand_analysis



## 5、给定一个学生成绩文件,对下面的问题进行分析

```txt
12 张三 25 男 chinese 50
12 张三 25 男 math 60
12 张三 25 男 english 70
12 李四 20 男 chinese 50
12 李四 20 男 math 50
12 李四 20 男 english 50
12 王芳 19 女 chinese 70
12 王芳 19 女 math 70
12 王芳 19 女 english 70
13 张大三 25 男 chinese 60
13 张大三 25 男 math 60
13 张大三 25 男 english 70
13 李大四 20 男 chinese 50
13 李大四 20 男 math 60
13 李大四 20 男 english 50
13 王小芳 19 女 chinese 70
13 王小芳 19 女 math 80
13 王小芳 19 女 english 70
```

```sql
-- 1.参加考试
一共有多少人参加考试？
一共有多少个小于20岁的人参加考试？
一共有多少个等于20岁的人参加考试？
一共有多少个大于20岁的人参加考试？
一共有多个男生参加考试？
一共有多少个女生参加考试？
12班有多少人参加考试？
13班有多少人参加考试？

-- 2.平均成绩
语文科目的平均成绩是多少？
数学科目的平均成绩是多少？
英语科目的平均成绩是多少？
单个人平均成绩是多少？
12班平均成绩是多少？

-- 3.总成绩
12班男生平均总成绩是多少？
12班女生平均总成绩是多少？
同理求13班相关成绩

-- 4.最高、最低分
全校语文成绩最高分是多少？
12班语文成绩最低分是多少？
13班数学最高成绩是多少？

-- 5.总和统计
总成绩大于150分的12班的女生有几个？
总成绩大于150分，且数学大于等于70，且年龄大于等于19岁的学生的平均成绩是多少？
```



## 6、tweets 数据分析

**数据**

```json
{“id”:“572692378957430785”,“user”:“Srkian_nishu “,“text”:”@always_nidhi @YouTube no i dnt understand bt i loved the music nd their dance awesome all the song of this mve is rocking”,“place”:“Orissa”,“country”:“India”}
```

**数据格式**

| id      | 用户id   |
| ------- | -------- |
| user    | 用户姓名 |
| text    | 内容     |
| place   | 位置     |
| country | 国家     |

**问题**

```sql
1）统计tweets 总条数；
2）统计每个用户的tweets 条数；
3）统计tweets中被@的分别是那些人；
4）统计出被@次数最多的10个人；
```