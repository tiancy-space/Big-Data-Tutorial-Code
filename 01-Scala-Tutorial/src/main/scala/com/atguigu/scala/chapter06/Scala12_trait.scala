package com.atguigu.scala.chapter06

/**
  * Scala - 面向对象编程 - 特质
  */
object Scala12_trait {

  def main(args: Array[String]): Unit = {
    // 初始叠加
    // 按照混入的顺序从左往右依此初始化
    //val mysql = new MySQL12


    // 功能叠加
    // 按照混入的顺序从右往左叠加
    // super： 上一层特质
    val mysql: MySQL13 = new MySQL13
    mysql.operData()

  }
}

trait Operate13{
  def operData():Unit={
    println("操作数据。。")
  }
}

trait DB13 extends Operate13{
  override def operData(): Unit = {
    print("向数据库中。。")
    super.operData()
  }
}
trait Log13 extends Operate13{

  override def operData(): Unit = {
    print("向日志中。。")

    super[Operate13].operData()
  }
}


class MySQL13 extends DB13 with Log13 {

}


trait Operator12 {
  println("operator...")
}
trait DB12 {
  println("db...")
}
class MySQL12 extends DB12 with Operator12{
  println("mysql...")
}
