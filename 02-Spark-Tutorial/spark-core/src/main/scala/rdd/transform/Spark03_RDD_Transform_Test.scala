package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Transform_Test {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的转换算子
        val rdd = sc.makeRDD(
            List(List(1,2),3,List(4,5))
        )

        rdd.flatMap {
            case list : List[_] => list
            case other => List(other)
        }.collect().foreach(println)



        sc.stop()

    }
}
