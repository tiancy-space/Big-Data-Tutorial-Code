package com.atguigu.scala.chapter05

/*
  TODO Scala - 函数式编程 - 闭包
    闭包现象: 函数内还有函数,内部函数中使用到了外层函数的变量.
    闭包最本质是改变了`外部变量`的生命周期,在Scala中语法不会报错,在Spark中会存在闭包检查.
 */
object Scala10_function {

  def main(args: Array[String]): Unit = {

    // 内部函数使用了外部函数的局部变量,将此种现象称之为闭包.
    // 其实 函数作为值赋值给变量 、匿名函数 等都存在闭包现象

    // Scala2.12  ： 将外部函数的局部变量作为内部函的参数传递给了内部函数

    // Scala2.11 :  将外部函数的局部变量赋值给一个全局的变量来保存.

    def outFun(i: Int): Int => Int = {

      def innerFun(j: Int): Int = {
        i + j
      }

      innerFun
    }

    println(outFun(10)(20))

    /*
      定义一个函数fun5,当前函数的返回值类型为一个函数类型[无参数,返回一个Int]
     */
    def fun5(): () => Int = {
      // 当前函数fun5的一个变量.
      val i = 20

      // 在内部定义一个函数作为fun5的最终返回
      def fun55(): Int = {
        // 内部函数中,使用到了外部函数的变量 i
        i * 2
      }
      // 将最终的fun55函数作为函数fun5的返回值.
      fun55 _
    }
    println(fun5()())
  }
}
