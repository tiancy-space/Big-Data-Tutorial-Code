package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc5_FoldByKey {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc3").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val wordsRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")

    /*
        使用 foldByKey 实现单词统计. 当前算子主要解决分区内和分区间计算逻辑相同的情况,用来替代 aggregateByKey
          第一个参数列表: 指定初始值. 第二个参数列表: 需要两个参数: 分区内分区间相同的计算逻辑.
     */
    wordsRDD.flatMap(_.split(" ")).map((_, 1)).foldByKey(0)(_ + _).collect().foreach(println)
  }

}
