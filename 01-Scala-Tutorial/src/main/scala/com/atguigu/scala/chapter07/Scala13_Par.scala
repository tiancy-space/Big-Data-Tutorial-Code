package com.atguigu.scala.chapter07

/**
  * Scala - 集合 - 并行
  */
object Scala13_Par {
  def main(args: Array[String]): Unit = {

    val result1 = (0 to 100).map{x => Thread.currentThread.getName}
    println(result1)

    val result2 = (0 to 100).par.map{x => Thread.currentThread.getName}
    println(result2)
  }

}
