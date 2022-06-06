package com.atguigu.scala.chapter07

/**
  * Scala - 集合 - 常用（通用）方法
  */
object Scala07_method {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5)

    //1. 集合长度
    println(list.length)

    println(list.size)

    //2. 集合是否为空

    println(list.isEmpty)

    //3. 集合迭代器
    val iterator: Iterator[Int] = list.iterator
    while(iterator.hasNext){
      val ele: Int = iterator.next()
      println(ele)
    }

    //4. 循环遍历集合
    def myprint(ele : Int ): Unit ={
        print(ele + " ")
    }

    list.foreach(myprint)
    println()

    // 匿名函数
    list.foreach(  ele => print( ele  + " ")  )

    println()
    list.foreach(  println )

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
    def findEle(ele : Int ) : Boolean  = {
       ele > 10
    }
    println(list.find(findEle).getOrElse("不存在"))

    println(list.find(  _ % 2 == 0   ))

    //16. 丢弃前几个元素

    println(list.drop(2))

    //17.丢弃后几个元素
    println(list.dropRight(2))

    //18.反转集合
    println(list.reverse)

    //19.集合去重
    val list1 = List(1,1,2,2,3,4,5,5,6,6)

    println(list1.distinct)



  }
}
