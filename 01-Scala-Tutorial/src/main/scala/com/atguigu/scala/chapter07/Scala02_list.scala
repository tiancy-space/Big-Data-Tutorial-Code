package com.atguigu.scala.chapter07

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  *  Scala - 集合 - list
  */
object Scala02_list {
  def main(args: Array[String]): Unit = {
    //1. 不可变list
    val list = List[Int](1,2,3,4,5)
    println(list)
    // 操作
    println(list(0))
    println(list)

    // 符号操作
    val str: String = list + "A"
    println(str)

    //val list1: List[Int] = list.:+(6)
    val list1: List[Int] = list :+ 6
    println(list1)

    // 以：结尾的符号操作,是反向操作
    //val list2: List[Int] = list.+:(6)
    val list2: List[Int] = 6 +: list
    println(list2)

    val list3: List[Int] = 5::6::7::8::list
    println(list3)
    println(Nil)

    val list4: List[Int] = 1::2::3::4::Nil
    println(list4)

    val list5: List[Any] = 5::6::7::8::list::Nil
    println(list5)

    val list6: List[Any] = 5::6::7::8::list:::Nil
    println(list6)

    println("----------------------------------------")
    //2. 可变list

    //val listBuffer = new ListBuffer[Int]()

    val listBuffer: ListBuffer[Int] = ListBuffer(1,2,3,4,5)

    println(listBuffer)

    //操作
    listBuffer.append(6,7,8)
    println(listBuffer)

    listBuffer.insert(0,10,11,12)
    println(listBuffer)

    listBuffer.remove(0)
    println(listBuffer)

    listBuffer.remove(0,2)
    println(listBuffer)

    listBuffer.update(0,0)
    println(listBuffer)

    //3. 可变  不可变 转换

    val list7 = List(1,2,3)

    val listBuffer1: ListBuffer[Int] = ListBuffer(5,6,7)

    // 可变 -> 不可变

    val BuuToLii: List[Int] = listBuffer1.toList

    // 不可变 -> 可变
    val LiiToBuu: mutable.Buffer[Int] = list7.toBuffer

  }
}
