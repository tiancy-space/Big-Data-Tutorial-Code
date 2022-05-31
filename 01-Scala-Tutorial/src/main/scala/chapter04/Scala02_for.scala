package chapter04

/**
  * Scala - 循环控制 - for
  */
object Scala02_for {
  def main(args: Array[String]): Unit = {
    /*
      语法:
          for( 循环变量  <- 数据集){  循环体 }
     */
    for(i <- "abc" ){
      println("i = " + i )
    }

    println("--------------------")

    for(i <- 1 to 5 ){   // [1,2,3,4,5]
      println("i = " + i )
    }

    println("---------------------")
    for(i <- 1 until 5){  //[1,2,3,4]
      println("i = " + i)
    }
    println("---------------------")

    for(i <- Range.inclusive(1,5)){
      println("i = " + i )
    }
    println("---------------------")
    for(i <- Range(1,5)){   // apply
      println("i = " + i )
    }


    println("--------------循环步长---------------")

    for(j <- 1 to 5 by 2 ){
      println("j = " + j )
    }

    println("---------------------")

    for(j <- 1 until 5 by 2 ){
      println("j = " + j )
    }

    println("---------------------")

    for(i <- Range.inclusive(1,5,2)){
      println("i = " + i )
    }
    println("---------------------")
    for(i <- Range(1,5,2)){   // apply
      println("i = " + i )
    }


    println("----------------------------")

    val list = List("hello","scala","spark",2,false)

    for(ele <- list){
      println(ele)
    }

  }
}
