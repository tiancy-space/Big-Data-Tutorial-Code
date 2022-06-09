package com.atguigu.scala.chapter07

/*
  TODO 集合的常用方法: 集合本身的方法 交、并、差集. 集合切分、集合滑动、集合滚动. 集合拉链、索引拉链.
    集合元素中的最值、计算. 集合规约、集合折叠、集合扫描.
    集合功能函数:
      map
      flatmap
      filter
      排序相关
      分组相关
 */
object Scala08_method {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5)

    val list1 = List(3,4,5,6,7,8)

    //20. 集合并集
    println(list.union(list1)) // List(1, 2, 3, 4, 5, 3, 4, 5, 6, 7, 8)

    //21. 集合交集
    println(list.intersect(list1)) // List(3, 4, 5)

    //22. 集合差集
    println(list.diff(list1)) // List(1, 2)
    println(list1.diff(list)) // List(6, 7, 8)

    //23. 切分集合, 指定待切分的位置,将一个集合拆分成两个集合. eg : list.splitAt(1) : 将集合list从 1的位置开启切分, 1左面的元素作为一个集合. [0-1), [1,结尾]
    println(list.splitAt(2)) // (List(1, 2),List(3, 4, 5))
    println(list.splitAt(1)) // (List(1),List(2, 3, 4, 5))

    /*
      24. 集合滑动,要充分理解滑动的感觉: 滑动的产物 : 一种是滑动窗口,一种是滚动窗口.
          滑动窗口: 每次滑动的步长是一样的,都是1. 滚动窗口可能会跳过几个值. 如果指定滚动窗口的步长为1.就会变成滑动窗口
          list.sliding(3) : 通过在固定大小的块中传递一个“滑动窗口”对元素进行分组（而不是像在分组中那样对它们进行分区）。“滑动窗口”步骤设置为 1。 // List(1, 2, 3),List(2, 3, 4),List(3, 4, 5)
          list.sliding(3,2) : 通过在固定大小的块中传递“滑动窗口”对元素进行分组（而不是对它们进行分区，就像在分组中所做的那样。） // List(1, 2, 3),List(3, 4, 5) .
     */
    println(list.sliding(3).mkString(","))
    //25. 集合滚动 1,2,3,4,5
    println(list.sliding(3, 2).mkString(","))

    //26. 集合拉链 : 相同位置的元素放到同一个元组中.如果集合中的长度不一致,则忽略掉长出部分.
    println(list.zip(list1)) // List((1,3), (2,4), (3,5), (4,6), (5,7))

    //27. 集合索引拉链 : 当前调用者,也就是集合本身的元素和元素所在位置,进行拉链. 最终形成多个元组.
    println(list.zipWithIndex) // List((1,0), (2,1), (3,2), (4,3), (5,4))

    //28. 集合最小值
    println(list.min) // 1

    //29. 集合最大值
    println(list.max) // 5

    //30. 集合求和
    println(list.sum) // 15

    //31. 集合乘积
    println(list.product) // 120

    /*
      32. 集合规约,结果等价于调用sum
           所谓的规约: reduce方法,底层默认调用的是 reduceLeft ,是在当前集合内指定运算操作 . 所谓规约,就是从集合指定位置开始,依次将两个元素将加得到结果,再与后一个相加.
     */
    println(list.reduce(_ + _))  //15
    println(list.sum)  //15
    // 1,2,3,4,5 : 1-2 = -1 , -1 - 3 = -4,-4 - 4 = -8, -8 - 5 = -13
    println(list.reduce(_ - _))  // -13

    //33. 集合左规约
    println(list.reduceLeft(_ + _))  //15
    println(list.reduceLeft(_ - _))  // -13

    //34. 集合右规约
    println(list.reduceRight(_ + _)) // 15
    println(list.reduceRight(_ - _)) // 3

    /*
      35. 集合折叠: 这个方法,用到了scala中的特殊语法: 柯里化,就是指定了多个参数列表,第一个参数列表指定了当前集合中的位置,最终获取它的值.
      比如: list.fold(6)(_ + _) : (6)就是获取当前集合中元素6,可以理解为一个集合,存在单个元素:6 ,后面再指定规约操作,也就是集合内的操作.  == > 折叠主要讲的就是: 集合内元素和集合规约的执行结果.
      1,2,3,4,5

     */
    println("fold: " + list.fold(6)(_ + _)) //  21

    //36. 集合左折叠
    println(list.foldLeft(6)(_ + _)) //21

    //37. 集合右折叠
    println(list.foldRight(6)(_ + _)) // 21

    // 集合内规约结果 - 6
    println(list.foldRight(6)(_ - _)) // -3

    //38. 集合扫描
    // 1,2,3,4,5 -> 6,1,2,3,4,5 --> 再进行左规约.
    val list2: List[Int] = list.scan(6)(_ + _)  // List(6, 7, 9, 12, 16, 21)
    println(list2)

    //39. 集合左扫描
    println(list.scanLeft(6)(_ + _)) //List(6, 7, 9, 12, 16, 21)

    //40.集合右扫描
    println(list.scanRight(6)(_ + _)) //List(6, 7, 9, 12, 16, 21)


  }
}
