package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark12_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd1 = sc.makeRDD(
            List(1,2,3,4), 2
        )
        val rdd2 = sc.makeRDD(
            List(3,4,5,6), 2
        )
        val rdd3 = sc.makeRDD(
            List("3","4","5","6"), 2
        )

        // 交集 3,4
       // println(rdd1.intersection(rdd2).collect().mkString(","))
        //println(rdd1.intersection(rdd3).collect().mkString(","))
        // 并集 1,2,3,4,3,4,5,6
        //println(rdd1.union(rdd2).collect().mkString(","))
        // 差集 1,2
        //println(rdd1.subtract(rdd2).collect().mkString(","))

        // 拉链
        // Can only zip RDDs with same number of elements in each partition
        // Can't zip RDDs with unequal numbers of partitions: List(2, 3)
        println(rdd1.zip(rdd2).collect().mkString(","))
        println(rdd1.zip(rdd3).collect().mkString(","))

        sc.stop()

    }
}
