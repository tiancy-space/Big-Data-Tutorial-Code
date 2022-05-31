package chapter07

/**
  * Scala - 集合 - 常用方法
  */
object Scala08_method {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5)

    val list1 = List(3,4,5,6,7,8)

    //20. 集合并集
    println(list.union(list1))

    //21. 集合交集
    println(list.intersect(list1))

    //22. 集合差集
    println(list.diff(list1))
    println(list1.diff(list))

    //23. 切分集合
    println(list.splitAt(2))

    //24. 集合滑动
    println(list.sliding(3).mkString(","))
    //25. 集合滚动
    println(list.sliding(3, 2).mkString(","))

    //26. 集合拉链
    println(list.zip(list1))

    //27. 集合索引拉链
    println(list.zipWithIndex)

    //28. 集合最小值
    println(list.min)

    //29. 集合最大值
    println(list.max)

    //30. 集合求和
    println(list.sum)

    //31. 集合乘积
    println(list.product)

    //32. 集合规约
    println(list.reduce(_ + _))  //15

    println(list.reduce(_ - _))  // -13

    //33. 集合左规约
    println(list.reduceLeft(_ + _))  //15
    println(list.reduceLeft(_ - _))  // -13

    //34. 集合右规约
    println(list.reduceRight(_ + _)) // 15
    println(list.reduceRight(_ - _)) // 3

    //35. 集合折叠
    println(list.fold(6)(_ + _)) // 21

    //36. 集合左折叠
    println(list.foldLeft(6)(_ + _)) //21

    //37. 集合右折叠
    println(list.foldRight(6)(_ + _)) // 21

    println(list.foldRight(6)(_ - _)) // -3

    //38. 集合扫描
    val list2: List[Int] = list.scan(6)(_ + _)  // List(6, 7, 9, 12, 16, 21)
    println(list2)

    //39. 集合左扫描
    println(list.scanLeft(6)(_ + _))

    //40.集合右扫描
    println(list.scanRight(6)(_ + _))


  }
}
