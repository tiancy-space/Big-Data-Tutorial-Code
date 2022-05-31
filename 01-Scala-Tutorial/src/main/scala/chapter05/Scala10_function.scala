package chapter05

/**
  * Scala - 函数式编程 - 闭包
  */
object Scala10_function {

  def main(args: Array[String]): Unit = {

    // 内部函数使用了外部函数的局部变量,将此种现象称之为闭包.
    // 其实 函数作为值赋值给变量 、匿名函数 等都存在闭包现象

    // Scala2.12  ： 将外部函数的局部变量作为内部函的参数传递给了内部函数

    // Scala2.11 :  将外部函数的局部变量赋值给一个全局的变量来保存.

    def outFun(i : Int ) : Int => Int = {

      def innerFun(j : Int ) : Int = {
        i + j
      }

      innerFun
    }

    println(outFun(10)(20))

  }
}
