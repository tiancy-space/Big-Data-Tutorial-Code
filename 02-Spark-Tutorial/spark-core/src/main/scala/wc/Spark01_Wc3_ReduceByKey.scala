package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc3_ReduceByKey {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc3").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val wordsRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")

    /*
        使用ReduceByKey实现单词统计. reduceByKey,分区内和分区间计算逻辑相同. 先分区内相同key的value进行两两元素操作,再分区间相同key进行操作.
     */
    wordsRDD.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).collect().foreach(println)
  }

}
