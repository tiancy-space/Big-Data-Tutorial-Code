package chapter07_sparksql优化实战

import chapter03.SparkSql02_Transform.{StudentAge, StudentHeight}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
 * @Description: spark sql 基本优化: 1、编码优化 2、参数优化(也就是提交时的参数)
 * @Author: tiancy
 * @Create: 2022/7/11
 */
object SparkSql01_编码的优化 {
  /*
    TODO Spark Sql 的基础优化,主要从如下两个点来进行:
      也可以去官网看看,性能调整部分的说明: https://spark.apache.org/docs/3.3.0/sql-performance-tuning.html
      1、编码优化部分
        - 避免 `select * `,而是指定`select 具体字段`.
        - 查询时,合理利用分区,减少数据量.
        - 分而治之,如果待处理的数据量非常大,比如处理一年的,可以将数据进行切分,先处理一个月的.
        - 使用广播,让Spark SQL 选择 Broadcast Join . 两种声明方式: 一种是符合条件进行广播,不是强制的[表大小所占的空间需要满足条件触发]. 另一种是强制广播.
          在声明 SparkSession对象时,直接指定配置项.[被动广播]
          利用API进行强制广播,就是使用DataSet处理数据过程中,使用API进行强制广播. table1.join(table2.hint("broadcast"),关联键,关联方式)
          Spark SQL中写SQL文,通过指定的关键字,也可以实现同样的操作. select /*broadcast(强制广播的表)*/ 表1的查询字段,表2的查询字段 from 表1 join 表2 on 关联条件.
        - 针对复杂的设计逻辑,缓存复用.
   */
  // TODO 编码优化部分: 1、select 具体字段 2、使用分区过滤数据 3、处理大数据量场景下,可以先进行局部聚合,再总体聚合.[每个月聚合 + 月月聚合.] 4、广播表[自动广播或者强制广播]. 5、缓存常用的表或者 DF.


  def main(args: Array[String]): Unit = {
    // 广播操作,自动广播选项,默认表大小在 10M以内自动广播
    val sparkConf: SparkConf = new SparkConf()
      .setAppName("spark sql 优化")
      .setMaster("local[*]")
    val spark: SparkSession = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()
    val sc: SparkContext = spark.sparkContext

    //spark.conf.set("spark.sql.autoBroadcastJoinThreshold", -1) // 将自动广播功能关闭.
    spark.conf.set("spark.sql.autoBroadcastJoinThreshold", 1073741824) // 开启自动广播功能: 默认广播大小为 10M ,是以字节为单位的. 可以调大一些. 512M 或者 1G.这里写了1G.


    // TODO 2、使用API进行强制广播:不管是否满足,只要使用语法标记,就会将被标记的表进行广播. 表关联走的是`Broadcast joins`
    /*定义第一个数据集 */
    println("spark sql的join操作 ******************** ")
    import spark.implicits._
    import org.apache.spark.sql._
    val lst = List(
      StudentAge(1, "Alice", 18),
      StudentAge(2, "Andy", 19),
      StudentAge(3, "Bob", 17),
      StudentAge(4, "Justin", 21),
      StudentAge(5, "Cindy", 20)
    )
    val ds1: Dataset[StudentAge] = spark.createDataset(lst)

    ds1.createOrReplaceTempView("student_info")
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
    val rdd: RDD[StudentHeight] = sc.makeRDD(
      List(
        StudentHeight("Alice", 160),
        StudentHeight("Andy", 159),
        StudentHeight("Bob", 170),
        StudentHeight("Cindy", 165),
        StudentHeight("Rose", 160)
      )
    )
    val ds2: Dataset[StudentHeight] = rdd.toDS
    ds2.createOrReplaceTempView("student_height")
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
    /*
      TODO 3、利用API进行强制广播的两种写法: 一种是SQL文指指定. 另一种使用 DSL语法实现.
        SQL语法格式是固定的: 正常的多表关联,并指定关联条件.  select  /*broadcast(ss)*/  ==> 强制广播另一张表: ss
     */
    val broadCastJoinDF: DataFrame = spark.sql(
      """
        |select /*broadcast(ss)*/ sno,name,age,height
        |from student_info s
        |join student_height ss on s.name = ss.sname
        |""".stripMargin)

    broadCastJoinDF.explain()
    // +- BroadcastHashJoin [name#4], [sname#26], Inner, BuildLeft, false

    broadCastJoinDF.show(10, false)
    //|sno|name |age|height|
    //+---+-----+---+------+
    //|1  |Alice|18 |160   |
    //|2  |Andy |19 |159   |
    //|3  |Bob  |17 |170   |
    //|5  |Cindy|20 |165   |
    //+---+-----+---+------+

    /*
      TODO 通过 DSL语法关联两个表.语法指定 Join Hints. Hints的具体说明,可以看官网描述: https://spark.apache.org/docs/3.3.0/sql-ref-syntax-qry-select-hints.html
        join hints的描述部分: https://spark.apache.org/docs/3.3.0/sql-ref-syntax-qry-select-hints.html#join-hints
     */
    println("======" * 6)
    ds1.join(ds2.hint("broadcast"), ds1("name") === ds2("sname"), "inner").show(10, false)
    ds1.join(ds2.hint("broadcast"), ds1("name") === ds2("sname"), "inner").explain()
    ds1.join(ds2.hint("broadcast"), $"name" === $"sname", "inner").show(10, false)
    /*
      TODO sql 中使用 `using`来简化两个表中关联字段名称相同的join写法.
        eg: 两张表,关联字段都是 id. 正常sql写法: select a.*,b.* from a join b on a.id = b.id . 简化为: select a.* ,b.* from a join b using(id)
     */
    // ds1.join(ds2.hint("broadcast"), Seq("name", "sname"), "inner").show(10, false)

    // TODO 使用算子 broadcast() 方法强制广播某张表.
    import org.apache.spark.sql.functions.broadcast
    ds1.join(broadcast(ds2), $"name" === $"sname", "inner").show(10, false)
    //+---+-----+---+-----+------+
    //|sno|name |age|sname|height|
    //+---+-----+---+-----+------+
    //|1  |Alice|18 |Alice|160   |
    //|2  |Andy |19 |Andy |159   |
    //|3  |Bob  |17 |Bob  |170   |
    //|5  |Cindy|20 |Cindy|165   |
    //+---+-----+---+-----+------+

    // TODO 将常用的或者重复使用的临时表或者 DF 进行缓存操作. 这个操作应该在前面定义,这里为了演示,就写在这里了.
    spark.sql(
      """
        | -- 将查询表的结果进行缓存操作,并将查询结果作为一个临时视图.给一个视图名称.
        |CACHE TABLE student OPTIONS ('storageLevel' 'DISK_ONLY') SELECT * FROM student_info;
        |""".stripMargin)
    spark.sql(
      """
        |select * from student
        |""".stripMargin).show(10,false)

    // 也可以直接将 DS 或者 DF使用 cache()进行缓存.
    val ds1Cache: ds1.type = ds1.cache()



    sc.stop()
    spark.stop()
  }

  case class StudentAge(sno: Int, name: String, age: Int)

  case class StudentHeight(sname: String, height: Int)

}
