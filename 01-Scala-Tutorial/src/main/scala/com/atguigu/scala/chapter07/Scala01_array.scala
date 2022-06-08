package com.atguigu.scala.chapter07

import java.util

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/*
  TODO Scala  - 集合 - array
    在scala中数组分为不可变长数组（在immutable包下）和可变长数组（在mutable包下）
    不可变长数组 : 指的是长度不可变，但是数组中角标对应的元素的值是可变的. 也就是在创建数组时,指定了数组的长度.
    可变数组指的是长度和数组中角标对应的元素都是可变的。
 */
object Scala01_array {

  def main(args: Array[String]): Unit = {
    /*
      1. 不可变数组 Array
      创建数组对象
      val array = new Array[Int](5)
      int[] var2 = new int[5];
     */
    // 直接通过apply方法,指定了数组中存放的内容,也就间接制定了数组的长度.
    val array: Array[Int] = Array(1,2,3,4,5)

    // 查看数组内容,直接查看显示的就是它的地址. 需要经过遍历数组,才能获取它的下标对饮的内容.
    println(array)  //[I@47f37ef1

    // 遍历方式一:直接通过for循环,把数组中的每个值,给到 变量i
    for(i <- array){
      print( i + " ") // 1 2 3 4 5
    }
    println()

    // 遍历的方式二: 直接通过 Arrays.toString
    println(util.Arrays.toString(array)) // [1, 2, 3, 4, 5]
    // 遍历方式三: 通过指定的分隔符,将当前不可变长的数组内容分割.
    println(array.mkString(",")) // 1,2,3,4,5

    // 操作 : 更新当前数组中0位置的值.
    array.update(0,0)
    println(array.mkString(",")) // 0,2,3,4,5

    array(0) = -1
    println(array.mkString(",")) // -1,2,3,4,5

    println("-----------------------------------")

    //2. 可变数组 : 数组的值或者长度一直在变的,反复都是在操作这个数组对象.
    //val buffer = new ArrayBuffer[Int]()
    val buffer: ArrayBuffer[Int] = ArrayBuffer[Int](5,6,7,8,9)
    println(buffer) // ArrayBuffer(5, 6, 7, 8, 9)

    //操作 insert: 两个参数: 参数1 指定数组位置,第二个参数: 插入的元素内容.
    buffer.insert(1,1,2,3)
    println(buffer) // ArrayBuffer(5, 1, 2, 3, 6, 7, 8, 9)

    buffer.update(0,0)
    println(buffer) //ArrayBuffer(0, 1, 2, 3, 6, 7, 8, 9)

    buffer.remove(0)
    println(buffer) // ArrayBuffer(1, 2, 3, 6, 7, 8, 9)
    // 指定下标位置,删除指定个数的元素.
    buffer.remove(1,2) // ArrayBuffer(1, 6, 7, 8, 9)

    println(buffer)


    //3. 可变和不可变数组的转换

    // 定义一个不可变数组
    val array1: Array[Int] = Array(1,2,3)
    // 定义一个可变长的数组
    val buffer1: mutable.Seq[Int] = ArrayBuffer(7,8,9)

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
    val array2: Array[Int] = Array(1,2,3)
    val array3: Array[Int] = Array(4,5,6)
    val arr6: Array[Int] = Array.concat(array2  , array3)
    println(arr6.mkString(",")) //  1,2,3,4,5,6

    // 创建指定范围的数组
    val arr7: Array[Int] = Array.range(0,2)
    println(arr7.mkString(",")) // 0,1

    // 创建并填充指定数量的数组
    val arr8:Array[Int] = Array.fill[Int](5)(-1)

    println(arr8.mkString(",")) // -1,-1,-1,-1,-1



  }
}
