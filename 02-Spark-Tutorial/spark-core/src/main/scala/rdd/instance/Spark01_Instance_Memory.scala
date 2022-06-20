package rdd.instance

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark01_Instance_Memory {

  def main(args: Array[String]): Unit = {
    // 1、创建 spark 运行环境,指定配置文件对象、sparkContext上下文对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd instance")
    val sc = new SparkContext(conf)

    // TODO 创建RDD(数据模型). 数据源为内存 . 两种方式 : 并行、makeRDD,当前两个方法的参数都是一个集合.

    /*
      parallelize方法用于将内存集合作为数据源创建RDD模型。
     */
    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5, 6))
    rdd1.collect().foreach(println)
    /*
      第二种创建RDD的方法更好记忆,见名之意.
     */
    val rdd2: RDD[String] = sc.makeRDD(List("Hello", "spark", "hello", "scala", "hello", "flink"))
    rdd2.collect().foreach(println)

    sc.stop()
  }
}