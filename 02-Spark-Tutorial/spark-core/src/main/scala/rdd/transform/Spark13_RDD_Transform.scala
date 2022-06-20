package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark13_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd : RDD[Int] = sc.makeRDD(
           // List(1,2,3,4),2
            List(
                1,4,5,7,9,2,3,6,8,0
            ), 2
        )
        rdd.sortBy(num=>num).saveAsTextFile("output")

//        val newRDD: RDD[(Int, Int)] = rdd.map((_, 1))

        // TODO 编译一定会出现错误。
        //rdd.partitionBy()
        // 当编译器发现错误之后，会在当前的作用域范围内，查找可以编译通过的转换操作，形成二次编译
        // TODO 隐式转换
        // partitionBy算子的主要作用是数据的重分区
        // 1. repartition算子主要作用是分区数量的重新设置
        // 2. partitionBy算子主要作用是分区数据的重新分配
//        newRDD.partitionBy()

        sc.stop()

    }
}
