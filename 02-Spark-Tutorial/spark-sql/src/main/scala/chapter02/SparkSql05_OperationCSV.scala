package chapter02

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description: 使用Spark操作并读取操作csv文件.
 * @Author: tiancy
 * @Create: 2022/7/1
 */
object SparkSql05_OperationCSV {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("operation csv").master("local[*]").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // 使用df读取csc文件.如果什么操作项都不指定,默认是以逗号作为列分割符,并且不以第一行作为表头,而是作为数据.
    val df: DataFrame = spark.read.csv("./02-Spark-Tutorial/data/people.csv")
    println("默认读取文件结果: =======")
    df.show()
    //+-----+----+---------+
    //|  _c0| _c1|      _c2|
    //+-----+----+---------+
    //| name| age|      job|
    //|Jorge|null|Developer|
    //| null|  32|Developer|
    //|Jorge|  30|Developer|
    //|  Bob|  32|Developer|
    //|Jorge|  30|Developer|
    //|  Bob|  32|Developer|
    //+-----+----+---------+

    // 通过配置项指定第一行作为表头. ("header","true"),默认的是以逗号作为字段的分隔符的.
    val df2: DataFrame = spark.read.option("header", "true").csv("./02-Spark-Tutorial/data/people.csv")
    println("指定第一行作为表头====")
    df2.printSchema()
    df2.show()
    //+-----+----+---------+
    //| name| age|      job|
    //+-----+----+---------+
    //|Jorge|null|Developer|
    //| null|  32|Developer|
    //|Jorge|  30|Developer|
    //|  Bob|  32|Developer|
    //|Jorge|  30|Developer|
    //|  Bob|  32|Developer|
    //+-----+----+---------+

    // 通过配置项指定(可以直接定义一个map):第一行作为表头、每数据中 列的分割符.以及是否开启类型推断 inferSchema : 所谓的类型推断,就是将 30这种由String类型推断为Integer类型.
    val operationCVSMap = Map("header" -> "true", "delimiter" -> ";", "inferSchema" -> "true")
    // 数据格式 name;age;job | Jorge;30;Developer
    val df4: DataFrame = spark.read.options(operationCVSMap).csv("./02-Spark-Tutorial/data/people1.csv")
    df4.printSchema()
    df4.show()


    sc.stop()
    spark.stop()
  }
}
