package com.atguigu.scala.chapter07

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Scala - 集合 - list
 */
object Scala02_list {
  def main(args: Array[String]): Unit = {
    //1. 不可变list
    val list: List[Int] = List[Int](1, 2, 3, 4, 5)
    println(list) // List(1, 2, 3, 4, 5)
    // 操作 获取当前列表中的第一个元素.
    println(list(0)) // 1
    println(list.head) // 1
    println(list) // List(1, 2, 3, 4, 5)

    // 符号操作
    val str: String = list + "A" // List(1, 2, 3, 4, 5)A

    //val list1: List[Int] = list.:+(6)
    // 给当不可变的列表添加元素,并形成一个新的列表,并将生成新的列表的地址给到 list1
    val list1: List[Int] = list :+ 6
    println(list1) // List(1, 2, 3, 4, 5, 6)

    // 以：结尾的符号操作,是反向操作
    //val list2: List[Int] = list.+:(6)
    val list2: List[Int] = 6 +: list
    println(list2) // // List(6, 1, 2, 3, 4, 5)

    val list3: List[Int] = 5 :: 6 :: 7 :: 8 :: list
    println(list3) // List(5, 6, 7, 8, 1, 2, 3, 4, 5)
    println(Nil) // List()

    val list4: List[Int] = 1 :: 2 :: 3 :: 4 :: Nil
    println(list4) // List(1, 2, 3, 4)

    val list5: List[Any] = 5 :: 6 :: 7 :: 8 :: list :: Nil
    println(list5) // List(5, 6, 7, 8, List(1, 2, 3, 4, 5))

    val list6: List[Any] = 5 :: 6 :: 7 :: 8 :: list ::: Nil
    println(list6) //  List(5, 6, 7, 8, 1, 2, 3, 4, 5)

    println("----------------------------------------")
    //2. 可变list

    //val listBuffer = new ListBuffer[Int]()

    val listBuffer: ListBuffer[Int] = ListBuffer(1, 2, 3, 4, 5)

    println(listBuffer) // ListBuffer(1, 2, 3, 4, 5)

    //操作
    listBuffer.append(6, 7, 8) // ListBuffer(1, 2, 3, 4, 5, 6, 7, 8)
    println(listBuffer)

    listBuffer.insert(0, 10, 11, 12) // ListBuffer(10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8)
    println(listBuffer)

    listBuffer.remove(0)
    println(listBuffer) // ListBuffer(11, 12, 1, 2, 3, 4, 5, 6, 7, 8)

    listBuffer.remove(0, 2) // ListBuffer(1, 2, 3, 4, 5, 6, 7, 8)
    println(listBuffer)

    listBuffer.update(0, 0)
    println(listBuffer) // ListBuffer(0, 2, 3, 4, 5, 6, 7, 8)

    //3. 可变  不可变 转换

    val list7 = List(1, 2, 3)

    val listBuffer1: ListBuffer[Int] = ListBuffer(5, 6, 7)

    // 可变 -> 不可变

    val BuuToLii: List[Int] = listBuffer1.toList

    // 不可变 -> 可变
    val LiiToBuu: mutable.Buffer[Int] = list7.toBuffer

  }
}
