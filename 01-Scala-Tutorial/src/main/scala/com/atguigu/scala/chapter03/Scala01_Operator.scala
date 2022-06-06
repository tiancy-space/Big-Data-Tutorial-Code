package com.atguigu.scala.chapter03

/**
  * Scala  - 运算符 - 本质
  */
object Scala01_Operator {
  def main(args: Array[String]): Unit = {
    // 运算符其实都是方法.

    var i : Int = 10
    var j : Int = 10

    //var result = i + j
    // var result = i.+(j)
    // 省略 .
    //var result  = i +(j)
    // 省略()
    var result = i + j

    println(result)
  }
}
