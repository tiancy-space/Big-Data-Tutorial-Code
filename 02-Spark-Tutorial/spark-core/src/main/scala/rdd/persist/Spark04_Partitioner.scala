package rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object Spark04_Partitioner {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
            List(
                ("nba", 1),
                ("cba", 2),
                ("wnba", 3),
                ("nba", 4)
            ), 2
        )

        // TODO 自定义数据的分区
        rdd.partitionBy( new MyPartitioner() ).saveAsTextFile("output")


        sc.stop()

    }
    // 自定义分区器
    // 1. 继承Partitioner类
    // 2. 重写方法(2)
    class MyPartitioner extends Partitioner{
        // 获取分区数量
        override def numPartitions: Int = 3

        // 根据数据的key值决定分区索引
        override def getPartition(key: Any): Int = {
            key match {
                case "nba" => 0
                case "cba" => 1
                case "wnba" => 2
            }
        }
    }
}
