package chapter04

import scala.util.control.Breaks._  // 将Breaks中内容导入

/**
  * Scala - 循环控制 - for
  */
object Scala04_for {
  def main(args: Array[String]): Unit = {
    // 循环中断
    //需求: 从1 循环到 5 ， 当循环到3的时候跳出循环

    breakable{ // 捕获处理异常.

      for(i <- 1 to 5 ){
        if(i == 3){
          // break    Scala中没有break关键字
          break  //通过抛出异常的方式中断循环
        }
        println(" i = " + i )
      }

    }

    println("我咋办?")
  }
}
