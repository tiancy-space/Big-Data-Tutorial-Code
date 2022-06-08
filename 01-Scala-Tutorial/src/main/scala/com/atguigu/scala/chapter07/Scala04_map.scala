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
    println(map) // Map(a -> 11, b -> 2, c -> 3)

    // 获取元素
    val value: Int = map.apply("a") // 11
    println(value)

    // 通过get(key)的形式获取当前 map中的value,并将当前返回的结果通过 Option类型包装[Some、None]
    val option: Option[Int] = map.get("a") // 11
    // 下面的这个判断, Option里面也是这样写的. option.isEmpty None(). 如果不为空,则返回 Some(value)
    if(option.isEmpty){
      println("没有")
    }else{
      val value: Int = option.get
      println(value)
    }

    // 查找map中的key的另一种写法,如果当前查找的key不存在,则使用指定的默认值替换.
    val any: Any = option.getOrElse( "不存在" )
    println(any) // 11

    val result: Any = map.getOrElse("e","找不到")
    println(result) // 找不到



    // 可变
    val mmap: mutable.Map[String, Int] = mutable.Map("a"->1 ,"b"->2, "c"->3)
    println(mmap) // Map(b -> 2, a -> 1, c -> 3)

    //操作

    mmap.put("d",4) // Map(b -> 2, d -> 4, a -> 1, c -> 3)
    println(mmap)

    mmap.update("f",111)  //如果key存在，则修改，key不存在，则添加
    println(mmap) // Map(b -> 2, d -> 4, a -> 1, c -> 3, f -> 111)

    mmap.remove("h")
    println(mmap) // Map(b -> 2, d -> 4, a -> 1, c -> 3, f -> 111)

    // 转换
    // map 转换成别的集合,将map中的每一项,转化成一个个元组.
    // k->v  =>  (k,v)  => 元组
    val array: Array[(String, Int)] = mmap.toArray
    println(array.mkString(" , "))  //(b,2) , (d,4) , (a,1) , (c,3) , (f,111)

    val list: List[(String, Int)] = mmap.toList
    println(list)  //List((b,2), (d,4), (a,1), (c,3), (f,111))

    val set: Set[(String, Int)] = mmap.toSet
    println(set)  //Set((c,3), (b,2), (d,4), (f,111), (a,1)) // Set((c,3), (b,2), (d,4), (f,111), (a,1))

    // map的迭代
    val keySet: collection.Set[String] = mmap.keySet
    val keys: Iterable[String] = mmap.keys
    val keysIterator: Iterator[String] = mmap.keysIterator

    val values: Iterable[Int] = mmap.values
    val valuesIterator: Iterator[Int] = mmap.valuesIterator

  }
}
