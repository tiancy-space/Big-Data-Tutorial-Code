package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark09_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        //List(1,1,1,1).distinct
        // TODO
        val rdd = sc.makeRDD(
            List(1,1,1,1), 2
        )
        // map(x => (x, null)).reduceByKey((x, _) => x, numPartitions).map(_._1)
        // 【1，1，1，1】
        // 【（1. null）, (1, null), (1, null), (1, null)】
        // [(1, (null, null, null, null))]
        // [(1, (null, null, null))]
        // [(1, (null, null))]
        // [(1, (null))]
        // [1]
        val newRDD: RDD[Int] = rdd.distinct()
        newRDD.collect().foreach(println)

        sc.stop()

    }
}
