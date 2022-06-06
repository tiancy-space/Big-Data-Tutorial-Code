package com.atguigu.scala.chapter08

/**
  * Scala - 模式匹配 - 匹配规则
  */
object Scala02_mathc {
  def main(args: Array[String]): Unit = {
    // 1. 匹配常量

    def describe(x: Any) = x match {
        case 5 => "Int five"
        case "hello" => "String hello"
        case true => "Boolean true"
        case '+' => "Char +"
    }

    println(describe(5))
    println(describe("hello"))
    println(describe(true))
    println(describe('+'))
    //println(describe(123))

    println("-------------------------------------")

    //2. 匹配类型
    //  匹配类型的时候，不考虑泛型。
    //  数组的泛型实际上是类型.
    def describe1(x: Any) = x match {
      case i: Int => "Int"
      case s: String => "String hello"
      case m: List[_] => "List"
      case c: Array[Int] => "Array[Int]"
      case someThing => "something else " + someThing
    }

    println(describe1(123))
    println(describe1("123"))
    println(describe1(List(1,2,3)))  // List[Int]
    println(describe1(List("1","2","3")))  // List[String]
    println(describe1(Array(1,2,4)))  // Array[Int]
    println(describe1(Array("1","2","4")))  // Array[String]



    println("---------------------------------------")
    //3. 匹配数组
    for (arr <- Array(
      Array(0),  //  "0"
      Array(1, 0),  // 1,0
      Array(0, 1, 0), // 以0开头的数组
      Array(1, 1, 0), // something else
      Array(1, 1, 0, 1), //something else
      Array("hello", 90) // hello,90

    )) { // 对一个数组集合进行遍历
      val result = arr match {
        case Array(0) => "0" //匹配Array(0) 这个数组
        case Array(x, y) => x + "," + y //匹配有两个元素的数组，然后将将元素值赋给对应的x,y
        case Array(0, _*) => "以0开头的数组" //匹配以0开头的数组
        case _ => "something else"
      }
      println("result = " + result)
    }

    println("---------------------------------")
    //4. 匹配List
    for (list <- Array(
      List(0),  // 0
      List(1, 0),  //  1,0
      List(0, 0, 0), //0 ...
      List(1, 0, 0), // something else
      List(88) // something else
    )) {
      val result = list match {
        case List(0) => "0" //匹配List(0)
        case List(x, y) => x + "," + y //匹配有两个元素的List
        case List(0, _*) => "0 ..."
        case _ => "something else"
      }

      println(result)
    }


    val list: List[Int] = List(1,2,5,6,7)

    // 1 :: 2 :: List(5,6,7)
    list match {
      case first :: second :: rest => println(first + "-" + second + "-" + rest)
      case _ => println("something else")
    }

    // 5.匹配元组:
    for (tuple <- Array(
      (0, 1),  // 0 ...
      (1, 0),  // 10
      (1, 1), // 1 1
      (1, 0, 2) //something else
    )) {
      val result = tuple match {
        case (0, _) => "0 ..." // 第一个元素是0的元组
        case (y, 0) => "" + y + "0" // 匹配后一个元素是0的对偶元组
        case (a, b) => "" + a + " " + b
        case _ => "something else" //默认
      }
      println(result)
    }





  }
}
