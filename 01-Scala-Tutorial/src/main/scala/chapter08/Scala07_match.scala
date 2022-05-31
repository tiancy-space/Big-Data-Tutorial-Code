package chapter08

/**
  * Scala - 模式匹配 - 偏函数
  */
object Scala07_match {
  def main(args: Array[String]): Unit = {
    //将该List(1,2,3,4,5,6,"test")中的Int类型的元素加一
    val list = List(1,2,3,4,5,6,"test")
    //常规处理:
    println(list.map(ele => {
      if (ele.isInstanceOf[Int]) {
        ele.asInstanceOf[Int] + 1
      } else {
        ele
      }
    }))

    //模式匹配
    println(list.map{
      case ele: Int => ele + 1
      case other => other
    })

    //需求: 将该List(1,2,3,4,5,6,"test")中的Int类型的元素加一 , 去掉其他类型的元素
    val list1 = List(1,2,3,4,5,6,"test" ,false )

    // 定义偏函数
    val pf  : PartialFunction[Any/*进去的类型*/, Int /*出来的类型*/] = {
      case i:Int => i + 1
     // case other => other
    }

   // println(list1.map(pf))
    println(list1.collect(pf))

    // 模式匹配
    println( list1.collect{
      case i: Int => i + 1
    })

  }
}
