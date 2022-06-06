package com.atguigu.scala.chapter06

/**
  * Scala - 面向对象编程 - 抽象
  */
object Scala10_abstract {
  def main(args: Array[String]): Unit = {

    /*
       抽象: 不完整 ， 不具体 。

       Java中的抽象:
          抽象类
          抽象方法
          一个类中声明了抽象方法，该类必须要声明成抽象类.
          一个抽象类中不一定包含抽象方法.

       Scala中的抽象:
          抽象类
          抽象方法
          抽象属性

          一个具体的类继承了抽象类后，需要将抽象类中的抽象方法补充完整. override关键字可加可不加。
          但是如果重写抽象类中的具体方法, override关键必须加.

          一个具体的类继承了抽象类后，需要将抽象类中的抽象属性补充完整. override关键可加可不加
          但是如果重写抽象类中的具体属性,override关键字必须加，且只有val声明的属性才能被重写.

     */
  }
}

abstract class User10{
  //抽象属性
  var name : String
  val password : String

  // 具体属性

  var age : Int = 30
  val sex : String = "man"



  // 抽象方法
  def testAbstractMethod(): Unit

  // 具体方法
  def testMethod():Unit = {
    println("User10 .... testMethod")
  }
}

class SubUser10 extends User10{

  // 将抽象属性补充完整
  override var name: String = _
  override val password: String = "123456"

  //重写具体属性
  //override  var age : Int = 50
  override val sex: String = "women"



  // 将抽象方法补充完整
  override  def testAbstractMethod(): Unit = {
    println("SubUser10 .... testAbstractMethod")
  }

  // 重写具体的方法
  override def testMethod(): Unit = {
    println("SubUser10 .... testMethod")
  }
}