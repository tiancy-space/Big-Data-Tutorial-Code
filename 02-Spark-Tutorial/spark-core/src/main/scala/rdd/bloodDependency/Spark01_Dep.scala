package rdd.bloodDependency

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_Dep {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local").setAppName("WordCount")
        val sc = new SparkContext(conf)

        val lines: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")
        println(lines.toDebugString)
        println("*******************  1   **************************")
        val words: RDD[String] = lines.flatMap(_.split(" "))
        println(words.toDebugString)
        println("********************  2   *************************")
        val wordToOne: RDD[(String, Int)] = words.map(
            word => {
                //println("*********")
                (word, 1)
            }
        )
        println(wordToOne.toDebugString)
        println("****************      3*****************************")
//        val value: RDD[(String, Iterable[(String, Int)])] = wordToOne.groupBy(_._1)

        val wordToCount: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
        println(wordToCount.toDebugString)
        println("********************    4       *************************")
       lines.collect()

        sc.stop()

    }
}