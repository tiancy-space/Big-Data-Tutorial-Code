package scala_tutorial

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/2
 */
object ScalaSummarize_01 {

  /*
      Scala中的main方法说明:这里和Java的main方法比较
      public static void main(String[] args){
      }
      使用关键字def声明方法, main是方法名,并且scala中,有自己的类型推断,因此名称的优先级要高于类型.甚至可以不写,在程序运行时交给编译器来进行类型推断.
      Unit : 表示 main( ) 的返回值类型,类似于Java中的void,但是也有区别,因为Unit本身也是一个对象
      并且在Scala中,有一个现象:真正的做到了完全面向对象,没有Java中的基本数据类型这种数据类型,
   */
  def main(args: Array[String]): Unit = {

    var userName = "tiancy"
    var age  = 23
    println(s"$userName,$age")

  }
}
