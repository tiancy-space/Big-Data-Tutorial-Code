package com.atguigu.scala.chapter07

/**
  * Scala - 集合 - 元组
  */
object Scala05_tuple {
  def main(args: Array[String]): Unit = {
    // zhangsan  20  10000.1  false

    val tuple: (String, Int, Double, Boolean) = ("zhangsan",20,10000.1,false)

    println(tuple)

    val name: Any = tuple.productElement(0)
    println(name)
    val nameStr: String = name.asInstanceOf[String]


    val username: String = tuple._1
    val age: Int = tuple._2
    val salary: Double = tuple._3
    val boo: Boolean = tuple._4

    println(s"username = $username ,age = $age , salary = $salary , boo = $boo")


    // map中的 k -> v  => ( k,v )
    // map中的 kv 实际上就是一个两个元素的元组. 称之为对偶元组
    val map: Map[String, Int] = Map("a"->1,"b"->2, ("c",3))
    println(map)


    //函数的参数个数:
    def testParam(i1: Int ,i2 : Int ,i3:Int,i4:Int,i5:Int,i6:Int,i7:Int,i8:Int,i9:Int,i10:Int,
                  i11: Int ,i12 : Int ,i13:Int,i14:Int,i15:Int,i16:Int,i17:Int,i18:Int,i19:Int,i20:Int,
                  i21:Int,i22:Int): Unit ={
      println("testParam....")
    }

    testParam(0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1)


    //将函数作为值

    var f = testParam _



  }
}
