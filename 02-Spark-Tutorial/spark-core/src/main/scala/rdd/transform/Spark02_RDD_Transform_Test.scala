package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Transform_Test {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
            List(1,2,3,4), 2
        )

//        rdd.mapPartitions(
//            list => {
//                List(list.max).iterator
//            }
//        ).collect().foreach(println)
        // TODO 获取分区数据的同时获取分区索引的场合，需要采用新的算子
        rdd.mapPartitionsWithIndex(
            (index, list) => {
                List((index, list.max)).iterator
            }
        ).collect().foreach(println)




        sc.stop()

    }
}
