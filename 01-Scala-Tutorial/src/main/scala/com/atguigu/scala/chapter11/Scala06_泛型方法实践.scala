package com.atguigu.scala.chapter11

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/10
 */

object Scala06_泛型方法实践 {
  def main(args: Array[String]): Unit = {
    // 调用泛型方法使用的实际参数,进行类型推断
    val value: Any = getMiddle(Array("Hello", 1, "ss", 5.0D, true,11,'c')) match {
      case value: Int => value.toInt
      case value: String => value.toString
      case value: Double => value.toDouble
      case value: Boolean => value
    }
    println(value + "  " +  value.getClass.getSimpleName)
  }

  // 定义一个泛型方法,获取集合中的中间元素.这样做的好处就是,我们在方法调用时,可以灵活传入参数类型
  def getMiddle[T](a: Array[T]) = a(a.length / 2)
}
