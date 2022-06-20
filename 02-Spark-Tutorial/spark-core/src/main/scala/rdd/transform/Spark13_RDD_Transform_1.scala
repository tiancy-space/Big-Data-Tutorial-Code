package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object Spark13_RDD_Transform_1 {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd : RDD[Int] = sc.makeRDD(
           List(1,2,3,4),2
        )
        //rdd.groupBy()

        // partitionBy算子可以将数据重新进行分区，默认分区的规则可以为HashPartitioner
        rdd.map((_,1)).partitionBy( new HashPartitioner(2) )
            .saveAsTextFile("output")
        // shuffle操作的默认分区规则就是HashPartitioner

        sc.stop()

    }
}
