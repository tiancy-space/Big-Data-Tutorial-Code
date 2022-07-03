package chapter02

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object CreateDF extends App {
  private val spark: SparkSession = SparkSession.builder().master("local[1]").getOrCreate()
  private val sc: SparkContext = spark.sparkContext
  // DF DS
  // range => DataSet
  //
  val numDS = spark.range(5, 100, 5)

  // orderBy desc show
  //    numDS.orderBy(desc("id")).show(10,false)

  //显示Schema信息
  numDS.printSchema()

  // 统计信息
  numDS.describe().show(100, false)

  // 使用RDD执行操作
  numDS.rdd.map(_.toInt).stats()

  // ✊分区数
  println(numDS.rdd.getNumPartitions)


  // 集合生成DataSet

  import spark.implicits._

  case class Person(name: String, age: Int, height: Int)

  val seq1 = Seq(Person("zs", 12, 160), Person("ls", 22, 170), Person("ww", 33, 180))
  val ds1 = spark.createDataset(seq1)
  ds1.printSchema()
  ds1.show(10, false)

  // 由集合-》 Dataframe
  val List1 = List(("zs", 12, 160), ("ls", 22, 170), ("ww", 33, 180))
  val df1 = spark.createDataFrame(List1)
    .withColumnRenamed("_1", "name")
    .withColumnRenamed("_2", "age")
    .withColumnRenamed("_3", "height")
  df1.printSchema()

  df1.orderBy("age").show(10, false)

  // 直接修改整个DF的列名
  val df2 = spark.createDataFrame(List1)
    .toDF("name", "age", "height")

  df2.show(10, false)

  println("============================")


  // RDD -> DataFrame
  // DataFrame = RDD[Row] +Schema. 第一种创建方式: 定义 Row和Schema,在使用 spark.createDataFrame(rdd,schema)
  val arr = Array(("zs", 12, 160), ("ls", 22, 170), ("ww", 33, 180))
  // ("zs",12,160) => row
  // 构造了一个 RDD[Row]
  val rdd1 = sc.makeRDD(arr).map(f => Row(f._1, f._2, f._3))
  val schema = StructType(Seq(
    StructField("name", StringType),
    StructField("age", IntegerType),
    StructField("height", IntegerType)
  ))
  val rddToDF = spark.createDataFrame(rdd1, schema)
  rddToDF.show(10, false)

  // 反射推断
  val rdd2: RDD[Person] = sc.makeRDD(arr).map(f => Person(f._1, f._2, f._3))
  val df3 = rdd2.toDF()
  val ds3 = rdd2.toDS()
  println("===================")
  ds3.orderBy("name").show(10, false)
  df3.orderBy("name").show(10, false)


  // RDD -> DataSet
  // DataSet = RDD[case class]
  spark.createDataset(rdd2)


  //从文件中创建DataFrame （csv文件）
  println("===============================")
  println("===============================")


  val df4 = spark.read.option("header", "true").csv("./02-Spark-Tutorial/data/people.csv")
  df4.printSchema()
  df4.show(10, false)

  // 开启自动推断
  val map = Map(("header" -> "true"), ("delimiter" -> ";"), "inferSchema" -> "true")

  val df5 = spark.read.options(map).csv("./02-Spark-Tutorial/data/people.csv")

  df5.printSchema()
  df5.show(10, false)


}