package com.atguigu.scala.chapter02

/**
  * Scala - 标识符
  */
object Scala03_identifier {
  def main(args: Array[String]): Unit = {
    var username : String = "zhangsan"
    var username1 : String = "lisi"
    var userName : String = "wangwu"

    //符号 => 编译后 => 转义符
    //原则: 想写啥就写啥， 报错换一个
    var ~ = 123
    var ! = 123
    //var @ = 123
    var @@ = 123
    //var # = 123
    var ## = 123
    var $ = 123
    var % = 123
    var ^ = 123
    var & = 123
    var * = 123
    var - = 123
    var + = 123
    //var : = 123
    var :: = 123
    //var " = 123
    var < = 123
    var > = 123
    var ? = 123

    Scala03_identifier.:->()
  }

  def :->(): Unit = {
    println(":->")
  }
}
