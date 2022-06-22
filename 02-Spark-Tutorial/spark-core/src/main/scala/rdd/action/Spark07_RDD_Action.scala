package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark07_RDD_Action {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子

        // TODO 闭包
        // 函数的内部使用了外部的数据，并且改变这个数据的生命周期，将数据包含到函数的内部环境中，形成闭合的效果，这个环境称之为闭包环境，简称闭包

        // Spark中算子的闭包操作都意味着：一定有数据需要从Driver端传递到Executor端
        // 在执行Job之前，需要判断传递的数据是否需要序列化。

        // 在执行作业之前，对闭包数据的检测功能称之为闭包检测

        val rdd = sc.makeRDD(
            List[Int]()
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
