package com.atguigu.scala.chapter05

/**
  * Scala - 函数式编程 - 递归
  */
object Scala13_function {
  def main(args: Array[String]): Unit = {
    // 递归: 自己调用自己。
    // 递归注意的问题:  递归一定要有退出条件  、 递归深度

    // 需求: 完成数字n的累加和 , 当n为1时， 累加和就是1
    //       例如: 数字5的累加和:  5 + 4 + 3 + 2 + 1

    def sum(num: Int ) : Int  = {
      if(num == 1) {
        1
      }else{
        num + sum(num-1)
      }
    }

    //println(sum(50000))


    // 尾递归:  每次递归调用，都会得到本次递归的结果。(每次的递归调用，结束后都会弹栈)
    // num    : 数字n
    // result ： 每次递归的临时结果

    // sum1(5,1)                  => sum1(4, 5 + 1 )
    // sum1(4, 5+1)               => sum1(3,5 + 4 + 1)
    //sum1(3,5 + 4 + 1)           => sum1(2,5 + 4 + 3 +  1)
    //sum1(2,5 + 4 + 3 +  1)      => sum1(1,5 + 4 + 3 + 2 + 1 )
    //sum1(1,5 + 4 + 3 + 2 + 1 )  =>  5 + 4 + 3 + 2 + 1

    def sum1(num: Int , result : Int ) :Int = {
      if(num == 1) {
        result
      }else{
        sum1(num -1 , num + result )
      }
    }


    println(sum1(500000,1))

  }
}
