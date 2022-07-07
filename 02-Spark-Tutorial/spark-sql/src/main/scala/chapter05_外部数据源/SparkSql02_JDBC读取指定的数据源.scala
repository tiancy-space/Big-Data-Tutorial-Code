package chapter05_外部数据源

import org.apache.spark.sql.SparkSession

/**
 * @Description: 使用spark sql JDBC的方式,读取指定的数据源.
 * @Author: tiancy
 * @Create: 2022/7/6
 */
object SparkSql02_JDBC读取指定的数据源 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession
      .builder()
      .appName("spark sql operator Hive")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    import org.apache.spark.sql._
    import spark.implicits._


    spark.stop()
  }
}
