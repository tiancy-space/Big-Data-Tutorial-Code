package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 需求: Top10 商品品类统计,优化写法
 * @Author: tiancy
 * @Create: 2022/6/24
 */
object Spark01_Req_HotCategoryTop10_3 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("category top10").setMaster("local[*]")
    val sc = new SparkContext(conf)
    /*
    TODO 1、Top10热门品类统计: 这里的热门按照: 每个品类的 点击、下单、支付数 综合方面给出统计.
      鞋			点击数 下单数  支付数
      衣服		  点击数 下单数  支付数
      电脑		  点击数 下单数  支付数
      例如，综合排名 = 点击数*20%+下单数*30%+支付数*50%
      这里统计结果改为: 先按照点击数排名，靠前的就排名高；如果点击数相同，再比较下单数；下单数再相同，就比较支付数
      数据格式如下
      点击日期、用户ID、用户sessionId、页面ID、事件发生时间、搜索关键字、点击品类ID(6)、点击商品ID、下单品类IDS(8)、下单商品IDS、支付品类IDS(10)、支付商品IDS、城市ID
      2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_37_2019-07-17 00:00:02_手机_-1_-1_null_null_null_null_3
      2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_48_2019-07-17 00:00:10_null_16_98_null_null_null_null_19
      2019-07-17_39_e17469bf-0aa1-4658-9f76-309859dcd641_47_2019-07-17 00:02:59_null_-1_-1_15,9,3_30_null_null_21
      2019-07-17_39_e17469bf-0aa1-4658-9f76-309859dcd641_4_2019-07-17 00:02:56_null_-1_-1_null_null_15,1,16_52,77_6
     */
    val lineRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/user_visit_action.txt")
    val fileDatas: lineRDD.type = lineRDD.cache()
    // TODO Top10热门品类, 可以不使用过滤,直接将数据集中每行数据,直接转化为 (品类,(点击,下单,支付)) 格式.

    val categoryIdTuple3RDD: RDD[(String, (Int, Int, Int))] = fileDatas.flatMap(
      line => {
        val fields: Array[String] = line.split("_")
        val clickField: String = fields(6) // 点击列
        val orderField: String = fields(8) // 下单列
        val payField: String = fields(10) // 支付列.
        if (clickField != "-1") {
          List((clickField, (1, 0, 0)))
        } else if (orderField != "null") {
          // 一行转多行的过程.
          orderField.split(",").map((_, (0, 1, 1)))
        } else if (payField != "null") {
          // 一行转多行的过程. 存在多个品类ID
          payField.split(",").map((_, (0, 0, 1)))
        } else {
          // 其他行的数据,直接放到空集合中.
          Nil
        }
      }
    )
    val reduceByCategoryID: RDD[(String, (Int, Int, Int))] = categoryIdTuple3RDD.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )

    val top10: Array[(String, (Int, Int, Int))] = reduceByCategoryID.sortBy(_._2, false).take(10)

    top10.foreach(println)

    sc.stop()
  }
}
