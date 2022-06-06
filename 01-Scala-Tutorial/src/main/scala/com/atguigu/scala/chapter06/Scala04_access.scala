package com.atguigu.scala.chapter06

/**
  * Scala - 面向对象编程 - 访问权限
  */
object Scala04_access {
  def main(args: Array[String]): Unit = {
    /*
      Java的访问权限修饰符:
        private     :  本类
        [default]   :  本类   本包
        protected   :  本类   本包  子类
        public      :  任意


      Scala的访问权限修饰符:
        private     :   本类
        private[包] :   本类   包
        protected   :   本类  子类
        [default]   :   任意
     */

    val user04 = new User04()
    //user04.password
    user04.sex

    //user04.salary

    user04.name

  }
}


class User04{

  private var password : String = "123456"

  private[scala] var sex : String = "man"

  protected var salary : Double = 10000.1

  var name : String = "zhangsan"



  def test(): Unit ={
    println(password)  // private -> 本类  -> ok

    println(sex)  // private[包] -> 本类 -> ok

    println(salary)

    println(name)
  }

}

class SubUser04 extends User04{

  def testSub(): Unit ={

    println(salary)

    println(name)
  }
}