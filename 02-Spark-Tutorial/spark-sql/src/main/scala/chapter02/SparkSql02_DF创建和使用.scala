package chapter02

import java.lang

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}


/**
 * @Description: DataFrame的三种创建方式以及和RDD之间的转化
 * @Author: tiancy
 * @Create: 2022/6/28
 */
object SparkSql02_DF创建和使用 extends App {
  private val spark: SparkSession = SparkSession
    .builder()
    .appName("DataFrame...")
    .master("local[*]")
    .getOrCreate()

  private val sc: SparkContext = spark.sparkContext

  import org.apache.spark.sql.types._

  /*
      创建DataFrame的三种方式
        1、通过数据源进行创建
        2、从一个存在的RDD进行转换
        3、查询Hive表中的数据,并返回DF
   */
  // 1、通过数据源进行创建,主要使用的是 SparkSession.read. 不同数据格式中的数据读取.
  val userDF: DataFrame = spark.read.json("./02-Spark-Tutorial/data/user.json")
  userDF.createOrReplaceTempView("user")

  spark.sql(
    """
      |select * from user
      |""".stripMargin).show()

  // 2、从已有的RDD转化生成DF. 就是给当前的RDD增加结构: Row + Schema(StructType + 列属性). 也就是体现: DataFrame = RDD[Row] + Schema
  val lineRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/student.txt")

  val rddRow: RDD[Row] = lineRDD.map(_.split(" ")).map(
    line => Row(line(0).toInt, line(1), line(2).toInt, line(3), line(4), line(5).toDouble)
  )
  val schema: StructType = StructType(List(
    StructField("classId", IntegerType, false),
    StructField("name", StringType, false),
    StructField("age", IntegerType, false),
    StructField("gender", StringType, false),
    StructField("subjectName", StringType, false),
    StructField("score", DoubleType, false)
  ))
  val studentScoreDF: DataFrame = spark.createDataFrame(rddRow, schema)
  studentScoreDF.show()

  // 3、将RDD通过 toDF("列名")进行转换.
  val list = List(("张三", 20, 8888), ("李四", 30, 128000), ("王五", 26, 12000), ("赵四", 38, 15000))
  val employeeDF: DataFrame = spark.createDataFrame(list)
  employeeDF
    .withColumnRenamed("_1", "name")
    .withColumnRenamed("_2", "age")
    .withColumnRenamed("_3", "salary").show()

  private val intDs: Dataset[lang.Long] = spark.range(1, 20, 1)
  intDs.show()



  case class Student(classId: Int, name: String, age: Int, gender: String, subjectName: String, score: Double)

  case class Employee(name: String, age: Int, salary: Double)

  sc.stop()
  spark.stop()
}
