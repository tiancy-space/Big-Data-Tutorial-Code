package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的转换算子
        // 所谓的转换算子，其实就是通过方法将功能叠加在一起
        val rdd = sc.makeRDD(
            List(1,2,3,4), 2
        )

        // 分布式操作
//        rdd.map(
//            num => {
//                println("***********")
//                num * 2
//            }
//        ).collect
        // TODO map算子会将数据集中的每一条数据进行处理，效率不高
        //  Spark提供了一种类似于批处理的算子(mapPartitions)优化执行
        //  一分区的数据统一一起进行处理，类似于批处理
        rdd.mapPartitions(
            list => {
                println("********************")
                list.map(_*2)
                //list.filter()
            }
        ).collect.foreach(println)



        sc.stop()

    }
}
