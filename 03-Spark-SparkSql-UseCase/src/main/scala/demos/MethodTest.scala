package demos

import bean.SourceSchema
import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame
import spark.{SparkApp, SparkIO}

/**
 * @Description: 测试自己写的一些工具类以及方法.
 * @Author: tiancy
 * @Create: 2022/7/18
 */
object MethodTest extends SparkApp {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = spark.sparkContext

    // 读取csv格式的文件,常规写法.
    val csvMapOps = Map("header" -> "true", "delimiter" -> ",", "inferSchema" -> "true")
    val moviesDF: DataFrame = spark.read.format("csv").options(csvMapOps).load("./03-Spark-SparkSql-UseCase/ml-25m/movies.csv")
    moviesDF.show(30, false)
    moviesDF.printSchema()

    val schema = new SourceSchema
    println("测试 SparkIO类中读取csv的方法......")
    SparkIO.readCsv(spark, "03-Spark-SparkSql-UseCase/ml-25m", "movies.csv", schema.getMovieSchema).show(10, false)

    println("测试jdbc读取MySql中某张表的方法 : readMySqlByJDBC ")
    val personDF: DataFrame = SparkIO.readMySqlByJDBC(spark, "person")
    personDF.show(10, false)

    sc.stop()
    spark.stop()
  }
}
