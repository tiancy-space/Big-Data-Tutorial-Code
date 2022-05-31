package chapter06

/**
  * Scala - 面向对象编程 - import
  */
object Scala02_import {
  def main(args: Array[String]): Unit = {
    /*
       Java的import:
         1. import  java.util.ArrayList;
            import  java.util.* ;

         2. import static ....

       Scala的import:

         1. 导入包下所有的类
            import  java.util._ ;

         2. import可以在任意位置使用

         3. import可以导包 、导类 、 导对象

         4. import可以在一行中导入多个类

         5. import可以屏蔽包中的某个类

         6. import可以给包中的类取别名

         7. import默认从当前包进行导入

     */
    //import java.util.ArrayList
    //new ArrayList[String]

    //import java.util.ArrayList  // 导类
    //import java.util   // 导包
   // new util.ArrayList[String]

    //val user02 = new User02()
    //println(user02.username)
    //user02.getUsername()

    //import user02._  // 导对象
    //println(username)
    //getUsername()

    //import java.util.{ArrayList,HashMap}
    //new ArrayList[String]()
    //new HashMap[String,String]()


    //import java.util._
    //import java.sql.{ Date=>_ , _}
    //new Date()


    //import java.util.{ Date => UtilDate , _}
    //import java.sql.{ Date => SqlDate,_}
    //new UtilDate() // util.Date
    //new SqlDate(System.currentTimeMillis()) // sql.Date

    //import java.util
    //val list = new util.ArrayList
    //println(list.getClass.getName)

    import _root_.java.util
    val list: util.ArrayList[String] = new util.ArrayList[String]
    println(list.getClass.getName)

  }
}
/*
package java.util{

  class ArrayList{

  }
}
 */



class User02{

  var username : String = "lisi"

  def getUsername(): Unit ={
    username
  }
}
