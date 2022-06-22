package rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Spark行动算子功能演示:collect、foreach、reduce、aggregate、fold、first、take、takeOrdered、count、countByKey、countByValue、save
 */
object Spark01_RDD_Action_Reduce {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 4, 2, 1), 2)
    // 聚集RDD 中的所有元素，先聚合分区内数据，再聚合分区间数据
    val reduceResult: Int = rdd.reduce(_ + _) // 简化规约:先分区内聚合,在分区间聚合.
    println(reduceResult) // 8

    // 将RDD分散存储的元素转换为单机上的Scala数组并返回，类似于toArray功能
    val collectArray: Array[Int] = rdd.collect()
    val collectStr: String = collectArray.mkString(",")
    println(collectStr) // 1,4,2,1

    // 返回该 RDD 排序后的前 n 个元素组成的数组
    val takeOrderedArray: Array[Int] = rdd.takeOrdered(3)
    val takeOrderedStr: String = takeOrderedArray.mkString(",")
    println(takeOrderedStr)

    // **统计 `Rdd` 中的数据量，如果我们需要统计多个表的数据量，会将此函数的返回值存取到 ArrayBuffer 中**
    val countResult: Long = rdd.count()
    println(countResult) // 4

    // 统计当前rdd中的第一个元素
    val firstResult: Int = rdd.first()
    println(firstResult) // 1

    // take: 返回rdd中前n个元素
    val takeArray: Array[Int] = rdd.take(3)
    val takeStr: String = takeArray.mkString(",")
    println(takeStr) // 1,4,2



    sc.stop()
  }
}