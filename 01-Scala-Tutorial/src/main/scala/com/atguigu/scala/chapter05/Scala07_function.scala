package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 函数作为值 -  当成函数的返回值
  */
object Scala07_function {
  def main(args: Array[String]): Unit = {

    //1. 定义函数
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

    fun1()("wangwu")


  }

}
