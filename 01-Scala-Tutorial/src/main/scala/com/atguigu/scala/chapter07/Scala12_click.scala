package com.atguigu.scala.chapter07

/**
  * Scala - 集合 - 不同省份商品点击排行
  */
object Scala12_click {
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
    //  ("zhangsan", "河北", "鞋")  =>  ("河北", "鞋")
    val etlDatas: List[(String, String)] = datas.map( t => (t._2,t._3) )

    //2. 按照省份-商品分组
    //  ("河北", "鞋") ,  ("河北", "鞋")   =>  (("河北", "鞋") => List( ("河北", "鞋") , ("河北", "鞋") )  )
    val provAndprodGroup: Map[(String, String), List[(String, String)]] = etlDatas.groupBy( t => t )
    println(provAndprodGroup)

    //3. 求省份-商品的点击次数
    // (("河北", "鞋") => List( ("河北", "鞋") , ("河北", "鞋") )  )  =>  (("河北", "鞋") -> 2  )
    val provAndProdNum: Map[(String, String), Int] = provAndprodGroup.mapValues(_.size)
    println(provAndProdNum)
    //4.按照省份分组
    // (("河北", "鞋") -> 2 , (河南,鞋) -> 6 )  =>  ("河北" -> Map(("河北", "鞋") -> 2 ) , "河南" -> Map(  (河南,鞋) -> 6 ) )
    val provGroup: Map[String, Map[(String, String), Int]] = provAndProdNum.groupBy(_._1._1)
    println(provGroup)

    //5. 转换数据结构
    // (河南,鞋) -> 6   =>  鞋 -> 6
     val provAndProdCount: Map[String, List[(String, Int)]] = provGroup.mapValues(_.map( t => ( t._1._2 , t._2) ).toList)
    println(provAndProdCount)

    //6. 按照商品的点击次数排序
    val result: Map[String, List[(String, Int)]] = provAndProdCount.mapValues(_.sortBy(_._2)(Ordering.Int.reverse))
    println(result)

  }
}
