package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的转换算子
        // 所谓的转换算子，其实就是通过方法将功能叠加在一起
        val rdd = sc.makeRDD(
            List(1,2,3,4), 2
        )

        // TODO map算子就是转换算子，可以在旧的RDD的基础上实现数据的转换
        // map算子会将数据集中的每一条数据进行map处理,这里的处理不是固定的方式，可以是任意的处理
        //rdd.map(_*2)
//        def mapFunction(num:Int): Int = {
//            num * 2
//        }

        //val newRDD: RDD[Int] = rdd.map(mapFunction)
//        val newRDD: RDD[Int] = rdd.map(
//            (num:Int) => {
//                num * 2
//            }
//        )
        val newRDD: RDD[Int] = rdd.map(
            num => {
                println("num >>>>>>>>> " + num)
                num
            }
        )
        val newRDD1: RDD[Int] = newRDD.map(
            num => {
                println("num ######## " + num)
                num
            }
        )
        newRDD1.collect()//.foreach(println)
        //newRDD.saveAsTextFile("output")


        sc.stop()

    }
}
