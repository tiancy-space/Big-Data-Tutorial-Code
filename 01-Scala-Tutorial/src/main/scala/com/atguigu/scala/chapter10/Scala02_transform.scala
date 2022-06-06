package com.atguigu.scala.chapter10

/**
  * Scala - 隐式转换 - 隐式函数 - 扩展功能
  */
object Scala02_transform {
  def main(args: Array[String]): Unit = {
    val mysql02 = new MySQL02 /* with Operator02 */
    mysql02.insert()

    mysql02.select()

    // 隐式规则 - 隐式函数
    implicit  def transform(mySQL02: MySQL02) : MySQLExt02 = {
      new MySQLExt02
    }


  }
}

//功能扩展类
class MySQLExt02{

  def select(): Unit ={
    println("select.....")
  }


}



trait Operator02{

  def select(): Unit ={
    println("select.....")
  }

}


class MySQL02 /*extends Operator02 */{

  def insert(): Unit ={
    println("insert.....")
  }


  // 修改源码， 添加功能
  /*
  def select(): Unit ={
    println("select.....")
  }

   */

}
