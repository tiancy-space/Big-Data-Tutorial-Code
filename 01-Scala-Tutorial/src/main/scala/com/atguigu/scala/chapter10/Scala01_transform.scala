package com.atguigu.scala.chapter10

/**
 * Scala - 隐式转换 - 隐式函数
 */
object Scala01_transform {

  def main(args: Array[String]): Unit = {

    var b: Byte = 10
    var aa: Int = b // BoxesRunTime.boxToInteger(b) 小类型被大类型来接收,自动类型转换,也就是编译器帮我们自动完成了转换过程.
    println(aa) // 10

    // 编译不通过,运行到当前行,程序会报错. 需要强制类型转化. 大类型转化小类型
    var numLong: Long = 128
    // var numInt: Int = numLong

    var d: Double = 2.0D
    //1. toInt
    var i: Int = d.toInt

    //2. 自定义方法
    def transform(d: Double): Int = {
      d.toInt
    }

    val ii: Int = transform(d)

    //3.隐式转换
    // 转换规则 - 隐式函数
    implicit def transform1(d: Double): Int = {
      d.toInt
    }

/*    implicit  def transform2(d:Double): Int = {
      d.toInt
    }*/

    implicit def transform2(d: Double): Long = {
      d.toLong
    }

    // var d: Double = 2.0D
    var iii: Int = d // 以前的写法中,会编译报错(Double->Int). 加入隐士转化后 -> Scalac会查找隐式规则（Double->Int） -> implicit transform1

    println(iii)

  }


}
