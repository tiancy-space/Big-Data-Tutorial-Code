package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @Description: 需求: Top10 商品品类统计,优化写法
 * @Author: tiancy
 * @Create: 2022/6/24
 */
object Spark01_Req_HotCategoryTop10_4_累加器写法 {
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
    /*
      TODO 2、Top10热门品类, 使用累加器写法.
        这里需要明确累加器实现的功能. 可以直接使用foreach算子遍历当前的数据集,每遍历到一条,就将当前条数据发送给累加器,累加器判断它是什么行为.
        根据当前的操作行为,进行 相同的品类ID的操作统计.
        累加器的使用步骤.
          1).创建累加器类型的对象.
          2).注册累加器
          3).调用累加器的方法.
     */
    val hotCategoryAcc = new HotCategoryTop10Acc
    sc.register(hotCategoryAcc, "acc1")


    fileDatas.foreach(
      line => {
        val fieldsValue: Array[String] = line.split("_")
        val clickCategoryId: String = fieldsValue(6)
        val orderCategoryIds: String = fieldsValue(8)
        val payCategoryIds: String = fieldsValue(10)
        if (clickCategoryId != "-1") {
          // 点击行为,调用累加器.
          hotCategoryAcc.add(clickCategoryId, "click")
        } else if (orderCategoryIds != "null") {
          // 下单行为,将当前多个下单ID转化为多个.
          orderCategoryIds.split(",").foreach(
            id => {
              hotCategoryAcc.add(id, "order")
            }
          )
        } else if (payCategoryIds != "null") {
          // 支付行为
          payCategoryIds.split(",").foreach(
            id => {
              hotCategoryAcc.add(id, "pay")
            }
          )
        }
      }
    )

    // 获取当前累加器对象,获取它的结果.
    val categoryIdMap: mutable.Map[String, CategoryCountCase] = hotCategoryAcc.value
    //    val categoryIdMap: mutable.Map[String, CategoryCountCase] = hotCategoryAcc.value
    // 获取当前map的value值.也就是一个个的统计结果
    val categoryCaseClassIter: mutable.Iterable[CategoryCountCase] = categoryIdMap.map(_._2)

    /** 为什么要使用这种排序呢? 原因: 当前要排序的是一个对象类型,需要我们自己手动指定排序规则 */
    // sortWith() 方法使用: 将自己的预期结果返回true即可.
    val sortWithCtn: List[CategoryCountCase] = categoryCaseClassIter.toList.sortWith(
      (hca1, hca2) => {
        if (hca1.clickCount > hca2.clickCount) { // if-1
          true
        } else if (hca1.clickCount == hca2.clickCount) { // if-1
          if (hca1.orderCount > hca2.orderCount) { // if-2
            true
          } else if (hca1.orderCount == hca2.orderCount) { // if-2
            hca1.payCount > hca2.payCount
          } else { // if-2
            false
          }
        } else { // if-1
          false
        }
      }
    )
    sortWithCtn.take(10).foreach(println)

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
  case class CategoryCountCase(id: String, var clickCount: Long, var orderCount: Long, var payCount: Long)

  /**
   * 1. 继承AccumulatorV2
   * 2. 定义泛型
   * IN : 累加器输入的数据类型 (品类ID, String, 行为类型 String), 输入一个品类ID和当前品类ID的操作: "click"、"order"、"pay"
   * OUT : 累加器输出的数据类型 Map[品类, (品类，（点击数量，下单数量，支付数量）)]
   * 3. 重写方法（3 + 3）
   */
  class HotCategoryTop10Acc extends AccumulatorV2[(String, String), mutable.Map[String, CategoryCountCase]] {

    // 定义一个成员变量,用来将最终的结果包装返回. key:品类ID. v:当前品类、某个操作的操作数.
    private val hcMap: mutable.Map[String, CategoryCountCase] = {
      mutable.Map[String, CategoryCountCase]()
    }

    /** 判断当前累加器是否为初始化状态 此方法主要作用:防止多台Executor在分布式环境下,有的节点由于网络阻塞,而读取到已经计算好返回给 Driver的非空集合. */
    override def isZero: Boolean = {
      hcMap.isEmpty
    }

    /** 将当前Driver中定义的变量复制,再分发到不同的计算节点上使用. */
    override def copy(): AccumulatorV2[(String, String), mutable.Map[String, CategoryCountCase]] = {
      new HotCategoryTop10Acc
    }

    /** 如果当前节点使用的Driver端的变量不是空的,则说明这个集合是某台节点的返回值与本地合并的结果.我需要重置此集合再计算返回. */
    override def reset(): Unit = {
      hcMap.clear()
    }

    /** 当前通过累加器传递过来的数据,如何处理: 这里是拿当前(品类ID,操作类型) 合并到当前分区中的空集合中. */
    override def add(v: (String, String)): Unit = {
      val (categoryId, operationType) = v // 将当前传过来的元组中的值,给到一个变量上,方便下面操作.

      // 从当前累加器的返回结果中,也就是当前Map hcMap,通过 `categoryId`取当前的样例类.如果不存在,则返回 (categoryId,0,0,0)
      val currentCaseClass: CategoryCountCase = hcMap.getOrElse(categoryId, CategoryCountCase(categoryId, 0, 0, 0))

      // 根据当前传入的操作类型,进行模式匹配.
      operationType match {
        case "click" => currentCaseClass.clickCount += 1
        case "order" => currentCaseClass.orderCount += 1
        case "pay" => currentCaseClass.payCount += 1
      }
      // 更新待返回的集合.
      hcMap.updated(categoryId, currentCaseClass)
    }

    /** 当前计算完成的计算结果 与另一台计算完成的集合 otherMap 进行相同key的value合并 */
    override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, CategoryCountCase]]): Unit = {
      // 遍历另一个节点累加器对象的结果.并和当前节点上的结果Map进行合并操作.
      val otherMap: mutable.Map[String, CategoryCountCase] = other.value
      otherMap.foreach {
        // 进行模式匹配
        case (otherCategoryId, otherCategoryCaseClass) => {
          // 判断当前节点上的累加器对象中,是否存在当前 categoryId对应的样例类对象.
          val currentCategoryCase: CategoryCountCase = hcMap.getOrElse(otherCategoryId, CategoryCountCase(otherCategoryId, 0, 0, 0))
          currentCategoryCase.clickCount += otherCategoryCaseClass.clickCount
          currentCategoryCase.orderCount += otherCategoryCaseClass.orderCount
          currentCategoryCase.payCount += otherCategoryCaseClass.payCount

          //更新当前节点上的累加器对象中的返回值.
          hcMap.updated(otherCategoryId, currentCategoryCase)
        }
      }
    }

    /** 最终计算的结果,一个Map集合返回给Driver端的调用者 */
    override def value: mutable.Map[String, CategoryCountCase] = {
      hcMap
    }
  }

}
