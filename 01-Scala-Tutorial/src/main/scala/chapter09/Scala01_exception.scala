package chapter09

import java.io.{File, FileInputStream}

/**
  * Scala - 异常
  */
object Scala01_exception {

  def main(args: Array[String]): Unit = {
    // 1. catch只能写一次.
    // 2. Scala中不区分编译时异常和运行时异常. 因此没有throws关键字.
    // 3. Java调用Scala的方法，如果明确异常   thorws[异常类型]


    new FileInputStream(new File(""))

    testException()

    try{


    }catch {
      // 模式匹配
      case ex : ArithmeticException => println("算术异常")
      case ex : NullPointerException => println("空指针异常")
      case _ => println("其他异常")

    }finally{

    }
  }

  @throws[Exception]
  def testException(): Unit ={
    throw new RuntimeException("抛异常了...")
  }
}
