package rdd.persist

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_IO_1 {

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

        println(sc.textFile("output").collect().mkString(","))
        println(sc.objectFile[(String, Int)]("output1").collect().mkString(","))
        println(sc.sequenceFile[String, Int]("output2").collect().mkString(","))

        sc.stop()

    }
}
