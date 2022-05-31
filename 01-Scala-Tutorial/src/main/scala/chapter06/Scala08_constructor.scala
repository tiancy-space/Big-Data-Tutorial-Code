package chapter06

/**
  * Scala - 面向对象编程 - 构造器
  */
object Scala08_constructor {

  def main(args: Array[String]): Unit = {
    /*
      1. Scala 将构造器和类体做了整合.

      2. Scala 的构造器分两种 :
         主构造器

         辅助构造器 : 辅助构造器必须要直接或者间接调用到主构造器
                     被调用的辅助构造器必须要声明到调用者的前面.

      3. 主构造器中的形参通过var或者val声明后，可以直接作为对象的属性来使用.


     */


    val user081 = new User08   //
    val user082 = new User08()

    val user083= new User08("zhangsan")

    user083.name

  }
}


// 主构造器
class User08(var name : String , var age : Int  ){

  //var username : String = name

  //var userAge : Int = age


  println("User08.....")


  // 辅助构造器
  def this(){
    this("admin",30)
  }




  def this(name : String  ){
    this()
  }

}
