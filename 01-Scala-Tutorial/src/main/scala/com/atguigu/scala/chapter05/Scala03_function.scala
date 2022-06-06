package com.atguigu.scala.chapter05

/**
  * Scala  - 函数式编程 - 函数参数
  */
object Scala03_function {
  def main(args: Array[String]): Unit = {
    // 1.可变参数
    // 可变参数只能写一个， 且要写到参数列表的最后面
    // Java  ： String ... args
    // Scala :  args : String*
    def fun1(names: String*  ):Unit = {
      println("names = " + names)
    }

    fun1()
    fun1("zhangsan")
    fun1("zhangsan","lisi")

    def fun2(age : Int, names: String*  ):Unit = {
      println("names = " + names)
    }

    //2. 参数默认值

    def fun3(name : String, password :String = "000000" ) : Unit = {
      println( s"name = $name , password = $password")
    }

    fun3("wangwu")

    fun3("wangwu","123456")

    //3. 带名参数
    def fun4(name : String, password :String = "000000" , address : String  ) : Unit = {
      println( s"name = $name , password = $password , address = $address")
    }

    fun4("zhaoliu","111111","beijing")

    fun4("zhaoliu", address = "shanghai")


  }
}
