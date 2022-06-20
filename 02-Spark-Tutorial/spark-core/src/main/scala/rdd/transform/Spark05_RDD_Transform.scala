package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO groupBy
        val rdd : RDD[Int] = sc.makeRDD(
            List(1,2,3,4,5,6),3
        )
        // 【1，2】【3，4】【5，6】
        // 【2】【4】【6】
        val newRDD: RDD[Array[Int]] = rdd.glom()
        newRDD.map(_.max).sum()

        rdd.groupBy(_%2, 1)



        sc.stop()

    }
}
