package rdd.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 演示累加器的定义与使用.
 * @Author: tiancy
 * @Create: 2022/6/23
 */
object Spark02_Acc_定义与使用 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("acc1").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd1: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8))
    /*
        TODO 累加器(分布式共享只写变量) 使用步骤
          **1.创建累加器对象**
          **2.在算子内部使用累加器
          **3.在Driver端本地查看合并结果**
     */
    // 1、在Driver端定义一个累加器对象,并声明当前变量的名称
    val sum: LongAccumulator = sc.longAccumulator("sum")

    rdd1.foreach(
      num => {
        // 当前写法会报错.需要使用当前的累加器对象调用方法实现功能.
        // sum += num
        sum.add(num)
      }
    )
    // 获取当前累加器对象的最终结果
    println(sum.value) // 36

    sc.stop()
  }
}
