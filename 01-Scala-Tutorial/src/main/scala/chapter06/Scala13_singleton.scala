package chapter06

/**
  * Scala - 面向对象编程 - 单例对象
  */
object Scala13_singleton {
  def main(args: Array[String]): Unit = {
    val user1 = User13()
    val user2 = User13()

    println(user1 eq user2)


  }
}

// 单例类
class User13 private {

}
// 伴生对象可以访问伴生类的私有内容
// 伴生对象就是单例对象。
object User13{

  var user13 : User13  = new User13

  def apply() : User13={
    //new User13
    user13
  }
}


