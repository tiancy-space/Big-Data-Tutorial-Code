package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/*
    TODO : 解决一个以前实现起来相对麻烦的小案例: 统计相邻字符串出现的次数. eg:现在有一个一个字符串"A;B;C;D;E;A;B;A;C"
        我需要统计(A,B,2)、(B,C,1)这种结果.
 */
object Spark03_RDD_Transform_巧用flatMap案例 {

  def main(args: Array[String]): Unit = {
    // 1、创建Spark上下文环境
    val conf: SparkConf = new SparkConf().setAppName("flatMap 巧用案例").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val strRDD: RDD[String] = sc.makeRDD(List("A;B;C;D;D;B;D;C;B;D;A;E;D;C;A;B"))
    val arrRDD: RDD[Array[String]] = strRDD.map(_.split(";"))
    val currentNextValue: RDD[(String, Int)] = arrRDD.flatMap(arr => {
      // 循环当前扁平化后的集合,并使用yield产生一个新的集合.集合中存放内容: (A,B,1),(B,C,1),(C,D,1),(D,D,1) ....
      for (i <- 0 until (arr.length - 1)) yield (arr(i) + "," + arr(i + 1), 1)
    })
    /*
        (A,B,1) (B,C,1) (C,D,1) (D,D,1) (D,B,1) (B,D,1) (D,C,1) (C,B,1) (B,D,1)
        (D,A,1) (A,E,1) (E,D,1) (D,C,1) (C,A,1) (A,B,1)
        == > 其中(A,B) 出现两次.
     */
    // currentNextValue.collect().foreach(println)
    val result: RDD[(String, Int)] = currentNextValue.reduceByKey(_ + _)
    /*
        (B,C,1) (C,D,1) (D,D,1) (D,B,1) (B,D,1) (D,C,1) (C,B,1) (B,D,1)
        (D,A,1) (A,E,1) (E,D,1) (D,C,1) (C,A,1) (A,B,2)
     */
    result.collect().foreach(println)
    sc.stop()
  }
}
