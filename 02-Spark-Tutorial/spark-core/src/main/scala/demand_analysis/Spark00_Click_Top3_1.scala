package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: TODO 统计出每一个省份每个广告被点击数量排行的Top3
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark00_Click_Top3_1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("统计每个省份广告点击量的top3").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val textRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/agent.log")
    /*
      TODO 统计出每一个省份每个广告被点击数量排行的Top3
        时间戳，        省份，城市，用户，广告，中间字段使用空格分隔。
        1516609143867  6    7    64   16
     */
    val provinceAdRDD: RDD[(String, String)] = textRDD.map(
      line => {
        val fields: Array[String] = line.split(" ")
        // (省份,广告id) ,一行数据代表一个省份的广告id被点击了一次. 处理后的数据,可能存在同一个城市同一个广告id出现多次情况,每出现一次就是点击行为.
        (fields(1), fields(4))
      }
    )
    // 相同城市的广告id被分到同一个组,广告id每出现一次,就是一次点击行为
    val groupByProvince: RDD[(String, Iterable[String])] = provinceAdRDD.groupByKey()
    //分组后组内数据进行格式转换,并按照广告id分组,分组后对values求和.==> 排序 ==> 取前三
    val adResult: RDD[(String, List[(String, Int)])] = groupByProvince.mapValues(
      iter => {
        // 对当前分组后的values进行处理,每个城市的广告id出现一次,就代表点击一次.因此需要将数据进行转化. 格式为 (广告id,1)
        val adToOneTuple: Iterable[(String, Int)] = iter.map((_, 1))
        val adGroupBy: Map[String, Iterable[(String, Int)]] = adToOneTuple.groupBy(_._1)
        val adToCtn: Map[String, Int] = adGroupBy.mapValues(_.size)
        adToCtn.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
      }
    )
    adResult.collect().foreach(println)
    sc.stop()
  }
}
