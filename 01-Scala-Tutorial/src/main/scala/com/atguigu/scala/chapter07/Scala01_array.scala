package com.atguigu.scala.chapter07

import java.util

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Scala  - 集合 - array
  */
object Scala01_array {

  def main(args: Array[String]): Unit = {
    //1. 不可变数组 Array
    //创建数组对象
    //val array = new Array[Int](5)   //int[] var2 = new int[5];
    val array = Array(1,2,3,4,5)

    // 查看数组内容
    println(array)  //[I@47f37ef1

    for(i <- array){
      print( i + " ")
    }
    println()

    println(util.Arrays.toString(array))

    println(array.mkString(","))

    // 操作
    array.update(0,0)
    println(array.mkString(","))

    array(0) = -1
    println(array.mkString(","))

    println("-----------------------------------")

    //2. 可变数组
    //val buffer = new ArrayBuffer[Int]()
    val buffer: ArrayBuffer[Int] = ArrayBuffer[Int](5,6,7,8,9)
    println(buffer)

    //操作
    buffer.insert(1,1,2,3)
    println(buffer)

    buffer.update(0,0)
    println(buffer)

    buffer.remove(0)
    println(buffer)

    buffer.remove(1,2)

    println(buffer)


    //3. 可变和不可变数组的转换

    val array1 = Array(1,2,3)

    val buffer1 = ArrayBuffer(7,8,9)

    // 可变-> 不可变
    val buuToArr: Array[Int] = buffer1.toArray

    // 不可变 -> 可变
    val arrToBuu: mutable.Buffer[Int] = array1.toBuffer



    //4. 其他数组
    // 多维数组
    var myMatrix = Array.ofDim[Int](3,3)
    //myMatrix.foreach(list=>list.foreach(println))

    for(i <- myMatrix){
      println(i.mkString(","))
    }

    // 合并数组
    val array2 = Array(1,2,3)
    val array3 = Array(4,5,6)
    val arr6: Array[Int] = Array.concat(array2  , array3)
    println(arr6.mkString(","))

    // 创建指定范围的数组
    val arr7: Array[Int] = Array.range(0,2)
    println(arr7.mkString(","))

    // 创建并填充指定数量的数组
    val arr8:Array[Int] = Array.fill[Int](5)(-1)

    println(arr8.mkString(","))



  }
}
