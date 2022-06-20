package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 测试SparkCore模块的环境: 搭建Maven工程、添加pom依赖、构建Spark的环境对象
 * @Author: tiancy
 * @Create: 2022/6/14
 */
object Spark01_Env {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("spark core env").setMaster("local")
    val sc = new SparkContext(conf)
    // 获取当前系统下相对路径,主要想用相对路径写文件位置
    val path = s"${System.getProperties.getProperty("user.dir")}"
    println(path) // B:\DevTools\IdeaProjects\Big-Data-Tutorial-Code
    // 获取当前所在位置,作为相对路径.
    val value: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt")
    val words: RDD[String] = value.flatMap(_.split(" "))
    val wordToOne: RDD[(String, Int)] = words.map(word => (word, 1))
    val wordCtn: RDD[(String, Int)] = wordToOne.reduceByKey { (word1Value, word2Value) =>
      word1Value + word2Value
    }
    wordCtn.collect().foreach(println)
    sc.stop()
  }
}
