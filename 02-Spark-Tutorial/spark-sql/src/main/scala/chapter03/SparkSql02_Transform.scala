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

    // 去重并统计
    val distinctCount: Long = employeeDF.distinct().count()
    println(distinctCount)

    // 按照指定的列值去重并返回结果集
    val distinctByCol: Dataset[Row] = employeeDF.dropDuplicates("employee_id")
    distinctByCol.show()


    sc.stop()
    spark.stop()
  }
}
