package com.atguigu.scala.chapter06

import java.lang.reflect.Constructor

/**
  * Scala - 面向对象编程  - 对象
  */
object Scala07_object {
  def main(args: Array[String]): Unit = {
    //1. 创建对象
    //1.1  new
    //val user07 = new User07()

    //1.2  反射
    //Class.forName("com.atguigu.scala.chapter06.User07")
    //user07.getClass
    //User07.class

    //val user07Class: Class[User07] = classOf[User07]
    //user07Class.newInstance()  // 无参数构造器
    //val cons: Constructor[User07] = user07Class.getDeclaredConstructor(classOf[String])
    //cons.newInstance("zhangsan")

    //1.3  反序列化

    //1.4  apply方法
    //     apply一般定义到伴生对象中， 用于创建伴生类的对象
    //     Scala能自动识别apply方法,可以省略不写.
    //
    //val user07 = User07.apply()
    val user07 = User07()
    println(user07.getClass.getName)   //com.atguigu.scala.chapter06.User07
    println(user07.username)


    val abc = User07("abc")

  }
}

class User07 private {   // 构造器私有化
   var username : String = "zhangsan"
}

// 伴生对象可以访问伴生类中的私有内容.
object User07{

  def apply():User07={
    new User07()
  }

  def apply(name : String ):String ={
   name
  }

  def a():User07={
    new User07()
  }


}
