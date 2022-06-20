package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Transform_Test_1 {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
            List(1,2,3,4,5,6), 3
        )

        rdd.mapPartitionsWithIndex(
            (index, list) => {
                if ( index == 1 ) {
                    list
                } else {
                    Nil.iterator
                }
            }
        ).collect.foreach(println)




        sc.stop()

    }
}
