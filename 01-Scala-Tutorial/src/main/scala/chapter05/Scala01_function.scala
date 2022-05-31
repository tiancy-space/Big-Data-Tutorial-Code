package chapter05

/**
  * Scala - 函数式编程 - 基本语法 &  基本概念
  */
object Scala01_function {

  def main(args: Array[String]): Unit = {
    /*
      函数语法:
         [修饰符] def  函数名(参数列表) : 返回值类型 = {  函数体  }

      方法&函数:
         方法其实就是函数.
         方法 ： 面向对象的概念. 方法可以重载和重写.  写到类体中的函数叫方法
         函数 :  函数编程的概念. 函数不支持重载和重写，写到非类体中的叫函数.

     */

    def testFun(): Unit ={
      println("testFun....")

      def testInnerFun(): Unit ={
        println("testInnerFun...")
      }
    }
   /*
    def testFun(str : String ): Unit ={
      println("testFun....")
    }*/

  }

  def testMethod(): Unit ={
    println("testMethod")
  }

  def testMethod(str : String) : Unit = {
    println("testMethod ... str ")
  }
}
