package chapter10

/**
  * Scala - 隐式转换 - 隐式参数 & 隐式变量
  */
object Scala04_transform {

  def main(args: Array[String]): Unit = {

    def regist(username: String, password : String ): Unit ={
      println(s"username = $username , password = $password")
    }
    regist("zhangsan","123456")


    def regist1(username: String, password : String = "000000" ): Unit ={
      println(s"username = $username , password = $password")
    }
    regist1("lisi")
    regist1("lisi","123456")


    // 隐式变量
    implicit var newPassword : String  = "222222"

    //implicit var newPassword1 : String  = "222222"


    // 隐式参数
    def regist2(username: String)(implicit password : String = "000000" ): Unit ={
      println(s"username = $username , password = $password")
    }
    regist2("lisi")()
    regist2("lisi")("123456")
    regist2("lisi")


    val list = List(6,2,9,1,3,8)

    println(list.sortBy(num => num)(Ordering.Int.reverse))


  }
}
