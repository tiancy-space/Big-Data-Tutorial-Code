package chapter03

import java.util

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * @Description: 演示Spark Sql 中转化算子操作. .
 * @Author: tiancy
 * @Create: 2022/7/1
 */
object SparkSql02_Transform {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("spark sql action").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // TODO 回顾编写SQL中使用到的关键字: select from where group by having order by等
    /*
      map、filter、flatMap、mapPartitions、sample、 randomSplit、 limit、 distinct、dropDuplicates、describe
     */
    val operationCsvMap: Map[String, String] = Map("header" -> "true", "delimiter" -> ",", "inferSchema" -> "true")
    val employeeDF: DataFrame = spark.read.options(operationCsvMap) csv ("./02-Spark-Tutorial/data/employee_info_20220701.csv")
    employeeDF.show()
    // +-----------+-------------+---+------+------+
    //|employee_id|employee_name|age|gender|salary|
    //+-----------+-------------+---+------+------+
    //|       e001|          Bob| 28|  male|  8000|
    //|       e002|        Alice| 25|female|  6000|
    //|       e003|        David| 26|  male|  9000|
    //|       e004|        Kitty| 30|female| 11000|
    //|       e005|        Allen| 24|  male|  5500|
    //|       e006|          Ben| 28|  male|  8000|
    //|       e007|       George| 26|  male| 10000|
    //|       e008|          Bob| 28|  male| 20000|
    //|       e001|         Ashe| 18|female|  9000|
    //+-----------+-------------+---+------+------+

    // TODO 1、Spark slq select 操作 : 列的多种表示方法。使用""、$""、'、col()、ds("")
    employeeDF.select("employee_id", "employee_name", "age").show(10, false) // √
    employeeDF.select(s"employee_id", s"age", s"gender", s"salary").show(10, false) // √

    // 如果想使用下面的这种写法: select($"列名"),则需要使用隐士转换操作.
    import spark.implicits._
    employeeDF.select($"employee_id", $"employee_name", $"age", $"gender", $"salary").show()


    // 对某列数据进行修改并展示指定的列: 比如:age列 + 10 后的结果.
    employeeDF.selectExpr("employee_name as name", "age + 10 as newAge").show() // 展示两列.
    // 修改列名
    employeeDF.withColumnRenamed("employee_id", "id").show()

    //cast 类型转换
    employeeDF.selectExpr("cast(salary as Double)").show()

    // TODO 2、where=filter,注意过滤条件的写法,  == 以及单引号
    employeeDF.filter("employee_id == 'e001' ").show()

    // TODO 3、group by、having
    println("group by ")
    // 先指定某个字进行分组,按照分组条件聚合操作. 聚合操作写法: 是一个map [key:字段名称,value:聚合动作]
    employeeDF.groupBy("employee_id").agg("salary" -> "avg", "age" -> "max").show(100, false)
    // +-----------+-----------+--------+
    //|employee_id|avg(salary)|max(age)|
    //+-----------+-----------+--------+
    //|e002       |6000.0     |25      |
    //|e001       |8500.0     |28      |
    //|e004       |11000.0    |30      |
    //|e003       |9000.0     |26      |
    //|e005       |5500.0     |24      |
    //|e008       |20000.0    |28      |
    //|e006       |8000.0     |28      |
    //|e007       |10000.0    |26      |
    //+-----------+-----------+--------+

    // 根据性别分组,求相同性别的工资最大值,如果分组后还需要进行 having操作,则可以使用 where
    employeeDF.groupBy("gender").agg("salary" -> "max").show(20, false)
    //+------+-----------+
    //|gender|max(salary)|
    //+------+-----------+
    //|female|11000      |
    //|male  |20000      |
    //+------+-----------+

    // 分组聚合 + 修改列名 + having二次过滤操作.
    employeeDF.groupBy("gender").agg("salary" -> "max").withColumnRenamed("max(salary)", "maxSalary").where("maxSalary > 11000").show()
    //+------+---------+
    //|gender|maxSalary|
    //+------+---------+
    //|  male|    20000|


    // TODO 4、orderBy 相关,默认升序. 如果想使用降序,则可以在 orderBy(使用`-`)
    println("orderBy 的使用...")
    employeeDF.orderBy("age").show(20, false)
    employeeDF.orderBy(-$"age").show(20, false)
    // 也可以使用下面这种方式,直接调用 sort 方法,并通过 $""取到指定的列,并指定排序方式
    import spark.implicits._
    employeeDF.sort($"age".desc).show(20, false)

    // TODO 5、Join操作
    // 定义第一个数据集
    println("spark sql的join操作 ******************** ")

    val lst = List(
      StudentAge(1, "Alice", 18),
      StudentAge(2, "Andy", 19),
      StudentAge(3, "Bob", 17),
      StudentAge(4, "Justin", 21),
      StudentAge(5, "Cindy", 20)
    )
    val ds1 = spark.createDataset(lst)
    ds1.show()
    //+---+------+---+
    //|sno|  name|age|
    //+---+------+---+
    //|  1| Alice| 18|
    //|  2|  Andy| 19|
    //|  3|   Bob| 17|
    //|  4|Justin| 21|
    //|  5| Cindy| 20|
    //+---+------+---+

    // 定义第二个数据集
    val rdd = sc.makeRDD(
      List(
        StudentHeight("Alice", 160),
        StudentHeight("Andy", 159), StudentHeight("Bob", 170),
        StudentHeight("Cindy", 165), StudentHeight("Rose", 160)
      )
    )
    val ds2 = rdd.toDS
    ds2.show()
    //+-----+------+
    //|sname|height|
    //+-----+------+
    //|Alice|   160|
    //| Andy|   159|
    //|  Bob|   170|
    //|Cindy|   165|
    //| Rose|   160|
    //+-----+------+

    // 备注：不能使用双引号，而且这里是 ===
    ds1.join(ds2, $"name" === $"sname").show
    // 注意这种写法: 指定字段的名称,可以使用半个单引号.
    ds1.join(ds2, 'name === 'sname).show

    ds1.join(ds2, ds1("name") === ds2("sname")).show
    // 指定关联条件并指定join方式.
    println("指定关联条件并指定join方式 ========== ")
    ds1.join(ds2, ds1("name") === ds2("sname"), "inner").show
    //+---+-----+---+-----+------+
    //|sno| name|age|sname|height|
    //+---+-----+---+-----+------+
    //|  1|Alice| 18|Alice|   160|
    //|  2| Andy| 19| Andy|   159|
    //|  3|  Bob| 17|  Bob|   170|
    //|  5|Cindy| 20|Cindy|   165|
    //+---+-----+---+-----+------+


    // 多种连接方式
    ds1.join(ds2, $"name" === $"sname").show
    //|sno| name|age|sname|height|
    //+---+-----+---+-----+------+
    //|  1|Alice| 18|Alice|   160|
    //|  2| Andy| 19| Andy|   159|
    //|  3|  Bob| 17|  Bob|   170|
    //|  5|Cindy| 20|Cindy|   165|
    //+---+-----+---+-----+------+
    ds1.join(ds2, $"name" === $"sname", "inner").show
    //+---+-----+---+-----+------+
    //|sno| name|age|sname|height|
    //+---+-----+---+-----+------+
    //|  1|Alice| 18|Alice|   160|
    //|  2| Andy| 19| Andy|   159|
    //|  3|  Bob| 17|  Bob|   170|
    //|  5|Cindy| 20|Cindy|   165|
    //+---+-----+---+-----+------+

    ds1.join(ds2, $"name" === $"sname", "left").show
    //|sno|  name|age|sname|height|
    //+---+------+---+-----+------+
    //|  1| Alice| 18|Alice|   160|
    //|  2|  Andy| 19| Andy|   159|
    //|  3|   Bob| 17|  Bob|   170|
    //|  5| Cindy| 20|Cindy|   165|
    //|  4|Justin| 21| null|  null|
    //+---+------+---+-----+------+
    ds1.join(ds2, $"name" === $"sname", "left_outer").show
    //+---+------+---+-----+------+
    //|sno|  name|age|sname|height|
    //+---+------+---+-----+------+
    //|  1| Alice| 18|Alice|   160|
    //|  2|  Andy| 19| Andy|   159|
    //|  3|   Bob| 17|  Bob|   170|
    //|  5| Cindy| 20|Cindy|   165|
    //|  4|Justin| 21| null|  null|
    //+---+------+---+-----+------+
    ds1.join(ds2, $"name" === $"sname", "right").show
    //+----+-----+----+-----+------+
    //| sno| name| age|sname|height|
    //+----+-----+----+-----+------+
    //|   1|Alice|  18|Alice|   160|
    //|   2| Andy|  19| Andy|   159|
    //|   3|  Bob|  17|  Bob|   170|
    //|   5|Cindy|  20|Cindy|   165|
    //|null| null|null| Rose|   160|
    //+----+-----+----+-----+------+
    ds1.join(ds2, $"name" === $"sname", "right_outer").show
    //+----+-----+----+-----+------+
    //| sno| name| age|sname|height|
    //+----+-----+----+-----+------+
    //|   1|Alice|  18|Alice|   160|
    //|   2| Andy|  19| Andy|   159|
    //|   3|  Bob|  17|  Bob|   170|
    //|   5|Cindy|  20|Cindy|   165|
    //|null| null|null| Rose|   160|
    //+----+-----+----+-----+------+
    ds1.join(ds2, $"name" === $"sname", "outer").show
    //+----+------+----+-----+------+
    //| sno|  name| age|sname|height|
    //+----+------+----+-----+------+
    //|   1| Alice|  18|Alice|   160|
    //|   2|  Andy|  19| Andy|   159|
    //|   3|   Bob|  17|  Bob|   170|
    //|   5| Cindy|  20|Cindy|   165|
    //|   4|Justin|  21| null|  null|
    //|null|  null|null| Rose|   160|
    //+----+------+----+-----+------+
    ds1.join(ds2, $"name" === $"sname", "full").show
    //+----+------+----+-----+------+
    //| sno|  name| age|sname|height|
    //+----+------+----+-----+------+
    //|   1| Alice|  18|Alice|   160|
    //|   2|  Andy|  19| Andy|   159|
    //|   3|   Bob|  17|  Bob|   170|
    //|   5| Cindy|  20|Cindy|   165|
    //|   4|Justin|  21| null|  null|
    //|null|  null|null| Rose|   160|
    //+----+------+----+-----+------+

    ds1.join(ds2, $"name" === $"sname", "full_outer").show
    //+----+------+----+-----+------+
    //| sno|  name| age|sname|height|
    //+----+------+----+-----+------+
    //|   1| Alice|  18|Alice|   160|
    //|   2|  Andy|  19| Andy|   159|
    //|   3|   Bob|  17|  Bob|   170|
    //|   5| Cindy|  20|Cindy|   165|
    //|   4|Justin|  21| null|  null|
    //|null|  null|null| Rose|   160|
    //+----+------+----+-----+------+

    // TODO 6、unionAll、union 等价；unionAll过期方法，不建议使用
    println("union(并集) 、intersect(交集)、except(差集)")
    val ds3 = ds1.select("name")
    val ds4 = ds2.select("sname")
    // union 求并集，不去重
    ds3.union(ds4).show
    // 交集
    ds3.intersect(ds4).show(100, false)
    // 差集
    ds3.except(ds4).show(100, false)
    //+------+
    //|name  |
    //+------+
    //|Justin|
    //+------+


    // TODO 7、去重并统计
    val distinctCount: Long = employeeDF.distinct().count()
    println(distinctCount)

    // 按照指定的列值去重并返回结果集
    val distinctByCol: Dataset[Row] = employeeDF.dropDuplicates("employee_id")
    distinctByCol.show()
    //+-----------+-------------+---+------+------+
    //|employee_id|employee_name|age|gender|salary|
    //+-----------+-------------+---+------+------+
    //|       e001|          Bob| 28|  male|  8000|
    //|       e002|        Alice| 25|female|  6000|
    //|       e003|        David| 26|  male|  9000|
    //|       e004|        Kitty| 30|female| 11000|
    //|       e005|        Allen| 24|  male|  5500|
    //|       e006|          Ben| 28|  male|  8000|
    //|       e007|       George| 26|  male| 10000|
    //|       e008|          Bob| 28|  male| 20000|
    //+-----------+-------------+---+------+------+

    // TODO 8、空值处理: na.fill、na.drop
    println(math.sqrt(9.0)) // 3.0
    println(math.sqrt(-1.0)) // NaN
    // isNaN : 判断当前的返回结果是否为 NaN类型. not a number
    println(math.sqrt(-1.0).isNaN()) // true
    println(math.sqrt(9.0).isNaN()) // false

    val csvOpMap = Map("header" -> "true", "delimiter" -> ",", "inferSchema" -> "true")
    val addressInfoDF: DataFrame = spark.read.options(csvOpMap).csv("./02-Spark-Tutorial/data/address_info.csv")
    addressInfoDF.show(20, false)
    //+----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|city  |exact_address              |
    //+----------+-----------+------+---------------------------+
    //|a001      |顾村公园   |上海市|宝山区顾村镇               |
    //|a002      |钟楼       |西安市|碑林区东大街和南大街交汇处 |
    //|a003      |迪士尼乐园 |上海市|浦东新区川沙镇黄赵路 310 号|
    //|a004      |田子坊     |上海市|泰康路 210 弄              |
    //|a005      |长风公园   |上海市|普陀区大渡河路 189 号      |
    //|a006      |宽窄巷子   |成都  |null                       |
    //|null      |南楼古巷   |北京  |null                       |
    //|null      |null       |null  |null                       |
    //|a007      |null       |null  |null                       |
    //+----------+-----------+------+---------------------------+

    // 删除所有的列的空值和NaN, 只要有 null的行,都删除.
    println("删除所有的列的空值和NaN")
    addressInfoDF.na.drop().show()
    //+----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|  city|              exact_address|
    //+----------+-----------+------+---------------------------+
    //|      a001|   顾村公园|上海市|               宝山区顾村镇|
    //|      a002|       钟楼|西安市| 碑林区东大街和南大街交汇处|
    //|      a003| 迪士尼乐园|上海市|浦东新区川沙镇黄赵路 310 号|
    //|      a004|     田子坊|上海市|              泰康路 210 弄|
    //|      a005|   长风公园|上海市|      普陀区大渡河路 189 号|
    //+----------+-----------+------+---------------------------+
    // 删除某列的空值和NaN, 如果指定的当前列中存在 null值和 NaN, 则删除整行.
    println("删除某列的空值和NaN")
    addressInfoDF.na.drop(Array("address_id", "scenic_spot")).show(20, false)
    //删除某列的空值和NaN
    //+----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|city  |exact_address              |
    //+----------+-----------+------+---------------------------+
    //|a001      |顾村公园   |上海市|宝山区顾村镇               |
    //|a002      |钟楼       |西安市|碑林区东大街和南大街交汇处 |
    //|a003      |迪士尼乐园 |上海市|浦东新区川沙镇黄赵路 310 号|
    //|a004      |田子坊     |上海市|泰康路 210 弄              |
    //|a005      |长风公园   |上海市|普陀区大渡河路 189 号      |
    //|a006      |宽窄巷子   |成都  |null                       |
    //+----------+-----------+------+---------------------------+


    // 对 null的部分,使用指定的内容替换.
    addressInfoDF.na.fill("-").show(10, false)
    //+----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|city  |exact_address              |
    //+----------+-----------+------+---------------------------+
    //|a001      |顾村公园   |上海市|宝山区顾村镇               |
    //|a002      |钟楼       |西安市|碑林区东大街和南大街交汇处 |
    //|a003      |迪士尼乐园 |上海市|浦东新区川沙镇黄赵路 310 号|
    //|a004      |田子坊     |上海市|泰康路 210 弄              |
    //|a005      |长风公园   |上海市|普陀区大渡河路 189 号      |
    //|a006      |宽窄巷子   |成都  |-                          |
    //|-         |南楼古巷   |北京  |-                          |
    //|-         |-          |-     |-                          |
    //|a007      |-          |-     |-                          |
    //+----------+-----------+------+---------------------------+


    // 对指定的列,指定字符替换空值.
    addressInfoDF.na.fill("-", Array("address_id")).show(10, false)
    //+----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|city  |exact_address              |
    //+----------+-----------+------+---------------------------+
    //|a001      |顾村公园   |上海市|宝山区顾村镇               |
    //|a002      |钟楼       |西安市|碑林区东大街和南大街交汇处 |
    //|a003      |迪士尼乐园 |上海市|浦东新区川沙镇黄赵路 310 号|
    //|a004      |田子坊     |上海市|泰康路 210 弄              |
    //|a005      |长风公园   |上海市|普陀区大渡河路 189 号      |
    //|a006      |宽窄巷子   |成都  |null                       |
    //|-         |南楼古巷   |北京  |null                       |
    //|-         |null       |null  |null                       |
    //|a007      |null       |null  |null                       |
    //+----------+-----------+------+--------------------------


    // 对多个字段的null值和NaN进行替换
    addressInfoDF.na.fill(Map("address_id" -> "****", "scenic_spot" -> "----")).show(10, false)
    // +----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|city  |exact_address              |
    //+----------+-----------+------+---------------------------+
    //|a001      |顾村公园   |上海市|宝山区顾村镇               |
    //|a002      |钟楼       |西安市|碑林区东大街和南大街交汇处 |
    //|a003      |迪士尼乐园 |上海市|浦东新区川沙镇黄赵路 310 号|
    //|a004      |田子坊     |上海市|泰康路 210 弄              |
    //|a005      |长风公园   |上海市|普陀区大渡河路 189 号      |
    //|a006      |宽窄巷子   |成都  |null                       |
    //|****      |南楼古巷   |北京  |null                       |
    //|****      |----       |null  |null                       |
    //|a007      |----       |null  |null                       |
    //+----------+-----------+------+---------------------------+

    // 对指定的值进行替换.
    /*
      将“高度”和“重量”列中所有出现的 1.0 替换为 2.0。
        df.na.replace("height" :: "weight" :: Nil, Map(1.0 -> 2.0));
      在“firstname”和“lastname”列中将所有出现的“UNKNOWN”替换为“unnamed”
        df.na.replace("firstname" :: "lastname" :: Nil, Map("UNKNOWN" -> "unnamed"));
     */
    addressInfoDF.na.replace("address_id" :: "scenic_spot" :: Nil, Map("a001" -> "1")).show(10, false)
    //+----------+-----------+------+---------------------------+
    //|address_id|scenic_spot|city  |exact_address              |
    //+----------+-----------+------+---------------------------+
    //|1         |顾村公园   |上海市|宝山区顾村镇               |

    // 查询非空列或者空值列. isNull 、 isNotNull为内置函数.
    addressInfoDF.filter("address_id is not null").show(10, false)
    addressInfoDF.filter($"address_id".isNotNull).show(10, false)

    addressInfoDF.filter($"scenic_spot".isNull).show(10, false)
    addressInfoDF.filter("scenic_spot is null").show(10, false)


    sc.stop()
    spark.stop()
  }

  case class StudentAge(sno: Int, name: String, age: Int)

  case class StudentHeight(sname: String, height: Int)

}
