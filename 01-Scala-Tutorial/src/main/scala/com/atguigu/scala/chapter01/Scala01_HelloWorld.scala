package com.atguigu.scala.chapter01

/**
  * 1. Java是面向对象的语言. Scala是完全面向对象的语言.
  *
  * 2. object : 因为Scala是完全面向对象的语言，因此Java中的很多非面向对象的语法在Scala中不存在.
  *             例如: static语法 、 基本数据类型  等.
  *             Scala通过object来模拟Java的static语法
  *
  * 3. def ： define的缩写。 表示定义，声明.
  *
  * 4. 方法的形参:
  *       Scala :   args: Array[String] => 参数名 : 参数类型   =>  我喜欢上了小花,她是女人
  *       Java  :   String[] args       => 参数类型  参数名    =>  我喜欢上了一个女人,她叫小花
  *       Person person = new Person()
  *       person.xxxx
  *       person.xxxx

  * 5. [] :
  *      Java  :  []  => 数组
  *      Scala :  []  => 泛型
  *
  * 6. 无返回值类型:
  *       Java  :  void
  *       Scala :  Unit   =>  ()
  *
  * 7. 方法定义：
  *       Java  :  void   main()   => 返回值类型   方法名
  *       Scala :  main(): Unit    => 方法名  : 返回值类型
  *
  * 8. = ：赋值运算.  Scala中方法(函数)也是对象，可以被赋值.
  *
  * 9. 输出语句:
  *      Java  :  System.out.println("Hello Java")  => Java中会默认导入java.lang包下的内容
  *
  *      Scala :  println()  => Predef.println()  => Scala中会默认导入 PreDef对象 、scala包 、 java.lang包下的内容
  *
  * 10: 语句结束的分号:  在没有歧义的情况下， 分号可以省略不写.
  *
  *
  */
object Scala01_HelloWorld {
  //程序的入口 main方法
  def main(args: Array[String]): Unit = {
    // 输出语句
    System.out.println("Hello Java")

    /*Predef.*/ println("Hello Scala")

  }
}
