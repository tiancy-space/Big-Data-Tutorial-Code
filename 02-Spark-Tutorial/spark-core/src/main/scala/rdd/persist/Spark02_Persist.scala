package rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_Persist {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val sc = new SparkContext(conf)

        val rdd = sc.textFile("./02-Spark-Tutorial/data/word.txt")
        val words = rdd.flatMap(_.split(" "))
        val wordToOne = words.map(
            word => {
                println("==map转化算子进行中....==")
                (word, 1)
            }
        )
        /*
            TODO 将上述的RDD计算结果进行持久化,wordToOne这个RDD进行持久化,在当前应用中实现重用.
                cache() 和 persist(缓存级别) 两个方法的区别:
                cache()底层调用的就是 persist(StorageLevel.MEMORY_ONLY) 这个方法,声明当前的缓存级别为仅内存中.
         */
        val cacheRDD = wordToOne.cache()
        //val cacheRDD = wordToOne.persist(StorageLevel.MEMORY_ONLY)
        // cache, persist方法存在局限性
        // TODO wordToOne 当前RDD的计算结果,在下面会达到重用效果,因此可以直接使用持久化后的 cacheRDD
        val wordToCount: RDD[(String, Int)] = cacheRDD.reduceByKey(_ + _)
        wordToCount.collect().foreach(println)
        println("**************************************")
        val wordToCount1 = cacheRDD.groupByKey()
        wordToCount1.collect().foreach(println)
        sc.stop()

    }
}
