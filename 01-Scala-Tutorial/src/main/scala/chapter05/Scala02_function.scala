package chapter05

/**
  * Scala - 函数式编程 - 函数定义
  */
object Scala02_function {
  def main(args: Array[String]): Unit = {
    //1. 无参数， 无返回值
    def fun1(): Unit ={
      println("fun1....")
    }
    fun1()

    //2. 无参数， 有返回值
    def fun2():String = {
      return "fun2...."
    }

    val result2: String = fun2()
    println(result2)

    //3. 有参数， 无返回值

    def fun3(name:String): Unit = {
      println("fun3..... " + name )
    }

    fun3("zhangsan")

    //4. 有参数， 有返回值

    def fun4(name:String):String = {

      return name * 2
    }

    println(fun4("zs"))

    //5. 多参数

    def fun5(name :String ,age :Int , address:String ): Unit = {
      println(s"name = $name  ,age = $age , address = $address")
    }

    fun5("lisi",30,"beijing")
  }
}
