package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 使用行动算子 countByKey实现单词统计. 7
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc7_CountByKey {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc countByKey").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val lineRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")
    /**使用countByKey 实现 wordCount.需要先将RDD转化为key-value类型的格式,否则不能调到算子. */
    val wordCount: collection.Map[String, Long] = lineRDD.flatMap(_.split(" ")).map((_, 1)).countByKey()
    println(wordCount)

    sc.stop()
  }
}
