package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark07_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO filter
        val rdd = sc.makeRDD(
            List("Hello", "hive", "hbase", "Hadoop")
        )

        // TODO filter算子可以对数据集中的每一条数据进行筛选过滤
        // 如果筛选结果为true，那么数据保留，如果为false，数据丢弃
        val newRDD: RDD[String] = rdd.filter(_.startsWith("H"))

        newRDD.collect().foreach(println)



        sc.stop()

    }
}
