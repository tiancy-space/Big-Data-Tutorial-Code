package chapter06_sparksql常用函数

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description: 参考源码Window.scala、WindowSpec.scala（主要）.工作中常用的还是直接写sql的方式使用开窗函数.
 * @Author: tiancy
 * @Create: 2022/7/8
 */

object SparkSql02_开窗函数 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("windows function")
      .master("local[*]")
      .getOrCreate()

    val cookCsvDF: DataFrame = spark.read.format("csv").option("header", true).option("inferSchema", true).load("./02-Spark-Tutorial/spark-sql/src/main/resources/cook.csv")
    cookCsvDF.show(20, false)
    //+--------+-----------+---+
    //|cookieid|create_time|pv |
    //+--------+-----------+---+
    //|cookie1 |2022-04-12 |7  |
    //|cookie1 |2022-04-12 |7  |
    //|cookie1 |2022-04-12 |7  |
    //|cookie1 |2022-04-12 |10 |
    //|cookie1 |2022-04-11 |5  |
    //|cookie2 |2022-04-15 |4  |
    //|cookie3 |2022-04-16 |4  |
    //|cookie4 |2022-04-13 |3  |
    //|cookie1 |2022-04-14 |2  |
    //|cookie2 |2022-04-10 |1  |
    //+--------+-----------+---+


    // TODO 1、先看一下开窗函数的源码
    import org.apache.spark.sql.expressions.Window // 用于在 DataFrame 中定义窗口的实用程序函数。
    import org.apache.spark.sql.expressions.WindowSpec // 主要用的还是这个,Window这个类中掉用的具体方法,都是WindowSpec下面的.

    /*
        TODO 回顾什么是开创函数,开窗函数主要解决了哪些问题呢.
          可以把“窗口”（windows）这个理解一个集合，一个窗口就是一个集合，在统计分析中有需要不同的「窗口]
          比如:统计相同部门的员工基本信息以及工资的排名. ==> 相同部门的员工被分到同一个组,每个组都是一个窗口.在窗口内进行工资排名操作.
          `窗口函数`会按当前所在的分组进行聚合计算，并将`聚合计算结果`追加在当前行对应的`新列` ，它表达的是当前行与这所在分组的关系
          而普通的分组聚合操作,不能取到分组列以及聚合列之外的无关字段.
     */
    cookCsvDF.createOrReplaceTempView("cook_info")
    // TODO 2、先看一段 使用开窗函数的sql.求每个用户、每天的页面点击量(pv)的增长趋势
    spark.sql(
      """
        |select
        | cookieid,create_time,pv,
        | sum(pv) over(partition by cookieid order by create_time asc ) upClickCtn
        |from cook_info
        |""".stripMargin).show(20, false)
    //|cookieid|create_time|pv |upClickCtn|
    //+--------+-----------+---+----------+
    //|cookie1 |2022-04-11 |5  |5         |
    //|cookie1 |2022-04-12 |7  |36        |
    //|cookie1 |2022-04-12 |7  |36        |
    //|cookie1 |2022-04-12 |7  |36        |
    //|cookie1 |2022-04-12 |10 |36        |
    //|cookie1 |2022-04-14 |2  |38        |
    //|cookie2 |2022-04-10 |1  |1         |
    //|cookie2 |2022-04-15 |4  |5         |
    //|cookie3 |2022-04-16 |4  |4         |
    //|cookie4 |2022-04-13 |3  |3         |
    //+--------+-----------+---+----------+

    /*
        TODO 3、使用 spark sql 提供的 DSL语法表达上述 2的需求写法.
          如果想使用spark sql中的窗口函数,就要先声明一个 窗口函数对象. 在用到 df.select()中.并指定窗口函数前的动作. eg: 声明一个窗口函数,按照cookid分组,按照时间排序,统计 pv的和.
          note1 : 在使用spark sql时,需要手动导入几个包: import org.apache.spark.sql.functions._ 、隐士转换 提供类型转化操作.
          使用步骤: 1、先定义一个窗口函数,根据哪个字段分区、根据哪个字段进行排序.返回一个窗口函数对象. 2、通过 df.select(查询字段...,窗口前的动作.over(窗口函数对象).alias(别名))
     */
    import spark.implicits._ // 主要进行类型转化. 这里不写,下面的 select($"字段") 用不了.
    import org.apache.spark.sql.functions._ // 主要用于导入 spark sql模块中定义的一些函数,这里不写,下面的 sum()等函数用不了.
    val windows1: WindowSpec = Window.partitionBy("cookieid").orderBy("create_time")
    val cookieIdSumWindow: DataFrame = cookCsvDF.select($"cookieid", $"pv", $"create_time", sum("pv").over(windows1).alias("pv1"))
    cookieIdSumWindow.show(10, false)
    //+--------+---+-----------+---+
    //|cookieid|pv |create_time|pv1|
    //+--------+---+-----------+---+
    //|cookie1 |5  |2022-04-11 |5  |
    //|cookie1 |7  |2022-04-12 |36 |
    //|cookie1 |7  |2022-04-12 |36 |
    //|cookie1 |7  |2022-04-12 |36 |
    //|cookie1 |10 |2022-04-12 |36 |
    //|cookie1 |2  |2022-04-14 |38 |
    //|cookie2 |1  |2022-04-10 |1  |
    //|cookie2 |4  |2022-04-15 |5  |
    //|cookie3 |4  |2022-04-16 |4  |
    //|cookie4 |3  |2022-04-13 |3  |
    //+--------+---+-----------+---+

    /*
        TODO 4、相同cookieid下的pv排名.  1,2,2,3,4这种排名方式
     */
    // 如果想指定当前排序降序,可以在 orderBy(负号 $"字段名称")指定.
    val windows2: WindowSpec = Window.partitionBy("cookieid").orderBy(-$"pv")
    cookCsvDF.select($"cookieid", $"create_time", $"pv", dense_rank().over(windows2).alias("dr")).show(10, false)
    //+--------+-----------+---+---+
    //|cookieid|create_time|pv |dr |
    //+--------+-----------+---+---+
    //|cookie1 |2022-04-12 |10 |1  |
    //|cookie1 |2022-04-12 |7  |2  |
    //|cookie1 |2022-04-12 |7  |2  |
    //|cookie1 |2022-04-12 |7  |2  |
    //|cookie1 |2022-04-11 |5  |3  |
    //|cookie1 |2022-04-14 |2  |4  |
    //|cookie2 |2022-04-15 |4  |1  |
    //|cookie2 |2022-04-10 |1  |2  |
    //|cookie3 |2022-04-16 |4  |1  |
    //|cookie4 |2022-04-13 |3  |1  |
    //+--------+-----------+---+---+

    /*
        TODO 5、演示 开窗函数中的移动窗口问题: 形成的窗口,要声明 移动范围设定(rows、range)和移动方向(precending、following)设定.
         其中移动窗口又包含:
         - 滑动窗口(当前行和前后[n]行形成一个封闭的窗口)
         - 扩展窗口(当前行到向上无边界 or 当前行向下无边界) 这种.
         eg: 演示 根据cookieid分组,统计当前行向上无边界的pv聚合值.
     */
    val windows3: WindowSpec = Window
      .partitionBy("cookieid")
      .rowsBetween(Window.unboundedPreceding, Window.currentRow) // 根据`cookieid`分组,求当前行向上无边界的pv求和操作. 当前行向上无边界.
    cookCsvDF.select($"cookieid", $"create_time", $"pv", sum("pv").over(windows3).alias("pvUpCtn")).show(10, false)
    //+--------+-----------+---+-------+
    //|cookieid|create_time|pv |pvUpCtn|
    //+--------+-----------+---+-------+
    //|cookie1 |2022-04-12 |7  |7      |
    //|cookie1 |2022-04-12 |7  |14     |
    //|cookie1 |2022-04-12 |7  |21     |
    //|cookie1 |2022-04-12 |10 |31     |
    //|cookie1 |2022-04-11 |5  |36     |
    //|cookie1 |2022-04-14 |2  |38     |
    //|cookie2 |2022-04-15 |4  |4      |
    //|cookie2 |2022-04-10 |1  |5      |
    //|cookie3 |2022-04-16 |4  |4      |
    //|cookie4 |2022-04-13 |3  |3      |
    //+--------+-----------+---+-------+

    println("******" * 5)
    // rowsBetween(数字) : 其中0代表当前行, -1 代表当前行的前一行,5代表当前行的后5行. 这里求的是当前行 + 当前行的下一行的和. 并且使用`withColumn`的方式原有的列 + 添加新列.
    val windows4: WindowSpec = Window.partitionBy("cookieid").orderBy("create_time").rowsBetween(0, 1)
    cookCsvDF.withColumn("currAndNextRowSum", sum("pv").over(windows4)).show(10, false)
    //+--------+-----------+---+-----------------+
    //|cookieid|create_time|pv |currAndNextRowSum|
    //+--------+-----------+---+-----------------+
    //|cookie1 |2022-04-11 |5  |12               |
    //|cookie1 |2022-04-12 |7  |14               |
    //|cookie1 |2022-04-12 |7  |14               |
    //|cookie1 |2022-04-12 |7  |17               |
    //|cookie1 |2022-04-12 |10 |12               |
    //|cookie1 |2022-04-14 |2  |2                |
    //|cookie2 |2022-04-10 |1  |5                |
    //|cookie2 |2022-04-15 |4  |4                |
    //|cookie3 |2022-04-16 |4  |4                |
    //|cookie4 |2022-04-13 |3  |3                |
    //+--------+-----------+---+-----------------+

    /*
      TODO 6、使用其他的窗口函数写法以及思路:
        都是需要先指定 字段进行分组 + 字段排序. ==> 返回一个 Window对象. 还可以指定窗口范围: `rowsBetween`、有无边界: `unboundedPreceding`、`unboundedFollowing`以及当前行.
        窗口函数定义结束后: 可以通过 df.select或者 `withColumn`的方式,再声明操作: sum() | 排名相关: rank()、dinse_rank()、row_number() | lag()、lead() | first_value、last_value等函数.

     */
    println("======" * 4)
    // 演示: 新增一列,字段的值为当前行的上一行pv数据.
    val windows5: WindowSpec = Window.partitionBy("cookieid").orderBy("create_time")
    cookCsvDF.withColumn("lastRow", lead("pv", 1, 0).over(windows5)).show(10, false)
    //========================
    //+--------+-----------+---+-------+
    //|cookieid|create_time|pv |lastRow|
    //+--------+-----------+---+-------+
    //|cookie1 |2022-04-11 |5  |7      |
    //|cookie1 |2022-04-12 |7  |7      |
    //|cookie1 |2022-04-12 |7  |7      |
    //|cookie1 |2022-04-12 |7  |10     |
    //|cookie1 |2022-04-12 |10 |2      |
    //|cookie1 |2022-04-14 |2  |0      |
    //|cookie2 |2022-04-10 |1  |4      |
    //|cookie2 |2022-04-15 |4  |0      |
    //|cookie3 |2022-04-16 |4  |0      |
    //|cookie4 |2022-04-13 |3  |0      |
    //+--------+-----------+---+-------+

    /*
        TODO 7、使用这种代码方式表现开窗操作的写法,仅仅是对当前数据进行简单的处理操作,如果需要进行复杂的操作,还是注册成临时表,直接写`sql`方便.
     */

    spark.stop()
  }
}
