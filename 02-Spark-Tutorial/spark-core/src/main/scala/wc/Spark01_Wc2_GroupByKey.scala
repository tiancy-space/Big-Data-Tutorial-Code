package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc2_GroupByKey {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc1").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val wordsRdd: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")

    /*
        使用groupByKey实现单词统计. 实现方式二.
     */
    wordsRdd.flatMap(_.split(" ")).map((_, 1)).groupByKey().mapValues(_.size).collect().foreach(println)
  }

}
