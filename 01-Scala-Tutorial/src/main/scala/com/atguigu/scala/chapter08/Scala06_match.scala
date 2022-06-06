package com.atguigu.scala.chapter08

/**
  * Scala - 模式匹配  - 商品点击排行
  */
object Scala06_match {

  def main(args: Array[String]): Unit = {
    var datas = List(
      ("zhangsan", "河北", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "鞋"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河南", "衣服"),
      ("wangwu", "河南", "鞋"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "鞋"),
      ("zhangsan", "河北", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "帽子"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河南", "衣服"),
      ("wangwu", "河南", "帽子"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "帽子"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "电脑"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河南", "衣服"),
      ("wangwu", "河南", "电脑"),
      ("zhangsan", "河南", "电脑"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "帽子")
    )

    //1. 清洗数据
    val etlDatas: List[(String, String)] = datas.map {
      case (name, prov, prod) => (prov, prod)
    }
    println(etlDatas)

    //2.省份 - 商品分组

    //etlDatas.groupBy( t => t )
    val provAndProdGroup: Map[(String, String), List[(String, String)]] = etlDatas.groupBy {
      case (prov, prod) => (prov, prod)
    }
    println(provAndProdGroup)


    //3.统计商品的点击次数
    val provAndProdnum: Map[(String, String), Int] = provAndProdGroup.mapValues {
      case prodGroup: List[_] => prodGroup.size
    }
    println(provAndProdnum)

    //4. 按照省份分组
    val provGroup: Map[String, Map[(String, String), Int]] = provAndProdnum.groupBy {
      case ((prov, prod), num) => prov
    }
    println(provGroup)

    //5. 转换结构
    val provGroupCount: Map[String, Map[String, Int]] = provGroup.mapValues {
      case values => values.map {
        case ((prov, prod), num) => (prod, num)
      }
    }
    println(provGroupCount)

    //6. 排序
    val result: Map[String, List[(String, Int)]] = provGroupCount.mapValues {
      case values => values.toList.sortBy {
        case (prod, num) => num
      }(Ordering.Int.reverse)
    }
    println(result)
  }
}
