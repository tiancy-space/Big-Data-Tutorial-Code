package com.atguigu.scala.chapter12

import scala.util.matching.Regex

/**
  * Scala - 正则
  */
object Scala01_regexp {
  def main(args: Array[String]): Unit = {
    //定义正则
    val r: Regex = "[s|S]cala".r

    val str = "Scala is Scalable Language scala"

    val option: Option[String] = r.findFirstIn(str)
    println(option.getOrElse("匹配不到"))

    println("-------------------------")
    val iterator: Regex.MatchIterator = r.findAllIn(str)
    iterator.foreach(println)


    println("-------------------------")
    // 需求: 匹配手机号


    val phoneR: Regex = "[0-9]{11}".r

    var phoneNum = "10010010011"

    val maybeString: Option[String] = phoneR.findFirstIn(phoneNum)
    println(maybeString.getOrElse("不是手机号"))


    val regex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))[0-9]{8}$".r

  }
}
