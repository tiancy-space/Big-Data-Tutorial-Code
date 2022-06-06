package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 惰性函数
  */
object Scala14_function {
  def main(args: Array[String]): Unit = {

    //定义函数

    def getData(): String  ={
      println("返回大量的数据 .....")
      "some data"
    }

    // 业务处理

    println("开始业务处理.....")

    lazy val data: String = getData()

    println("继续业务处理.....")

    println("处理data : " + data)
  }
}
