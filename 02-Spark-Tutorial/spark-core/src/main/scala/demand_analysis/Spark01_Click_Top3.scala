package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: TODO 统计出每一个省份每个广告被点击数量排行的Top3
 * @Author: tiancy
 * @Create: 2022/6/22
 */
object Spark01_Click_Top3 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("统计每个省份广告点击量的top3").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val textRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/agent.log")
    /*
      agent.log：时间戳，省份，城市，用户，广告，中间字段使用空格分隔。
          1516609143867 6 7 64 16
      需求描述
          统计出每一个省份每个广告被点击数量排行的Top3
     */
    val provinceAdvertisementRDD: RDD[((String, String), Int)] = textRDD.map(
      line => {
        val fields: Array[String] = line.split(" ")
        // (省份,广告),点击次数 1
        ((fields(1), fields(4)), 1)
      }
    )
    // 按照(城市,广告)作为key进行分组,统计当前城市当前广告的点击次数.
    val provinceAdvertisementRDDCtn: RDD[((String, String), Int)] = provinceAdvertisementRDD.reduceByKey(_ + _)
    // 数据格式转化, (城市,广告),点击量 ==> (城市,(广告,点击量)),根据key进行分组, . 在进行组内排序,取每个城市,广告点击量的前三名.
    val groupByProvince: RDD[(String, Iterable[(String, Int)])] = provinceAdvertisementRDDCtn.map {
          // 这里使用模式匹配,改变数据集中的结构.
      case
        ((province, ad), ctn) => (province, (ad, ctn))
    }.groupByKey()
    // 对每个分区中的数据 (province,(ad,ctn)) 格式的数据的values,进行组内排序. 这里使用Scala中的集合方法.
    val top3: RDD[(String, List[(String, Int)])] = groupByProvince.mapValues(
      iter => {
        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
      }
    )
    top3.collect().foreach(println)

    sc.stop()
  }
}
