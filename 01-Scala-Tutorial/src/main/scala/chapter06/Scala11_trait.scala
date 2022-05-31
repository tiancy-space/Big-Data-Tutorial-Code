package chapter06

/**
  * Scala - 面向对象编程 - 特质
  */
object Scala11_trait {
  def main(args: Array[String]): Unit = {
     //静态混入
     val mySQL11 = new MySQL11


    //动态混入
    /*
    val mySQL11 = new MySQL11 with Operator11 with DB {
      override var name: String = _

      override def operData(): Unit = {
        println("MySQL11 operData....")
      }
    }

     */

    mySQL11.insert()
    mySQL11.storeData()
    mySQL11.operData()
  }

}

//声明特质

trait Operator11{

  //抽象属性
  var name : String

  //具体属性
  var password : String = "123456"

  //抽象方法
  def operData() :Unit

  //具体方法
  def storeData() : Unit = { println("Operator11 storeData") }
}

trait DB{

  def insert(): Unit ={
    println("DB insert ")
  }
}


// 使用特质
// 混入特质:  extends ... with ....with ... with

class MySQL11 extends Operator11  with DB {

  override var name: String = "MySQL"

  override def operData(): Unit = { println("MySQL11 operData") }
}


/*
class MySQL11 {

}
 */