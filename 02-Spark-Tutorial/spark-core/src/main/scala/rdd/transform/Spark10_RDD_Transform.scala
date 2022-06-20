package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark10_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
            List(1,2,3,4,5,6), 3
        )

        // TODO 缩减分区
        //val newRDD: RDD[Int] = rdd.coalesce(2)
        val newRDD: RDD[Int] = rdd.coalesce(2, true)
        newRDD.saveAsTextFile("output")

        sc.stop()

    }
}
