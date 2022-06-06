package com.atguigu.scala.chapter08

/**
  * Scala - 模式匹配  - 基本语法
  */
object Scala01_match {
  def main(args: Array[String]): Unit = {

    /*
       语法:
           obj  match {

              case xxx  =>  分支体
              case xxx  =>  分支体
              case _  =>  分支体

           }

       1. 如果没有提供case_分支, 其他的分支都不能匹配，会报错
       2. case _ 分支后面的分支不起作用 , 因此 case _分支要写到最后面.
       3. 匹配成功后，执行完该分支后直接退出. 没有穿透.

     */

    var a: Int = 10
    var b: Int = 20

    var operator: Char = '+'

    var result = operator match {
      //case _ => "illegal"
      case '+' => a + b
      case '-' => a - b
      case '*' => a * b
      case '/' => a / b
      case _ => "illegal"
    }
    println(result)

  }
}
