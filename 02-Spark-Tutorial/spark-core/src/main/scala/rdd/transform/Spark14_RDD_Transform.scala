package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark14_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
           List(
               ("a", 1),
               ("a", 2),
               ("a", 3)
           )
        )

        // TODO : reduceByKey算子表示相同key的数据的V进行两两聚合
        //  reduceByKey算子可以实现WordCount (2 / 10)
        rdd.reduceByKey( _+_ )

        sc.stop()

    }
}
