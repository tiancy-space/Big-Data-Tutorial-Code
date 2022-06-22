package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark16_RDD_Req {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO 统计出每一个省份每个广告被点击数量排行的Top3
    // 1. 读取原始数据
    val fileDatas = sc.textFile("data/agent.log")
    /*
        时间戳，        省份，城市，用户，广告，中间字段使用空格分隔。
        1516609143867  6    7    64   16
     */

    // 2. 过滤数据
    val prvToAdDatas = fileDatas.map(
      data => {
        val datas = data.split(" ")
        (datas(1), datas(4))
      }
    )

    // 3. 按照省份进行分组
    val groupDatas: RDD[(String, Iterable[String])] = prvToAdDatas.groupByKey()

    // 4. 组内对广告进行统计（word, count）
    // 5. 组内统计后对结果进行排序，取前3名
    val top3: RDD[(String, List[(String, Int)])] = groupDatas.mapValues(
      // 对当前分组后的values进行处理,每个城市的广告id出现一次,就代表点击一次.因此需要将数据进行转化. 格式为 (广告id,1)
      wordlist => {
        val adGroupMap: Map[String, Iterable[(String, Int)]] = wordlist.map((_, 1)).groupBy(_._1)
        val adToCountMap: Map[String, Int] = adGroupMap.mapValues(_.size)
        adToCountMap.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
      }
    )

    // 6. 将结果打印在控制台上
    top3.collect().foreach(println)

    sc.stop()

  }
}
