package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_Req_PageFlow {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10")
        val sc = new SparkContext(conf)

        val fileDatas = sc.textFile("data/user_visit_action.txt")
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

        // TODO 计算分母数据,每行数据包含一个sessionId,每个session都是一次页面请求. 因此只需要统计页面访问次数,就是页面跳转的总分母.
        actionDatas.cache()
        val pageClickDataMap = actionDatas.map(
            action => {
                (action.page_id, 1)
            }
        ).reduceByKey(_+_).collect().toMap

        // TODO 计算分子数据
        val sessionGroupDatas: RDD[(String, Iterable[UserVisitAction])] =
            actionDatas.groupBy(_.session_id)
        // 同一个sessionId对应页面跳转顺序. (1,2,3,4,5) ==> (1,2),(2,3),(3,4),(4,5)
        val zipDatas : RDD[(String, List[(Long, Long)])] = sessionGroupDatas.mapValues(
            iter => {
                val sortActions: List[UserVisitAction] = iter.toList.sortBy(_.action_time)
                val pageids: List[Long] = sortActions.map(_.page_id)
                val tuples: List[(Long, Long)] = pageids.zip(pageids.tail)
                tuples
            }
        )

        val pageidDatas: RDD[(Long, Long)] = zipDatas.map(_._2).flatMap(list => list)

        val resultDatas: RDD[((Long, Long), Int)] = pageidDatas.map((_, 1)).reduceByKey(_ + _)

        // TODO 计算单跳转换率
        resultDatas.foreach {
            case ( (pageid1, pageid2), cnt ) => {
                println(s"页面跳转【${pageid1}-${pageid2}】的转换率为：" + (cnt.toDouble / pageClickDataMap.getOrElse(pageid1, 1)))
            }
        }

        sc.stop()

    }
    //用户访问动作表
    case class UserVisitAction(
          date: String,//用户点击行为的日期
          user_id: Long,//用户的ID
          session_id: String,//Session的ID
          page_id: Long,//某个页面的ID
          action_time: String,//动作的时间点
          search_keyword: String,//用户搜索的关键词
          click_category_id: Long,//某一个商品品类的ID
          click_product_id: Long,//某一个商品的ID
          order_category_ids: String,//一次订单中所有品类的ID集合
          order_product_ids: String,//一次订单中所有商品的ID集合
          pay_category_ids: String,//一次支付中所有品类的ID集合
          pay_product_ids: String,//一次支付中所有商品的ID集合
          city_id: Long//城市 id
      )
}
