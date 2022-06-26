package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @Description: 需求: 在品类Top10的基础上,进行每个用户session的点击统计.
 * @Author: tiancy
 * @Create: 2022/6/24
 */
object Spark02_HotCategoryTop10ToSession10 {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10")
    val sc = new SparkContext(conf)

    val fileDatas = sc.textFile("./02-Spark-Tutorial/data/user_visit_action.txt")
    fileDatas.cache()
    val datas = fileDatas.flatMap(
      line => {
        val datas = line.split("_")
        if (datas(6) != "-1") {
          // 点击数据
          List((datas(6), (1, 0, 0)))
        } else if (datas(8) != "null") {
          // 下单数据
          val ids = datas(8)
          ids.split(",").map(
            id => {
              (id, (0, 1, 0))
            }
          )
        } else if (datas(10) != "null") {
          // 支付数据
          val ids = datas(10)
          ids.split(",").map(
            id => {
              (id, (0, 0, 1))
            }
          )
        } else {
          Nil
        }
      }
    ).reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )

    val top10 = datas.sortBy(_._2, false).take(10)
    val top10Ids: Array[String] = top10.map(_._1)

    // TODO 需求二 *************************************************
    val actionDatas = fileDatas.map(
      line => {
        val datas = line.split("_")
        UserVisitAction(
          datas(0),
          datas(1).toLong,
          datas(2),
          datas(3).toLong,
          datas(4),
          datas(5),
          datas(6).toLong,
          datas(7).toLong,
          datas(8),
          datas(9),
          datas(10),
          datas(11),
          datas(12).toLong
        )
      }
    )

    // TODO 将数据进行筛选过滤
    //println(actionDatas.count())
    val filterData = actionDatas.filter(
      action => {
        if (action.click_category_id != -1) {
          // 这里需要注意: 热门品类Top10中存放在一个数组中,每个品类类型是一个字符串. 而样例类中给定的当前字段类型为Long类型.
          top10Ids.contains(action.click_category_id.toString)
        } else {
          false
        }
      }
    )
    //println(filterData.count())

    val reduceDatas = filterData.map(
      action => {
        ((action.click_category_id, action.session_id), 1)
      }
    ).reduceByKey(_ + _)

    val groupDatas = reduceDatas.map {
      case ((cid, sid), cnt) => {
        (cid, (sid, cnt))
      }
    }.groupByKey()

    groupDatas.mapValues(
      iter => {
        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
      }
    ).collect().foreach(println)

    sc.stop()

  }

  //用户访问动作表
  case class UserVisitAction(
                              date: String, //用户点击行为的日期
                              user_id: Long, //用户的ID
                              session_id: String, //Session的ID
                              page_id: Long, //某个页面的ID
                              action_time: String, //动作的时间点
                              search_keyword: String, //用户搜索的关键词
                              click_category_id: Long, //某一个商品品类的ID
                              click_product_id: Long, //某一个商品的ID
                              order_category_ids: String, //一次订单中所有品类的ID集合
                              order_product_ids: String, //一次订单中所有商品的ID集合
                              pay_category_ids: String, //一次支付中所有品类的ID集合
                              pay_product_ids: String, //一次支付中所有商品的ID集合
                              city_id: Long //城市 id
                            )

}