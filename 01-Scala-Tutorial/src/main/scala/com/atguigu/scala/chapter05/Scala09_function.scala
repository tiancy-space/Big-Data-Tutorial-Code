package com.atguigu.scala.chapter05

/*
  TODO Scala - 函数式编程 - 函数作为值 - 匿名函数 - 至简原则
    1、匿名函数的声明: (参数列表) => {函数体}, eg:  (name:String,num:Int) => {name*num}
    2、声明函数的参数列表类型为函数类型或者当前函数的返回值为函数类型. 函数类型的声明方式: (参数列表中每个参数的数据类型) => 当前函数的返回值类型

 */
object Scala09_function {
  def main(args: Array[String]): Unit = {

    // 定义一个函数,函数的参数列表为一个函数类型,返回值为 Unit
    def fun ( f : String => String ) : Unit ={
      // 函数内部,拿到当前fun函数的参数列表,是一个函数,并传入指定类型的参数.
      println("匿名函数的使用一、" +  f("zhangsan") )
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
