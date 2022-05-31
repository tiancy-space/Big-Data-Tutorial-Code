package chapter05

/**
  * Scala - 函数式编程 - 函数作为值 - 匿名函数 - 至简原则
  */
object Scala09_function {
  def main(args: Array[String]): Unit = {
    def fun ( f : String => String ) : Unit ={
      println( f("zhangsan") )
    }

    // 匿名函数
    fun( (name :String ) => { name.toUpperCase } )

    // 至简原则:
    // 1. 如果函数体只有一行语句,{}可以省略
    fun( (name :String ) =>  name.toUpperCase  )

    // 2. 函数的参数类型可以省略
    fun( (name) =>  name.toUpperCase  )

    // 3. 如果函数的参数只有一个,()可以省略
    fun( name =>  name.toUpperCase  )

    // 4. 如果函数的参数在函数体中只使用一次,参数名可以省略 , => 一并省略
    //    通过_来代替参数
    fun( _.toUpperCase  )

    println("-------------------------------------")

    def fun1( f: (String, Int) => String  ) : Unit = {
      println(f("lisi", 2))
    }
    //匿名函数
    fun1( (name:String, num : Int) => { name * num }  )

    //省略{}
    fun1( (name:String, num : Int) =>  name * num   )
    //省略参数类型
    fun1( (name , num ) =>  name * num   )
    //省略()

    //省略参数
    fun1(  _ * _   )

    //fun1( (name , num ) =>  (name + num) * num   )

  }
}
