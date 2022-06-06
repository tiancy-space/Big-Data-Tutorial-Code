package com.atguigu.scala.chapter02

/**
  * Scala - 变量
  */
object Scala02_variable {
  def main(args: Array[String]): Unit = {
    // 变量 :  代词  , 指代对象

    //1. 变量的语法
    // Java :  变量类型   变量名  = 变量值
    // Scala:  var | val  变量名 :  变量类型  = 变量值
    var username : String = "zhangsan"
    var age : Int = 10

    //2. 类型可以省略  - 类型推断
    var password  = "nicai"
    var salary  = 10000.1D

    //3. 变量初始化
    /*
       Java :  先声明后初始化      or  声明的同时初始化
               int i ; i = 10 ;       int j = 10 ;

       Scala : 声明的同时初始化
     */
    var  sex : String = "man"

    //4. 可变变量var    不可变变量val

    username ="zhangxiaosan"
    println(username)

    val idCard : String = "110110110"
    //idCard = "111"
    println(idCard)


  }
}
