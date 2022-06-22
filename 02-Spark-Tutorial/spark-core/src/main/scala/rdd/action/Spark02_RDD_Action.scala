package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Action {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子
        val rdd = sc.makeRDD(
            List(1,4,3,2), 2
        )

        // reduce : 简化，规约
        val i: Int = rdd.reduce(_ + _)
        println(i)

        // collect ：采集
        // 行动算子的结果一般都是在Driver端处理
        val ints: Array[Int] = rdd.collect()
        println(ints.mkString(","))

        val l: Long = rdd.count()
        println(l)

        val i1: Int = rdd.first()
        println(i1)

        //val ints1: Array[Int] = rdd.take(3)

        val ints2: Array[Int] = rdd.takeOrdered(3)
        println(ints2.mkString(","))
        sc.stop()

    }
}
