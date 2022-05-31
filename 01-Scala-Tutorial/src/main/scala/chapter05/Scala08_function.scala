package chapter05

/**
  * Scala - 函数式编程 - 函数作为值 - 匿名函数
  */
object Scala08_function {

  def main(args: Array[String]): Unit = {

    // 函数作为参数
    def fun1( f : (String,Int) => String ) : Unit = {
      println(f("zhangsan", 2))
    }

    //单独定义函数作为参数使用
    def fparam(name :String, num : Int ) : String = {
      name * num
    }

    fun1(fparam)

    // 匿名函数的写法
    // 匿名函数的语法:  (参数列表) => { 函数体 }
    fun1(  (name :String, num : Int ) => { name * num }   )



    println("-------------------------------------")

    //函数作为返回值
    def fun2() : String => String = {

      def freturn( name : String ) : String = {
        name * 2
      }
      freturn
    }

    println(fun2()("lisi"))


    def fun3() : String => String = {

      (name : String ) => { name * 2 }
    }

    println(fun3()("wangwu"))


    println("----------------------------")

    // 函数赋值给变量



    def fvar(name :String ) : String = {
      name * 2
    }

    var f : String =>String  = fvar

    var ff : String => String = (name:String)=>{ name * 2 }



  }
}
