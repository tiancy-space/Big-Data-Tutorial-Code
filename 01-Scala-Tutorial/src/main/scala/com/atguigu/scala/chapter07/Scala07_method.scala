package com.atguigu.scala.chapter07

/**
 * Scala - 集合 - 常用（通用）方法
 */
object Scala07_method {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4, 5)

    //1. 集合长度
    println(list.length) // 5

    println(list.size) // 5

    //2. 集合是否为空

    println(list.isEmpty) // false

    //3. 获取当前集合的迭代器方法.
    val iterator: Iterator[Int] = list.iterator
    while (iterator.hasNext) {
      val ele: Int = iterator.next()
      println(ele)
    }

    /*
      TODO 集合中需要传入函数的方法总结以及窥探原理
        定义一个方法,方法名称 foreach(函数类型).
        - 疑问的点1: 没有指定返回值类型,也没有 = ,直接跟了方法体 {} . 这种写法是省略了返回值类型 : Unit = {return ... } 这样吗 ?
        - 疑问的点2: 当前方法在声明时使用泛型[U]定义,是一个泛型方法. 参数列表需要一个函数f, 传入一个 A类型,这个A类型是List类中定义的类型,返回一个U类型.
          这里可不可以直接理解为 函数在调用使用时,输入输出参数类型不一样就行呀,还是说两者存在关系. 比如: A是U的一部分. ==> 经过请教,没有特殊关系,如果存在关系:会通过语法强调.
        - foreach(f: 函数类型) 源码部分
        @inline final override def foreach[U](f: A => U) {
          // list.foreach(println(_). 先定义一个变量,拿到当前调用者对象,这里也就是拿到集合本身
          var these = this
          // 判断当前集合是否不为空,如果不为空,则调用函数 f(参数) ,当前函数f的参数为 集合的第一个元素.
          while (!these.isEmpty) {
            f(these.head)
            // 处理后,将当前集合的剩余元素组成的集合,再赋值给变量 these,形成递归调用.
            these = these.tail
          }
        }
     */
    //4. 循环遍历集合
    def myprint(ele: Int): Unit = {
      print(ele + " ")
    }

    list.foreach(myprint)
    println()

    // 匿名函数
    list.foreach(ele => print(ele + " "))

    println()
    list.foreach(println)

    //5. 集合的头
    println(list.head)

    //6. 集合的尾
    println(list.tail)

    //7. 集合最后一个元素
    println(list.last)

    //8. 集合除了最后一个元素
    println(list.init)

    //9. 集合尾迭代
    val tails: Iterator[List[Int]] = list.tails
    tails.foreach(println)

    //10.集合初始迭代

    val inits: Iterator[List[Int]] = list.inits
    inits.foreach(println)

    //11. 集合转换成字符串
    println(list.mkString(" , "))

    //12. 集合是否包含某个元素

    println(list.contains(5))

    //13. 取集合的前几个元素
    println(list.take(2))

    //14. 取集合的后几个元素
    println(list.takeRight(2))

    //15. 查找元素
    def findEle(ele: Int): Boolean = {
      ele > 10
    }

    println(list.find(findEle).getOrElse("不存在"))

    println(list.find(_ % 2 == 0))

    //16. 丢弃前几个元素

    println(list.drop(2))

    //17.丢弃后几个元素
    println(list.dropRight(2))

    //18.反转集合
    println(list.reverse)

    //19.集合去重
    val list1 = List(1, 1, 2, 2, 3, 4, 5, 5, 6, 6)

    println(list1.distinct)

  }
}
