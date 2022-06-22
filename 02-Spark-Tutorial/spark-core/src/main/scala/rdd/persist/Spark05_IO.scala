package rdd.persist

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object Spark05_IO {

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

        rdd.saveAsTextFile("output")
        rdd.saveAsObjectFile("output1")
        // KV数据才能使用
        rdd.saveAsSequenceFile("output2")

        sc.stop()

    }
}
