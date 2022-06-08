package com.atguigu.scala.chapter05

import scala.util.control.Breaks

/**
  * Scala - 函数式编程 - 控制抽象
  */
object Scala12_function {
  def main(args: Array[String]): Unit = {
    // 所谓的控制抽象实际是Scala支持将一段代码作为参数进行传递.

    // 需求: 从1 循环到5, 当循环到3，跳出循环

    Breaks.breakable {

      for(i <- 1 to 5 ){
        if(i == 3) {
          Breaks.break()
        }
        println(" i = " + i )
      }

    }
    println("end....")

    println("----------------------------------------")



    def operator( op : => Unit ){
      println("--------1----------")
      op
      println("--------2----------")
    }

    //operator( println( "haha")  )

    // 定义一个名字为operator的代码块,通过当前 op 这个变量来传递当前段代码.
    operator{

      var age : Int = 30
      if(age <18){
        println("未成年")
      }else{
        println("已成年")
      }

    }
  }
}
