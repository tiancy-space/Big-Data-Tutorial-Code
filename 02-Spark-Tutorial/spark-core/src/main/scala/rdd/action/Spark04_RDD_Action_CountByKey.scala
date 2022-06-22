package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Action_CountByKey {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子
        val rdd = sc.makeRDD(
            List(
                ("a", 1), ("a", 2), ("a", 3)
            )
        )
        // ("a", 2) => ("a", 1), ("a", 1)
        // ("a", 3) => ("a", 1),("a", 1),("a", 1)
        // TODO countByKey算子可以实现WordCount( 7 / 10 )
        //val stringToLong: collection.Map[String, Long] = rdd.countByKey()
        // 单值类型
        // TODO countByValue/算子可以实现WordCount( 8 / 10 )
        val tupleToLong: collection.Map[(String, Int), Long] = rdd.countByValue()
        
        println(tupleToLong)

        sc.stop()

    }
}
