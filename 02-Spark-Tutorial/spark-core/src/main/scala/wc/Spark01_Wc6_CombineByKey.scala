package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 单词统计的第六种写发: 使用算子 combinByKey
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc6_CombineByKey {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc3").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val wordsRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")

    /*
        使用 combineByKey 实现单词统计. 三个参数: 第一个参数 : 每个分区中相同key的第一个value如何变化. 第二个参数: 分区内计算逻辑.第三个参数:分区间计算逻辑.
          第一个参数列表: 指定初始值. 第二个参数列表: 需要两个参数: 分区内分区间相同的计算逻辑.
     */
    val wordToOne: RDD[(String, Int)] = wordsRDD.flatMap(_.split(" ")).map((_, 1))
    wordToOne.combineByKey(
      // 每个分区中相同key的第一个value不需要发生变化即可
      value => value,
      // 分区内的计算逻辑: 这里需要声明出相同key的两个value值的数据类型
      (v1:Int,v2:Int) => {v1 + v2},
      // 分区间计算逻辑: 同样需要声明相同key的两个value值的数据类型
      (v1:Int,v2:Int) => {v1 + v2}
    ).collect().foreach(println)
  }
}
