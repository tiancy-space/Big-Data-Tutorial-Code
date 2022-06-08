package com.atguigu.scala.chapter07

/*
  TODO Scala - 集合 - 元组
    元组一般用来存储多个不同类型的值。例如同时存储姓名，年龄，性别，出生年月这些数据, 就要用到元组来存储,并且元组的长度和元素都是不可变的

 */
object Scala05_tuple {
  def main(args: Array[String]): Unit = {
    // zhangsan  20  10000.1  false
    // 元组的定义,使用() .
    val tuple: (String, Int, Double, Boolean) = ("zhangsan", 20, 10000.1, false)

    println(tuple) // (zhangsan,20,10000.1,false)

    val name: Any = tuple.productElement(0) // zhangsan
    println(name)

    // 将Any类型的name变量转化为String类型. 这里是一个类型转化.
    val nameStr: String = name.asInstanceOf[String]

    val username: String = tuple._1
    val age: Int = tuple._2
    val salary: Double = tuple._3
    val boo: Boolean = tuple._4

    println(s"username = $username ,age = $age , salary = $salary , boo = $boo") // username = zhangsan ,age = 20 , salary = 10000.1 , boo = false


    // map中的 k -> v  => ( k,v )
    // map中的 kv 实际上就是一个两个元素的元组. 称之为对偶元组.
    val map: Map[String, Int] = Map("a" -> 1, "b" -> 2, ("c", 3))
    println(map) // Map(a -> 1, b -> 2, c -> 3)


    //函数的参数个数:
    def testParam(i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int,
                  i11: Int, i12: Int, i13: Int, i14: Int, i15: Int, i16: Int, i17: Int, i18: Int, i19: Int, i20: Int,
                  i21: Int, i22: Int): Unit = {
      println("testParam....")
    }

    testParam(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1)


    //将函数作为值

    var f = testParam _
  }
}
