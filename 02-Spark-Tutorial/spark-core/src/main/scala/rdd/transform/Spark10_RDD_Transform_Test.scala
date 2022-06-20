package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark10_RDD_Transform_Test {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
            List(1,2,3,4,5,6), 3
        )

        // TODO 重分区
        // coalesce算子在不shuffle的情况下，无法扩大分区
        //val newRDD: RDD[Int] = rdd.coalesce(6, true)
        val newRDD = rdd.repartition(6)
        newRDD.saveAsTextFile("output")

        sc.stop()

    }
}
