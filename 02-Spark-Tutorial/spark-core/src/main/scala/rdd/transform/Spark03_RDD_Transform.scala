package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的转换算子
        // flatMap : 扁平映射
        // 扁平化后的数据全部都需要
        sc.makeRDD(
            List(
                List(1,2), List(3,4)
            )
        ).flatMap(
            list => list
        ).collect().foreach(println)



        sc.stop()

    }
}
