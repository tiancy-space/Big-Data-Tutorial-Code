package com.atguigu.scala.chapter07

import scala.collection.mutable

/**
  * Scala - 集合 - Set（无序不重复）
  */
object Scala03_Set {
  def main(args: Array[String]): Unit = {
    //1. 不可变
    val set: Set[Int] = Set(1,2,3,4,5,6,5,6)
    println(set)

    println(set.getClass.getName)


    //2. 可变
    val mset: mutable.Set[Int] = mutable.Set(1,2,3,4,5,6,5,6,7)
    println(mset)

    //操作
    mset.add(8)
    println(mset)

    mset.update(9,true)  // add
    println(mset)

    mset.update(10,false )  //不操作
    println(mset)

    mset.update(1,true)  // 不操作
    println(mset)

    mset.update(1,false)  //remove
    println(mset)

    mset.remove(2)
    println(mset)

    mset.remove(20)


    // 转换

    val set1: Set[Int] = Set(1,2,3,4,5,6,7)

    val mset1: mutable.Set[Int] = mutable.Set(1,2,3,4,5,6,7)

    //可变 -> 不可变
    val set2: Set[Int] = mset1.toSet

    //不可变  -> 可变
    val buffer: mutable.Buffer[Int] = set1.toBuffer
  }
}
