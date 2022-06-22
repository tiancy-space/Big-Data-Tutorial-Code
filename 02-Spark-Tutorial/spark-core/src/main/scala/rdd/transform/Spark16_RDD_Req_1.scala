package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark16_RDD_Req_1 {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 统计出每一个省份每个广告被点击数量排行的Top3
        // 1. 读取原始数据
        val fileDatas = sc.textFile("./02-Spark-Tutorial/data/agent.log")

        // 2. 过滤数据 (word)
        val prvToAdCount = fileDatas.map(
            data => {
                val datas = data.split(" ")
                ( (datas(1), datas(4)), 1 )
            }
        ).reduceByKey(_+_)

        val groupDatas = prvToAdCount.map {
            case ( (prv, ad), sum ) => {
                (prv, (ad, sum))
            }
        }.groupByKey()

        groupDatas.mapValues(
            iter => {
                iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
            }
        ).collect.foreach(println)


        sc.stop()

    }
}
