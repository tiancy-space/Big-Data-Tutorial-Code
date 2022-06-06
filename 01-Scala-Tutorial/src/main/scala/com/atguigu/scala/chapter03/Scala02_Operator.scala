package com.atguigu.scala.chapter03

object Scala02_Operator {
  def main(args: Array[String]): Unit = {
    var a = new String("abc")
    var b = new String("abc")

    println(a == b )        // true  编译后查看 就是  非空判断 + equals的操作

    println(a.equals(b))    // true   编译后查看就是equals的操作
    println( a equals b )

    // 比较地址
    println(a.eq(b))  //  false  编译后查看就是 ==
    println(a eq b )


  }
}
