package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark15_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
           List(
               ("a", 1),
               ("a", 2),
               ("a", 3)
           )
        )

        // TODO groupByKey
        //  groupByKey算子也能实现WordCount (3 / 10)
        val newRDD1: RDD[(String, Iterable[(String, Int)])] = rdd.groupBy(_._1)
        val newRDD2: RDD[(String, Iterable[Int])] = rdd.groupByKey()

        sc.stop()

    }
}
