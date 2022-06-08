package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 函数作为值 - 传递给参数
  */
object Scala06_function {
  def main(args: Array[String]): Unit = {
    //1. 定义函数,当前函数的参数为 函数类型 [输入String,输出String]
    def fun( f : String =>String  ) : Unit = {
      // 给当前传递进来的函数指定参数. 也就是调用函数执行.
      println(f("zhangsan"))
    }

    //2. 定义fun函数的参数:也是一个函数,其中具体的操作: 给定一个名称,将名称打印两遍
    def fparam( name : String ) : String = {
       name * 2
    }
    // 真正的函数调用,其中函数的参数为一个函数,也就是我们上面写好的函数 fparam.
    fun(fparam) // zhangsanzhangsan


    println("-------------------------------------")

    def fun1( f : String => Unit  ) : Unit = {
      f("lisi")
    }

    def fparam1( name : String ) : Unit = {
      println( name * 2 )
    }

    fun1(fparam1) // lisilisi
  }

}
