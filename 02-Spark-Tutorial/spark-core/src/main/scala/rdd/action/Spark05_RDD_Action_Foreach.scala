package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Action_Foreach {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子
        val rdd = sc.makeRDD(
            List(
                ("a", 1), ("a", 4), ("a", 3),("a", 2)
            ),2
        )

        // collect按照分区顺序将数据从Executor端拉取回到Driver端
        //rdd.collect().foreach(println)
        rdd.foreach(println)

        sc.stop()

    }
}
