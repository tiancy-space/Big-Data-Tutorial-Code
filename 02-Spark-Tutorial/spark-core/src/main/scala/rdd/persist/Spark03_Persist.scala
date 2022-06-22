package rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_Persist {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val sc = new SparkContext(conf)
        sc.setCheckpointDir("cp")

        val rdd = sc.textFile("data/word.txt")
        val words = rdd.flatMap(_.split(" "))
        val wordToOne = words.map(
            word => {
                //println(">>>>>>>>>>>>>")
                (word, 1)
            }
        )
        // TODO 检查点
        // Checkpoint directory has not been set in the SparkContext
        // 检查点操作会导致作业多次执行,性能比较低,所以一般需要和cache联合使用

        // cache操作会在血缘关系中增加依赖关系
        //wordToOne.cache()
        // checkpoint操作会切断血缘关系
        wordToOne.checkpoint()
        val wordToCount: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
        println(wordToCount.toDebugString)
        println("**************************************")
        wordToCount.collect()//.foreach(println)
        println(wordToCount.toDebugString)

//        val wordToCount1 = wordToOne.groupByKey()
//        wordToCount1.collect().foreach(println)
        sc.stop()

    }
}
