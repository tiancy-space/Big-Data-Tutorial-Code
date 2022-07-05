package chapter03

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
 * @Description: 有管理表和无管理表的区别: 对应的是Hive中的内部表和外部表.
 * @Author: tiancy
 * @Create: 2022/7/5
 */
object SparkSql00_有管理表和无管理表 {
  def main(args: Array[String]): Unit = {
    /*
        有管理表: Spark既管理着元数据信息,又管理着文件存储上的数据. 如果执行删除操作: 会删除元数据 + 存储位置上的真是数据.
        无管理表: SPark仅仅管理着元数据. 如果执行删除操作:仅仅会删除元数据,不会删磁盘上的真是数据.
     */


    val spark: SparkSession = SparkSession.builder().appName("spark create table ").master("local[*]").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // 默认创建的是有管理表
    spark.sql(
      """
        |create database spark_db
        |""".stripMargin)

    // 使用 api 查看元数据信息. 查看数据库、查看表、查看某张表的列名
    spark.catalog.listDatabases().show(100, false)
    spark.catalog.listTables().show(100, false)
    spark.catalog.listColumns("spark_db.student").show(100, false)

    // spark rdd 中的cache操作,在spark sql 中也可以使用:

    //缓存表
    spark.sql("cache table ds_spark.people")
    //清除缓存表
    spark.sql("uncache table ds_spark.people")



    sc.stop()
    spark.stop()
  }

}
