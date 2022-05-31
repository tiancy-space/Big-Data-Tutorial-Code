package chapter05

/**
  * Scala - 函数式编程 - 函数作为值 - 赋值给变量
  */
object Scala05_function {

  def main(args: Array[String]): Unit = {

    //1. 定义函数
    def fun(name : String ) : String  = {
      return name.toUpperCase
    }

    //2. 将函数赋值给变量
    var fresult  = fun("zhangsan")  // 将函数的调用结果赋值给变量

    // 函数类型:  (参数类型) => 返回值类型
    var f : String=>String  = fun _  //通过_指明将函数本身赋值给变量
    println(f("lisi"))

    var ff : String => String  = fun

    println(ff("wangwu"))
  }

}
