package rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_Persist {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val sc = new SparkContext(conf)

        val rdd = sc.textFile("data/word.txt")
        val words = rdd.flatMap(_.split(" "))
        val wordToOne = words.map((_, 1))
        val wordToCount: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
        wordToCount.collect().foreach(println)
        println("**************************************")
        val rdd1 = sc.textFile("data/word.txt")
        val words1 = rdd1.flatMap(_.split(" "))
        val wordToOne1 = words1.map((_, 1))
        val wordToCount1 = wordToOne1.groupByKey()
        wordToCount1.collect().foreach(println)
        sc.stop()

    }
}
