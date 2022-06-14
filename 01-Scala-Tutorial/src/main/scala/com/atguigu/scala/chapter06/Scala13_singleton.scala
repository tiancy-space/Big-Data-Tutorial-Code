package com.atguigu.scala.chapter06

/**
  * Scala - 面向对象编程 - 单例对象
  */
object Scala13_singleton {
  def main(args: Array[String]): Unit = {
    val user1 = User13()
    val user2 = User13()

    println(user1 eq user2) //  true


  }
}

/*
  单例类,定义一个类并且使用 private指定构造器私有.
  在定义一个和 User13类名相同的伴生对象,并且当前伴生对象可以访问伴生类中私有的内容. 因此可以直接new.
  并将最终new的对象直接放在 apply方法中. apply方法就是为了免new的操作.通过调用 apply可以直接创建对象.
 */
class User13 private {

}
// 伴生对象可以访问伴生类的私有内容
// 伴生对象就是单例对象。
object User13{

  var user13 : User13  = new User13

  def apply() : User13={
    //new User13
    user13
  }
}


