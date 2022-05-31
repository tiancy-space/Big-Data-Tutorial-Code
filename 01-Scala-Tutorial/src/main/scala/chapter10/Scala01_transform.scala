package chapter10

/**
  * Scala - 隐式转换 - 隐式函数
  */
object Scala01_transform {

  def main(args: Array[String]): Unit = {
    //var b : Byte = 10
    //var i : Int = b  // BoxesRunTime.boxToInteger(b)
    //println(i)


    var d : Double = 2.0D
    //1. toInt
    var i : Int  = d.toInt

    //2. 自定义方法
    def transform(d: Double) : Int = {
      d.toInt
    }
    val ii : Int = transform(d)

    //3.隐式转换
    // 转换规则 - 隐式函数
    implicit def transform1(d:Double) : Int = {
      d.toInt
    }

    /*
    implicit  def transform2(d:Double): Int = {
      d.toInt
    }
   */

    implicit  def transform2(d:Double): Long = {
      d.toLong
    }

    var iii : Int  = d  // 编译报错(Double->Int) -> Scalac会查找隐式规则（Double->Int） -> implicit transform1

    println(iii)

  }


}
