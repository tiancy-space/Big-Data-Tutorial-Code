package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 使用GroupBy算子实现 单词统计.
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Wc1_GroupBy {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("wc1").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val wordsRdd: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")
    /*
        使用flatMap对读取到的文件数据进行转换操作, RDD[String]类型. 在按照每个单词作为标记,按照标记作为分组.结果(word,iter(....)).
        再使用mapValues对当前 key-value类型的RDD的value进行处理,并保留key.
     */
    wordsRdd.flatMap(
      line => {
        line.split(" ")
      }
    ).groupBy(word => word).mapValues(_.size).collect().foreach(println)
    sc.stop()
  }

}
