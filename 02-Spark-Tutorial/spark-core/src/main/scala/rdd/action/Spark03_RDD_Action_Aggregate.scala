package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Action_Aggregate {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子
        val rdd = sc.makeRDD(
            List(1,2,3,4)
        )

//        val i: Int = rdd.aggregate(0)(_ + _, _ + _)
//        println(i)
        // aggregate行动算子的初始值不仅仅在分区内计算，也会在分区间计算
        val j: Int = rdd.aggregate(10)(_ + _, _ + _)
        val k: Int = rdd.fold(10)(_ + _)
        println(j)
        sc.stop()

    }
}
