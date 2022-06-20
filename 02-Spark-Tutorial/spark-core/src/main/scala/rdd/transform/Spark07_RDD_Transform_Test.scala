package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark07_RDD_Transform_Test {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO filter
        val rdd = sc.textFile("data/apache.log")

        rdd.filter(
            line => {
                val datas = line.split(" ")
                val time = datas(3)
                time.startsWith("17/05/2015")
            }
        ).map(
            line => {
                val datas = line.split(" ")
                datas(6)
            }
        ).collect().foreach(println)



        sc.stop()

    }
}
