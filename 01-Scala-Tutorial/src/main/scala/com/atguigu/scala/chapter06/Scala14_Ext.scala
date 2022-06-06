package com.atguigu.scala.chapter06

object  Scala14_Ext {
  def main(args: Array[String]): Unit = {
    println(Color.RED)
    println(Color.RED.id)

  }
}


// 枚举类2
object Color extends Enumeration {

  val RED = Value(1, "red")

  val YELLOW = Value(2, "yellow")

  val BLUE = Value(3, "blue")
}

// 应用类
object AppTest extends App {

  println("application");

}

