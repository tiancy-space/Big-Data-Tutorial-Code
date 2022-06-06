package com.atguigu.scala.chapter08

/**
  * Scala - 模式匹配 - 匹配对象
  */
object Scala03_match {
  def main(args: Array[String]): Unit = {
    // 匹配对象实际上就是比较对象的内容.
    // 匹配对象会默认调用 unapply方法进行对象内容的解析.
    val user03 = new User03("zhangsan",30 , "beijing")

    val result: String = user03 match {
      case User03("zhangsan", 40) => "ok"
      case _ => "no"
    }

    println(result)

  }
}

class User03(var username : String , var age : Int , var address : String  ){

}

object User03{

  // unapply
  def unapply(user: User03): Option[(String, Int )] = {
    if (user == null)
      None
    else
      Some(user.username, user.age)
  }

}
