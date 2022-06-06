package com.atguigu.scala.chapter10

/**
  * Scala - 隐式转换 - 隐式规则的查找范围
  */
object Scala05_transform extends  Scala06_transform {
  def main(args: Array[String]): Unit = {
    val mysql05 = new MySQL05

    mysql05.insert()

    mysql05.select()

    //当前代码作用域
    /*
    implicit  class MySQLExt05(mySQL05: MySQL05){
      def select(): Unit ={
        println("select..... 当前代码作用域中...")
      }
    }

     */


  }

  //当前代码父作用域
  /*
  implicit  class MySQLExt05(mySQL05: MySQL05){
    def select(): Unit ={
      println("select..... 当前代码父作用域中...")
    }
  }

   */

}




class MySQL05{

  def insert(): Unit ={
    println("insert .....")
  }
}