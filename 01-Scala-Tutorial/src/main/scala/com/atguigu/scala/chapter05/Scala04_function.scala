package com.atguigu.scala.chapter05

/**
 * Scala - 函数式编程 - 函数至简原则(能省则省)
 */
object Scala04_function {
  def main(args: Array[String]): Unit = {
    //1. 完整的函数
    def fun1(): String = {
      return "abc"
    }

    println(fun1())

    //2. return关键字可以省略不写.
    def fun2(): String = {
      "abc"
    }

    println(fun2())

    //3.如果函数体只有一行代码， {}可以省略
    def fun3(): String = "abc"

    println(fun3())

    //4. 函数的返回值类型可以省略
    def fun4() = "abc"

    println(fun4())

    //5.如果函数没有参数, ()可以省略
    // 调用函数时: 如果函数定义的时候没有写（）,调用的时候不能写()
    //            如果函数定义的时候写了()，调用的时候，如果函数没有参数()可以省略不写。
    def fun5 = "abc"

    println(fun5)

    def fun55(name: String) = "abc"

    println(fun55("aaa"))
    //println( fun55 )

    //6. 如果只关心函数的功能， 不关心函数的名字， def 和函数名可以省略
    //   此函数叫匿名函数， 语法: (参数列表) => { 函数体 }
    // 下面的写法: 将匿名函数本身赋值给一个 变量,变量的类型就是一个函数类型.
    var f: () => String = () => "def"
    println(f()) // def


    //7. return 关键字 和 返回值的问题:
    // 7.1 如果明确指定了return关键字, 返回值类型不能省
    def fun71(): String = {
      return "abc"
    }

    //7.2 如果返回值类型指定成Unit， 就算明确通过return返回结果,也不管用
    def fun72(): Unit = {
      return "abc"
    }

    println(fun72)  // ()

    //7.3 如果明确返回值类型就是Unit,函数体中又使用了return，但是想省略Unit , 需要将=一并省略.
    //    此种函数叫过程函数.

    def fun73() {
      return "abc"
    }
  }
}
