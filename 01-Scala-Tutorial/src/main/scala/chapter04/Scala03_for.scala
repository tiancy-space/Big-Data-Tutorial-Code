package chapter04

/**
  * Scala - 循环控制 - for
  */
object Scala03_for {
  def main(args: Array[String]): Unit = {
    //1. 循环守卫
    // 需求: 从1循环到5, 当循环到3的时候，跳过本次循环
    for( i <- 1 to 5 ){
      //if(i == 3){
         //continue   Scala中没有continue关键字
      // }
      if(i != 3){
        println("i = " + i )
      }
    }

    println("--------------------------")
    for( i <- 1 to 5 if i != 3 ){
        println("i = " + i )
    }
    println("--------------------------")

    //2. 引入变量
    // 需求: i 从1 循环到 5, 将每次i+1的值赋值给j ,打印 i 和 j 的值
    for(i <- 1 to 5 ){
      var j = i + 1
      println(s"i = $i ,j = $j" )
    }

    println("--------------------------")

    for(i <- 1 to 5 ; j = i+1){
      println(s"i = $i ,j = $j" )
    }

    println("--------------------------")
    //3. 嵌套循环
    // 需求: 外层循环从1循环到5, 内层循环从1循环到3，每次打印循环的值
    for(i <- 1 to 5 ){
      // 外层循环体
      for(j <- 1 to 3 ){
        // 内层循环体
        println(s"i = $i , j = $j")
      }
    }
    println("--------------------------")

    for(i <- 1 to 5 ; j <- 1 to 3  ){

        println(s"i = $i , j = $j")  // 内层循环体
    }

    println("--------------------------")
    //4. 循环返回值
    // for循环默认没有返回值. 可以通过 yield 关键字来指定让循环返回结果。
    var result = for(i <- 1 to 5 ) yield {
      i + 1
    }
    println(result)


    // 字符串的乘法操作

    var str  = "a"

    println(str * 3)

  }
}
