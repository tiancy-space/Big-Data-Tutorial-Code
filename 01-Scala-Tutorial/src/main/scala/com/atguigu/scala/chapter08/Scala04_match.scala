package com.atguigu.scala.chapter08

/*
  TODO Scala - 模式匹配 - 样例类
    样例类的定义、单例对象、apply()方法以及提取器unapply()方法.
 */
object Scala04_match {

  def main(args: Array[String]): Unit = {

    /*
       样例类: case
          1. 样例类提供了伴生对象，提供了常用的一些方法: apply , unapply ....
          2. 样例类会将主构造器的构造参数直接作为对象的属性来使用.
             不需要var 或者 val 来声明. 默认使用val声明.

          3. 样例类已经实现了序列化接口.
     */

    //val user04 = new User04("zhangsan",30)

    val user04 = User04("zhangsan",30)

    //user04.username = "lisi"

    var result  = user04 match{
      case User04("zhangsan",30) => "ok"
      case _ => "no"
    }

    println(result)
  }
}

// 样例类

case class User04( username :String, age :Int ){}