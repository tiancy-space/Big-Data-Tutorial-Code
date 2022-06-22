package rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_Persist {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val sc = new SparkContext(conf)

        val rdd = sc.textFile("data/word.txt")
        val words = rdd.flatMap(_.split(" "))
        val wordToOne = words.map(
            word => {
                println(">>>>>>>>>>>>>")
                (word, 1)
            }
        )
        // TODO 持久化
        val cacheRDD = wordToOne.cache()
        val cacheRDD1 = wordToOne.persist(StorageLevel.DISK_ONLY)
        //val cacheRDD = wordToOne.persist(StorageLevel.MEMORY_ONLY)
        // cache, persist方法存在局限性
        val wordToCount: RDD[(String, Int)] = cacheRDD.reduceByKey(_ + _)
        wordToCount.collect().foreach(println)
        println("**************************************")
        val wordToCount1 = cacheRDD.groupByKey()
        wordToCount1.collect().foreach(println)
        sc.stop()

    }
}
