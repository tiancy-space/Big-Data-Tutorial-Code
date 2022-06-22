package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark13_RDD_Transform {

  def main(args: Array[String]): Unit = {
    val path: String = System.getProperty("user.dir")
    println(path) // B:\DevTools\IdeaProjects\Big-Data-Tutorial-Code

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(
      // List(1,2,3,4),2
      List(
        1, 4, 5, 7, 9, 2, 3, 6, 8, 0
      ), 2
    )
    // 使用 sortBy算子对当前RDD内的数据进行排序. 排序方式: 先进行重新分区操作,在分区内进行局排序.
    rdd.sortBy(num => num).saveAsTextFile("./02-Spark-Tutorial/output") // 结果两个文件, 文件1: 0,1,2,3,4 文件2: 5,6,7,8,9

    //        val newRDD: RDD[(Int, Int)] = rdd.map((_, 1))
    val newRDD: RDD[(Int, Int)] = rdd.map((_, 1))
    // newRDD.partitionBy()


    /*
      看如下现象: 使用 rdd: RDD[Int] 类型的rdd对象,连当前算子 partitionBy 都调不到.而使用 newRDD: RDD[(Int, Int)] 算子则能调到算子.
      当编译器发现错误之后，会在当前的作用域范围内，查找可以编译通过的转换操作，形成二次编译
     TODO 隐式转换
       partitionBy算子的主要作用是数据的重分区
       1. repartition算子主要作用是分区数量的重新设置
       2. partitionBy算子主要作用是分区数据的重新分配
     */
    // TODO 编译一定会出现错误。
    // rdd.partitionBy() // 连算子都调用不到,编译不通过
    // newRDD.partitionBy() // 可以调到算子

    sc.stop()
  }
}
