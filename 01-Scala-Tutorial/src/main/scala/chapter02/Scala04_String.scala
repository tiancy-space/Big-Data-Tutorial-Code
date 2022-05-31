package chapter02

/**
  * Scala - 字符串
  */
object Scala04_String {

  def main(args: Array[String]): Unit = {
    // Scala直接使用的是Java的String类。
    // type String        = java.lang.String

    // 创建字符串对象
    var str : String = "abc"

    var username = "zhangsan"
    var age  = 30

    // 字符串拼接操作
    //1.  +
    println("username = " + username + " , age = " + age )

    //2. 传值字符串 printf
    printf("username = %s , age = %s ",username, age )
    println()

    //3. 插值字符串
    println(s"username = $username , age = $age")

    //4. 多行字符串
    var sql  = "   select id, name , count(*) cnt  from user where id > ? group by id,name having  cnt > ? order by cnt desc  "
    println(sql)

    var sql1 =
      s"""
        |select
        |  id , name , count(*) cnt
        |from
        |  user
        |where
        |   id > ?
        |and
        |   name = $username
        |group by
        |   id, name
        |having
        |   cnt > ?
        |order by
        |   cnt
        |desc
      """.stripMargin

    println(sql1)

  }
}
