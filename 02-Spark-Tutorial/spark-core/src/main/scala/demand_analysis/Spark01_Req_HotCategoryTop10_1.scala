package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 需求: Top10 商品品类统计
 * @Author: tiancy
 * @Create: 2022/6/24
 */
object Spark01_Req_HotCategoryTop10_1 {
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
    // 这个数据集,下面三个算子会依次调用.因此可以做一个缓存.仅调用一次.
    val cacheRDD: lineRDD.type = lineRDD.cache()
    // 写法一: 将当前数据集进行分类,形成 点击行为、下单行为、支付行为四种RDD
    val clickRDD: RDD[(String, Int)] = cacheRDD.filter(_.split("_")(6) != "-1").map(
      line => {
        val data: String = line.split("_")(6)
        //(品类,1)
        (data, 1)
      }
    )
    // 下单数据
    val orderRDD: RDD[(String, Int)] = cacheRDD.filter(
      _.split("_")(8) != "null"
    ).flatMap(
      line => {
        // 1,9,11
        val ids: String = line.split("_")(8)
        // (1,1),(9,1),(11,1)
        ids.split(",").map((_, 1))
      }
    )
    // 支付数据
    val payRDD: RDD[(String, Int)] = cacheRDD.filter(
      line => {
        line.split("_")(10) != "null"
      }
    ).flatMap(
      line => {
        line.split("_")(10).split(",").map((_, 1))
      }
    )

    // 上述的每个RDD处理后的数据格式相同,都是 品类ID,相应的操作数.
    val clickByKeyRDD: RDD[(String, Int)] = clickRDD.reduceByKey(_ + _)
    val orderByKeyRDD: RDD[(String, Int)] = orderRDD.reduceByKey(_ + _)
    val payByKeyRDD: RDD[(String, Int)] = payRDD.reduceByKey(_ + _)
    // 使用算子将多个RDD相同的key进行合并操作. 最终的结果是一个元组(品类ID,(点击统计的迭代器,下单统计后的迭代器,支付统计后的迭代器))
    val allRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickByKeyRDD.cogroup(orderByKeyRDD, payByKeyRDD)
    // 对当前相同key的三元组进行模式匹配,从而转化格式,把每个迭代器中的值取出来.最终结果数据格式: (品类ID,(点击次数,下单次数,支付次数))
    val categoryNameValues: RDD[(String, (Long, Long, Long))] = allRDD.mapValues {
      case (clickIter, orderIter, payIter) => {
        var clickCount: Long = 0
        var orderCount: Long = 0
        var payCount: Long = 0
        val click: Iterator[Int] = clickIter.iterator
        if (click.hasNext) {
          clickCount = click.next()
        }
        val order: Iterator[Int] = orderIter.iterator
        if (order.hasNext) {
          orderCount = order.next()
        }
        val pay: Iterator[Int] = payIter.iterator
        if (pay.hasNext) {
          payCount = pay.next()
        }
        (clickCount, orderCount, payCount)
      }
    }
    categoryNameValues.collect().foreach(println)
    /*
        (4,(5961,1760,1271))
        (8,(5974,1736,1238))
        (20,(6098,1776,1244))
     */
    println("********" * 5)
    // 最终将上述结果使用 sortBy算子默认排序为升序,如果想要降序，那么传递第二个参数,并指定排序的字段为元组中的第二个元素,也是一个三元组本身.
    val top10 = categoryNameValues.sortBy(_._2, false).take(10)
    top10.foreach(println)

    sc.stop()
  }
}
