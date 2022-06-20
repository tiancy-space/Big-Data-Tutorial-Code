package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, SparkConf, SparkContext}

object Spark02_WordCount {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(conf)

    // TODO 从文件中获取数据，实现WordCount
    // 1. 读取文件
    val lines: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")

    // 2. 分词（扁平化）
    val words: RDD[String] = lines.flatMap(
        (line: String) => {
        line.split(" ")
      }
    )
    // 3. 对单词分组
    // k = word, v = List[word, word, word]
    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy((word: String) => word)
    // 4. 分组后统计数据
    val wordCount: RDD[(String, Int)] = wordGroup.mapValues(_.size)
    // 5. 采集数据打印在控制台
    wordCount.collect().foreach(println)

    sc.stop()

  }
}