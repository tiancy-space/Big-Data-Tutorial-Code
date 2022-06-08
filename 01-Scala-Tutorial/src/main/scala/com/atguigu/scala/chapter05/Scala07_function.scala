package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 函数作为值 -  当成函数的返回值
  */
object Scala07_function {
  def main(args: Array[String]): Unit = {

    //1. 定义函数fun(),其中它的返回值类型为 一个函数类型
    def fun() : String => String = {
      freturn
    }

    def freturn(name : String) :String = {
      name.toUpperCase
    }

    //val fresult: String => String = fun()
    //println(fresult("zhangsan"))

    println(fun()("lisi"))

    println("--------------------------------")

    def fun1(): String => Unit = {

      def freturn1(name : String) : Unit = {
        println(name.toUpperCase)
      }

      freturn1
    }

    // 函数调用,调用fun1()后,返回值是一个函数类型,其中返回的函数类型的参数为一个String,无返回值.
    fun1()("wangwu")


  }

}
