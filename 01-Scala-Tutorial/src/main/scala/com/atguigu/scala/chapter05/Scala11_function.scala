package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 柯里化
  */
object Scala11_function {

  def main(args: Array[String]): Unit = {
    // 所谓的函数柯里化其实就是函数支持多个参数列表.

    def fun1(name : String, age :Int , password :String ): Unit = {
      println(s"name = $name , age = $age , password = $password")
    }

    fun1("zhangsan",30,"123")


    def fun2(name :String ) (age :Int , password:String  ) : Unit = {

      println(s"name = $name , age = $age , password = $password")
    }

    fun2("lisi")(40,"123456")


//    val list = List(5,3,6,2,9)
//
//    println(list.sortBy(num => num))


  }

}
