package chapter03

import java.util

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @Description: 演示Spark Action操作.
 * @Author: tiancy
 * @Create: 2022/7/1
 */
object SparkSql01_Action {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("spark sql action").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    /*
      关于spark-sql中csv文件的使用,可以参照官方文档: https://spark.apache.org/docs/latest/sql-data-sources-csv.html
     */
    val operationCsvMap: Map[String, String] = Map("header" -> "true", "delimiter" -> ",", "inferSchema" -> "true")
    val personDF: DataFrame = spark.read.options(operationCsvMap) csv ("./02-Spark-Tutorial/data/people.csv")

    /** show 也是一个行动算子 */
    personDF.show()

    // TODO spark-sql中行动算子: show、 collect、 collectAsList、 head、 first、 count、 take、 takeAsList、 reduce

    personDF.collect().foreach(println) // 结果是一个row类型:  [Jorge,null,Developer]

    val listRows: util.List[Row] = personDF.collectAsList()

    println(personDF.count) // 6

    // 缺省显示20行
    personDF.union(personDF).show() //两张表中字段以及字段数量相同的两张表,进行 union操作,也就是长表.

    // 显示2行
    personDF.show(2)

    // 不截断字符,一行数据转化承一个 json字符串.
    personDF.toJSON.show(false) // {"name":"Jorge","job":"Developer"}  | {"age":32,"job":"Developer"}
    // 显示10行，不截断字符
    personDF.toJSON.show(10, false)

    // 用户可以通过该接口创建、删除、更改或查询底层数据库、表、函数等。
    spark.catalog.listFunctions.show(10000, false)

    // collect返回的是数组, Array[org.apache.spark.sql.Row]
    val c1 = personDF.collect()
    // collectAsList返回的是List, List[org.apache.spark.sql.Row]
    val c2 = personDF.collectAsList()
    // 返回 org.apache.spark.sql.Row
    val h1 = personDF.head()
    val f1 = personDF.first()
    // 返回 Array[org.apache.spark.sql.Row]，长度为3
    val h2 = personDF.head(3)
    val f2 = personDF.take(3)
    // 返回 List[org.apache.spark.sql.Row]，长度为2
    val t2 = personDF.takeAsList(2)

    sc.stop()
    spark.stop()
  }
}
