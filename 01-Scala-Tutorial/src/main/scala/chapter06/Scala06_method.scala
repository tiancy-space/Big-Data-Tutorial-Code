package chapter06

/**
  * Scala - 面向对象编程  - 方法
  */
object Scala06_method {

  def main(args: Array[String]): Unit = {
    // 通过对象调用方法

    val user06 = new User06()
    user06.testMethod()

    var b : Byte  = 10
    user06.testMethod(b)

    val bobject : A  = new B

    user06.testMethod(bobject)

  }
}

class User06{

  //语法:  修饰符  def  方法名(参数列表) : 返回值类型 = { 方法体 }

  def testMethod( ) : Unit = {
    println("testMethod....")
  }

  // 方法的重载
  /*def testMethod(b : Byte ): Unit ={
    println("Byte....")
  }*/
 /* def testMethod(s : Short ): Unit ={
    println("Short....")
  }*/
  def testMethod(c : Char ): Unit ={
    println("Char....")
  }
  def testMethod(i : Int ): Unit ={
    println("Int....")
  }

  def testMethod(b : B ): Unit = {
    println("B.....")
  }

  def testMethod(a : A ): Unit = {
    println("A.....")
  }
}

class A {}


class B  extends A {}
