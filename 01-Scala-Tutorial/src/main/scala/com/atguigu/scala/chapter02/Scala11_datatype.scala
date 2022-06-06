package com.atguigu.scala.chapter02

import java.util
import java.util.LinkedList
import java.util.HashMap
import java.util.ArrayList
import java.util.List



/**
  * Scala - 数据类型
  */
object Scala11_datatype {
  def main(args: Array[String]): Unit = {
    // AnyVal
    var b : Byte = 10   // byte b = 10
    var s : Short = 10
    var i : Int = 10
    var c : Char = 'A'
    var boo : Boolean  = false

    // AnyRef
    val arrayList = new ArrayList[String]()
    var str : String = null


    // 类型转换  - 隐式转换 -  AnyVal
    var bb : Byte  = 10
    var ss : Short = bb  // 隐式转换  BoxesRunTime.boxToShort(bb)
    println(ss)
    // 类型转换  - 隐式转换 -  AnyRef
    val hashMap : AnyRef = new HashMap[String,String]


    // 类型转换  - 强制类型转换 -  AnyVal
    // 在Byte, Short ,Char, Int, Long, Float, Double中都提供了跟其他类型转换的方法.
    // 例如: toByte  toShort toInt toLong toFloat toDouble ....
    var ll : Long  = 100L
    var ii : Int = ll.toInt

    // 类型转换  - 强制类型转换 -  AnyRef
    // isInstanceOf[类型] :  判断是否为指定的类型
    // asInstanceOf[类型] ： 转换成指定的类型
    val list: List[String] = new LinkedList[String]

    if(list.isInstanceOf[LinkedList[String]]){
      val linkedList : LinkedList[String ] = list.asInstanceOf[LinkedList[String]]
    }


    // String类型转换:  Scala中任意类型都可以调用toString直接转换成String类型。
    var dd : Double = 5.0D


  }
}
