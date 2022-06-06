package com.atguigu.scala.chapter07

import scala.collection.mutable

/**
  * Scala - 集合 - map
  */
object Scala04_map {
  def main(args: Array[String]): Unit = {
    //1. 不可变
    // kv:  k->v
    val map: Map[String, Int] = Map("a"->1 ,"b"->2, "c"->3,"a"->11)
    println(map)

    // 获取元素
    val value: Int = map.apply("a")
    println(value)


    val option: Option[Int] = map.get("a")
    if(option.isEmpty){
      println("没有")
    }else{
      val value: Int = option.get
      println(value)
    }

    val any: Any = option.getOrElse( "不存在" )
    println(any)

    val result: Any = map.getOrElse("e","找不到")
    println(result)



    // 可变
    val mmap: mutable.Map[String, Int] = mutable.Map("a"->1 ,"b"->2, "c"->3)
    println(mmap)

    //操作

    mmap.put("d",4)
    println(mmap)

    mmap.update("f",111)  //如果key存在，则修改，key不存在，则添加
    println(mmap)

    mmap.remove("h")
    println(mmap)

    // 转换
    // map 转换成别的集合
    // k->v  =>  (k,v)  => 元组
    val array: Array[(String, Int)] = mmap.toArray
    println(array.mkString(" , "))  //(b,2) , (d,4) , (a,1) , (c,3) , (f,111)

    val list: List[(String, Int)] = mmap.toList
    println(list)  //List((b,2), (d,4), (a,1), (c,3), (f,111))

    val set: Set[(String, Int)] = mmap.toSet
    println(set)  //Set((c,3), (b,2), (d,4), (f,111), (a,1))

    // map的迭代
    val keySet: collection.Set[String] = mmap.keySet
    val keys: Iterable[String] = mmap.keys
    val keysIterator: Iterator[String] = mmap.keysIterator

    val values: Iterable[Int] = mmap.values
    val valuesIterator: Iterator[Int] = mmap.valuesIterator

  }
}
