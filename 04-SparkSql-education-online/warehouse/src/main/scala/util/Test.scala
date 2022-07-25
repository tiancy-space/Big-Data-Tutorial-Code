package util

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("dwd_qz_controller").setMaster("local[*]")
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    val ssc = sparkSession.sparkContext
    import sparkSession.implicits._
    val r1 = ssc.makeRDD(List((1001, "张三", "18"))).toDF("id", "name", "age")
    val r2 = ssc.makeRDD(List((1001, "schoo001"))).toDF("id", "schoolname")
    //    r1.join(r2, r1("id") === r2("id")).show()
    r1.join(r2, (Seq("id"))).show()
  }
}

