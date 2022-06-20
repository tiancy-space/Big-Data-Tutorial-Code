package rdd.instance

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark03_Instance_Memory_PatitionsSave {

  def main(args: Array[String]): Unit = {
    // 1、创建 spark 运行环境,指定配置文件对象、sparkContext上下文对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd instance")
    // 通过配置项指定分区数. 这里指定的是2,查看结果: 保存文件的个数
    conf.set("spark.default.parallelism", "2")
    val sc = new SparkContext(conf)

    // TODO 创建RDD(数据模型). 数据源为内存 . 两种方式 : 并行、makeRDD,当前两个方法的参数都是一个集合. 在内存中的结果,按照分区保存到指定位置

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
    // 查看保存文件的结果,声明的元素: 1 - 6 共六个. 而我创建sc对象时,则使用了 local[*] . 我的电脑是4核8线程.但是指定了分区数为两个
    rdd1.saveAsTextFile("./02-Spark-Tutorial/output")
    sc.stop()
  }
}