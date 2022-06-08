package com.atguigu.scala.chapter04

/**
  * Scala - 循环控制 - for
  */
object Scala02_for {
  def main(args: Array[String]): Unit = {
    /*
      语法:
          for( 循环变量  <- 数据集){  循环体 }
     */
    for(i <- "abc" ){
      println("i = " + i ) // [a,b,c]
    }

    println("--------------------")

    for(i <- 1 to 5 ){   // [1,2,3,4,5]
      println("i = " + i )
    }

    println("---------------------")
    for(i <- 1 until 5){  //[1,2,3,4]
      println("i = " + i)
    }
    println("---------------------")
    // Range.inclusive(1,5)方法使用说明: 使用步长值 1 从开始到结束创建一个包含范围。
    for(i <- Range.inclusive(1,5)){
      println("i = " + i )
    }
    println("---------------------")
    for(i <- Range(1,5)){   // 直接调用Range类的的apply方法,避免了通过new当前类产生对象的过程.步长为1,不包含后面的5
      println("i = " + i ) // 1 to 4
    }


    println("--------------循环步长---------------")

    for(j <- 1 to 5 by 2 ){
      println("j = " + j )
    }

    println("---------------------")

    for(j <- 1 until 5 by 2 ){
      println("j = " + j )
    }

    println("---------------------")

    for(i <- Range.inclusive(1,5,2)){
      println("i = " + i )
    }
    println("---------------------")
    for(i <- Range(1,5,2)){   // apply
      println("i = " + i )
    }


    println("----------------------------")

    val list = List("hello","scala","spark",2,false)

    for(ele <- list){
      println(ele)
    }

  }
}
