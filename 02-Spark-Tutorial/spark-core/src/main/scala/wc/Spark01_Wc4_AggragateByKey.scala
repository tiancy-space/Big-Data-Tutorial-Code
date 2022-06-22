package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc4_AggragateByKey {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc3").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val wordsRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")

    /*
        使用 aggregateByKey 实现单词统计. 给定一个初始值,当前方法在使用时,参数伴有柯里化性质,也就是需要传入两个参数列表.
          第一个参数列表: 指定初始值. 第二个参数列表: 需要两个参数: 分区内计算逻辑、分区间计算逻辑.
     */
    wordsRDD.flatMap(_.split(" ")).map((_, 1)).aggregateByKey(0)(_ + _, _ + _).collect().foreach(println)
  }

}
