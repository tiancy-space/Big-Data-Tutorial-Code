package chapter10

/**
  * Scala - 隐式转换 - 隐式类
  */
object Scala03_transform {
  def main(args: Array[String]): Unit = {
    val mysql03 = new MySQL03
    mysql03.insert()

    mysql03.select()

    // 隐式函数
    /*
    implicit  def transform(mySQL03: MySQL03) : MySQLExt03 = {
      new MySQLExt03
    }
     */


    // StringOps

    var str : String = "abcd"

    println(str.substring(0, 3))

    println(str.slice(0, 3))   // String -> StringOps


  }

  // 隐式类
  // 功能扩展类  + 隐式函数
  implicit class MySQLExt03(mySQL03: MySQL03){
    def select(): Unit ={
      println("select.....")
    }
  }

}



class MySQL03{

  def insert(): Unit ={
    println("insert.....")
  }
}