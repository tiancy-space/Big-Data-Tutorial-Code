package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Transform_Test {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的转换算子

        // map
        val rdd: RDD[String] = sc.textFile("data/apache.log")

        val urlData = rdd.map(
            line => {
                line.split(" ")(6)
            }
        )
        urlData.collect().foreach(println)

        sc.stop()

    }
}
