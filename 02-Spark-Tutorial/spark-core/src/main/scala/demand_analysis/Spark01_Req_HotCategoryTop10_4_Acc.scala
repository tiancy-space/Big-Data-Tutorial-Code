package demand_analysis


import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark01_Req_HotCategoryTop10_4_Acc {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10")
    val sc = new SparkContext(conf)

    /*
      TODO Top10热门品类: 这里的热门按照: 每个品类id的 点击、下单、支付数 综合方面给出统计.
        最终统计结果为: 品类id,(点击数,下单数,支付数),并按照点击、下单、支付降序排序,取前十.
        数据格式:
          点击日期、用户ID、用户sessionId、页面ID、事件发生时间、搜索关键字、点击品类ID(6)、点击商品ID、下单品类IDS(8)、下单商品IDS、支付品类IDS(10)、支付商品IDS、城市ID
          2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_37_2019-07-17 00:00:02_手机_-1_-1_null_null_null_null_3
          2019-07-17_95_26070e87-1ad7-49a3-8fb3-cc741facaddf_48_2019-07-17 00:00:10_null_16_98_null_null_null_null_19
          2019-07-17_39_e17469bf-0aa1-4658-9f76-309859dcd641_47_2019-07-17 00:02:59_null_-1_-1_15,9,3_30_null_null_21
          2019-07-17_39_e17469bf-0aa1-4658-9f76-309859dcd641_4_2019-07-17 00:02:56_null_-1_-1_null_null_15,1,16_52,77_6
       实现方式: 累加器实现. acc("品类ID","品类操作类型").返回值类型为一个Map[品类id,自定义的样例类(品类ID,点击数,下单数,支付数)]
     */

    // 1. 读取数据文件
    val fileDatas = sc.textFile("./02-Spark-Tutorial/data/user_visit_action.txt")

    // 2. 使用累加器实现数据的统计
    // 2.1 创建累加器
    val hcAcc = new HotCategoryAccmulator()
    // 2.2 注册累加器
    sc.register(hcAcc, "HotCategory")

    fileDatas.foreach(
      line => {
        val datas: Array[String] = line.split("_")
        if (datas(6) != "-1") {
          // 点击的场合
          hcAcc.add((datas(6), "click"))
        } else if (datas(8) != "null") {
          // 下单的场合
          val ids = datas(8).split(",")
          ids.foreach(
            id => {
              hcAcc.add((id, "order"))
            }
          )
        } else if (datas(10) != "null") {
          // 支付的场合
          val ids = datas(10).split(",")
          ids.foreach(
            id => {
              hcAcc.add((id, "pay"))
            }
          )
        }
      }
    )

    // 3. 将统计结果进行排序处理，取前10条
    val accResult: mutable.Map[String, HotCategoryCountAnalysis] = hcAcc.value
    val analyses: mutable.Iterable[HotCategoryCountAnalysis] = accResult.map(_._2)
    //        analyses.toList.sortBy(
    //            data => {
    //                (data.clickCount, data.orderCount, data.payCount)
    //            }
    //        )
    // sortWith方法将你预期的结果返回true
    analyses.toList.sortWith(
      (hca1, hca2) => {
        if (hca1.clickCount > hca2.clickCount) {
          true
        } else if (hca1.clickCount == hca2.clickCount) {
          if (hca1.orderCount > hca2.orderCount) {
            true
          } else if (hca1.orderCount == hca2.orderCount) {
            hca1.payCount > hca2.payCount
          } else {
            false
          }
        } else {
          false
        }
      }
    ).take(10).foreach(println)

    sc.stop()

  }

  /**
   * 定义一个格式转化的样例类.
   * 并且在声明当前样例类时,id是一个val类型的变量: 原因 不会发生修改.而其他参数,则是需要不断进行变化的.
   *
   * @param id         品类ID
   * @param clickCount 点击行为当前品类ID的统计值.
   * @param orderCount 下单行为当前品类ID的统计值.
   * @param payCount   支付行为当前品类ID的统计值.
   */
  case class HotCategoryCountAnalysis(id: String, var clickCount: Long, var orderCount: Long, var payCount: Long)

  /**
   * 1. 继承AccumulatorV2
   * 2. 定义泛型
   * IN : 累加器输入的数据类型 (品类ID, String, 行为类型 String), 输入一个品类ID和当前品类ID的操作: "click"、"order"、"pay"
   * OUT : 累加器输出的数据类型 Map[品类, (品类，（点击数量，下单数量，支付数量）)]
   * 3. 重写方法（3 + 3）
   */
  class HotCategoryAccmulator extends AccumulatorV2[(String, String), mutable.Map[String, HotCategoryCountAnalysis]] {
    // 定义一个成员变量,用来将最终的结果包装返回. key:品类ID. v:当前品类、某个操作的操作数.
    private val hcMap = mutable.Map[String, HotCategoryCountAnalysis]()

    /** 判断当前累加器是否为初始化状态 此方法主要作用:防止多台Executor在分布式环境下,有的节点由于网络阻塞,而读取到已经计算好返回给 Driver的非空集合. */
    override def isZero: Boolean = {
      hcMap.isEmpty
    }

    /** 将当前Driver中定义的变量复制,再分发到不同的计算节点上使用. */
    override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategoryCountAnalysis]] = {
      new HotCategoryAccmulator
    }

    /** 如果当前节点使用的Driver端的变量不是空的,则说明这个集合是某台节点的返回值与本地合并的结果.我需要重置此集合再计算返回. */
    override def reset(): Unit = {
      hcMap.clear()
    }

    /** 当前通过累加器传递过来的数据,如何处理: 这里是拿当前(品类ID,操作类型) 合并到当前分区中的空集合中. */
    override def add(v: (String, String)): Unit = {
      val (hcid, actionType) = v // 累加器被调用时,传过来的两个变量. 品类Id,操作类型: click、order、pay
      //判断当前累加器对象中的Map中是否存在当前传过来的key
      val analysis: HotCategoryCountAnalysis = hcMap.getOrElse(hcid, HotCategoryCountAnalysis(hcid, 0, 0, 0))

      // 根据传过来的类型,字符串格式,来进行模式匹配:
      actionType match {
        case "click" => analysis.clickCount += 1
        case "order" => analysis.orderCount += 1
        case "pay" => analysis.payCount += 1
      }
      // 更新待返回的集合.
      hcMap.update(hcid, analysis)

    }
    /** 另一个节点上的累加器对象,取出它的value值,变量其他节点上的map,与当前节点上的map进行合并操作. */
    override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategoryCountAnalysis]]): Unit = {
      val map2 = other.value
      map2.foreach {
        case (hcid, hca2) => {
          val hca1: HotCategoryCountAnalysis = hcMap.getOrElse(hcid, HotCategoryCountAnalysis(hcid, 0, 0, 0))

          hca1.clickCount += hca2.clickCount
          hca1.orderCount += hca2.orderCount
          hca1.payCount += hca2.payCount

          hcMap.update(hcid, hca1)
        }
      }
    }

    override def value: mutable.Map[String, HotCategoryCountAnalysis] = {
      hcMap
    }
  }

}