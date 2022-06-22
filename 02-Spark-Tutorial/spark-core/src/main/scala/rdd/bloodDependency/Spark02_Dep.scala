package rdd.bloodDependency

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_Dep {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local").setAppName("WordCount")
        val sc = new SparkContext(conf)

        val lines: RDD[String] = sc.textFile("data/word.txt")
//        val lines = sc.makeRDD(
//            List("Hello Spark")
//        )
        println(lines.dependencies)
        println("*********************************************")
        val words: RDD[String] = lines.flatMap(_.split(" "))
        println(words.dependencies)
        println("*********************************************")
        val wordToOne: RDD[(String, Int)] = words.map(
            word => {
                //println("*********")
                (word, 1)
            }
        )
        println(wordToOne.dependencies)
        println("*********************************************")
//        val value: RDD[(String, Iterable[(String, Int)])] = wordToOne.groupBy(_._1)

        val wordToCount: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
        println(wordToCount.dependencies)
        println("*********************************************")
       lines.collect()

        sc.stop()

    }
}