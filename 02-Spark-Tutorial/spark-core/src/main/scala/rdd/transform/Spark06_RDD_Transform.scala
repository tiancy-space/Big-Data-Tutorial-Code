package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO groupBy
        val rdd = sc.makeRDD(
            List("Hello", "hive", "hbase", "Hadoop")
        )

        rdd.groupBy(_.head).collect().foreach(println)

        /*
            (h,CompactBuffer(hive, hbase))
            (H,CompactBuffer(Hello, Hadoop))
         */



        sc.stop()

    }
}
