package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark11_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        val rdd = sc.makeRDD(
            List(1,3,2,4), 2
        )

        // 【1，3】，【2，4】
        // 【1，2】，【3，4】

        // sortBy算子默认排序为升序,如果想要降序，那么传递第二个参数
        rdd.sortBy(num=>num, false).collect.foreach(println)

        sc.stop()

    }
}
