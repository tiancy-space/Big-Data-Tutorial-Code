package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: mapValues算子使用案例.
 * @Author: tiancy
 * @Create: 2022/6/21
 */
object Spark13_RDD_Transform_MapValues {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("mapValues").setMaster("local[*]")
    val sc = new SparkContext(conf)
    // TODO mapValues用法: 只适用于 key-value类型的数据集合. 保持key不变,仅仅通过当前算子内的处理逻辑,处理value的值.

    // 获取分区内的最大值.
    val rdd1: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6), 2)
    val partitionsMax: RDD[Int] = rdd1.glom().map(_.max)
    partitionsMax.collect().foreach(println) // 3 6

    /*
      TODO  按照当前rdd1内的元素进行分组,分组后求分区内的最大值.分组规则: 元素 奇、偶数
        可以看到: 经过 groupBy 算子分组后产生的新的RDD,是一个元组,第一个元素是分组标记,第二个是相同组内元素的迭代器.
        获取当前组内的最大值,就可以直接获取当前元组内的最大值即可,也就是可以直接处理key-value中的value值.可以直接使用mapValues算子.
     */
    val groupByRdd: RDD[(Int, Iterable[Int])] = rdd1.groupBy(_ % 2)
    // mapValues算子: 直接忽略掉key部分,直接处理values部分
    val everyGroupMax: RDD[(Int, Int)] = groupByRdd.mapValues(_.max)

    everyGroupMax.collect().foreach(println) // (0,6)  (1,5)
    sc.stop()
  }
}
