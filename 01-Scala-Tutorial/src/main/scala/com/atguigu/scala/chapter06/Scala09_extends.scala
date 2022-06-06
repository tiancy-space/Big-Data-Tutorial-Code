package com.atguigu.scala.chapter06

/**
  * Scala - 面向对象编程 - 继承
  */
object Scala09_extends {

  def main(args: Array[String]): Unit = {
    /*
      Scala中也是单继承， 也是使用extends关键字。
     */

    val subUser09 = new SubUser09("zhangsan")
  }
}

class User09(name : String ){
  println("User09.... " + name )
}


class SubUser09(parentName : String ) extends User09(parentName){
  println("SubUser09....")
}

