package com.atguigu.scala.chapter04

/**
  * Scala - 分支控制 - if
  */
object Scala01_branch {
  def main(args: Array[String]): Unit = {
    var age : Int =  10

    //1. 单分支
    if(age >= 18){
      println("已成年")
    }

    //2. 双分支
    if(age >=18){
      println("已成年")
    }else{
      println("未成年")
    }

    //3. 多分支
    if(age <= 12){
      println("太海")

    }else if ( age <=18){
      println("木海")

    }else {
      println("大海")
    }


    //4. 表达式的返回值
    // 需求: 通过分支的判断，将结果赋值给一个变量

    //4.1  定义变量，在每个分支中给变量赋值
    var result = ""
    if(age <= 12 ){
       result = "童年"
    }else if(age <=18){
      result = "青年"
    }else {
      result = "壮年"
    }

    println(result)

    //4.2 通过函数的返回值来实现

    def getResult(age : Int ): String  = {
      if(age <= 12 ){
        return  "童年"
      }else if(age <=18){
        return "青年"
      }else {
        return "壮年"
      }
    }

    val result1: String = getResult(age)
    println(result1)


    //4.3 使用表达式的返回值
    //    表达式的返回值是将最后一行语句的执行结果返回
    var result2 =
    if(age <= 12 ){
       "童年"
        12
    }else if(age <=18){
       "青年"
    } else {
       "壮年"
    }

    println(result2)



    // 三元运算符:  Scala通过if  else 的简写形式 实现类似三元运算符的效果
    //  Java ： boolean表达式 ?  true的处理  ： false的处理

    var result3=
      if(age <=12){
      "少年"
    }else{
      "青年"
    }

    var result4 = if(age <=12 ) "少年" else "青年"

    println(result4)


  }
}
