package rdd.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 演示累加器的使用. 累加器是在driver端定义一个变量,通过序列化后,发往每个Executor端一份,并将运算结果拉取到Driver端合并操作.
 * @Author: tiancy
 * @Create: 2022/6/23
 */
object Spark01_Acc {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("acc1").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val rdd1: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6),2)
    val resultSum: Double = rdd1.sum()
    println(s"resultSum = $resultSum")

    val reduceResult: Int = rdd1.reduce(_ + _)
    println(s"reduceResult = $reduceResult")

    var sumRes = 0
    rdd1.foreach(
      num => {
        sumRes += num
        // 在每个Executor端执行计算,并查看累加结果
        println (s"sumRes 算子内部数据累加结果 = $sumRes")
        /*
          查看算子内部累加结果,有值.我上面定义的RDD为两个分区. 1,2,3 | 4,5,6
          sumRes 算子内部数据累加结果 = 1
          sumRes 算子内部数据累加结果 = 4
          sumRes 算子内部数据累加结果 = 3
          sumRes 算子内部数据累加结果 = 9
          sumRes 算子内部数据累加结果 = 6
          sumRes 算子内部数据累加结果 = 15
         */
      }
    )
    /*
      TODO 这中写法:Spark往Executor端发送变量没有问题,但是在Executor端执行结束后,变量拉取不回来,因此查看当前变量的值为 0
        基于上述问题,`Spark`框架提供了`累加器`这种数据结构来处理. 累加器,也叫做`分布式共享只写变量`,可以将`Executor`端计算结果拉取到本地进行`合并`操作
     */
    println(s"sumRes = $sumRes") // sumRes = 0 结果为0.

    sc.stop()
  }
}
