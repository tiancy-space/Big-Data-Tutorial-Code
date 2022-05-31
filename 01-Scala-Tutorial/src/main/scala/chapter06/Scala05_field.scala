package chapter06

import scala.beans.BeanProperty

/**
  * Scala - 面向对象编程 - 属性
  */
object Scala05_field {

  def main(args: Array[String]): Unit = {

    /*
       1. 在Scala中声明属性时，使用任意修饰符，最终编译后发现属性都是通过private修饰的。
          且val声明的属性, 会通过final来修饰.

       2. 在Scala中声明属性后，编译后会提供对应的"get/set"方法. 如果属性通过val声明,只会提供"get"方法
          通过private修饰的属性, "get/set"方法也是private修饰的。
          其他修饰符修饰的属性，"get/set"方法是通过public修饰的.

       3. 所谓的get/set方法是否真的有必要提供?

       4. 可以通过@BeanProperty注解来标注属性，就可以生成对应的get/set方法


     */

    val user05 = new User05()

    user05.name = "zhangsan"   //  name_$eq()

    println(user05.name)  // name()

  }
}

class User05{
  //定义属性
  // 语法:  [修饰符]  var | val  属性名 ： 属性类型 = 属性值

  private var password : String = _  // 属性默认初始化

  private val sex : String = "man" // val声明的属性必须直接初始化

  private[chapter06] var address : String = _

  protected  var salary : Double = _

  @BeanProperty
  var name : String = _


}

