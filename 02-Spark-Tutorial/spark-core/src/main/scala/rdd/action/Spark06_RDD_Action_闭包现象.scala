package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_Action_闭包现象 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO RDD的行动算子
    val rdd = sc.makeRDD(
      List(
        1, 2, 3, 4
      ), 2
    )
    val user = new User()
    rdd.foreach(
      num => {
        println(user.age + num)
      }
    )

    sc.stop()

  }

  class User() {
    val age = 30
  }

}
