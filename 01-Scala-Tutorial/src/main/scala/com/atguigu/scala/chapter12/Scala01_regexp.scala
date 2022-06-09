package com.atguigu.scala.chapter12

import scala.util.matching.Regex

/*
  TODO  Scala - 正则表达式的定义、使用
 */
object Scala01_regexp {
  def main(args: Array[String]): Unit = {

    //定义正则表达式对象.
    val r: Regex = "[s|S]cala".r

    val str = "ScalaA is Scalable Language scala"
    // 通过当前定义的正则表达式对象调用方法 findFirstIn,返回匹配到的第一个字符串. 结果是一个Option类型.
    val option: Option[String] = r.findFirstIn(str)
    println(option.getOrElse("匹配不到")) //Scala

    println("-------------------------")
    val iterator: Regex.MatchIterator = r.findAllIn(str)
    iterator.foreach(println) // Scala Scala scala


    println("-------------------------")

    // 需求: 匹配手机号
    val phoneR: Regex = "[0-9]{11}".r

    var phoneNum = "10010010011"

    val maybeString: Option[String] = phoneR.findFirstIn(phoneNum)
    println(maybeString.getOrElse("不是手机号")) // 10010010011


    val regex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))[0-9]{8}$".r

  }
}
