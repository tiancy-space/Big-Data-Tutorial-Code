package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 函数作为值 - 传递给参数
  */
object Scala06_function {
  def main(args: Array[String]): Unit = {
    //1. 定义函数
    def fun( f : String =>String  ) : Unit = {
      println(f("zhangsan"))
    }

    //2. 给函数的参数传值
    def fparam( name : String ) : String = {
       name * 2
    }
    fun(fparam)


    println("-------------------------------------")

    def fun1( f : String => Unit  ) : Unit = {
      f("lisi")
    }

    def fparam1( name : String ) : Unit = {
      println( name * 2 )
    }

    fun1(fparam1)
  }

}
