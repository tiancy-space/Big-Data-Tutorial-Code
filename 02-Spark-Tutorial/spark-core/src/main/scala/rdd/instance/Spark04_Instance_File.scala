package rdd.instance

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/20
 */
object Spark04_Instance_File {
  def main(args: Array[String]): Unit = {
    // 1、声明spark运行环境. 指定配置项对象
    val conf: SparkConf = new SparkConf().setAppName("save rdd").setMaster("local[*]")
    conf.set("spark..default.parallelism", "2")
    val sc = new SparkContext(conf)

    // TODO 通过文件读取数据,并加载成RDD. 第一个参数: 指定文件的位置. 第二个配置项: 指定分区数. textFile方法的第一个参数可以设定为文件路径，也可以设定为目录的路径.
    val rdd1: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt", 4)
    rdd1.collect().foreach(println)

  }
}
