package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO groupBy
        val rdd = sc.makeRDD(
            List(1,2,3,4),3
        )

        val newRDD: RDD[(Int, Iterable[Int])] = rdd.groupBy(num => {
            num % 2
        }, 2)
        newRDD.collect.foreach(println)



        sc.stop()

    }
}
