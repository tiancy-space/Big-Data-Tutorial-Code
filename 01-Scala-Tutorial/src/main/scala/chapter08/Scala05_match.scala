package chapter08

/**
  * Scala - 模式匹配 - 使用场景
  */
object Scala05_match {
  def main(args: Array[String]): Unit = {
    //1. 变量声明
    val tuple: (String, Int, Boolean, Double) = ("zhangsan",30,false,10000.1)
    println(tuple._1 + " , " + tuple._2 + " , " + tuple._3 + " , " + tuple._4)

    var result = tuple match {
      case (name, age ,marry, salary ) =>  name + " , " + age + " , " + marry  + " , " + salary
      case _ => "no"
    }
    println(result)

    var  (name, age ,marry, salary )  = ("zhangsan",30,false,10000.1)

    println(s"name = $name , age = $age , marry = $marry , salary = $salary ")


    val Array(first, second, _*) = Array(1, 7, 2, 9)
    println(s"first=$first,second=$second")

    val Person(username, userage) = Person("zhangsan", 16)
    println(s"name=$username,age=$userage")


    println("---------------------------------")

    //2.  循环匹配
    val map = Map("A" -> 1, "B" -> 0, "C" -> 3)

    //需求一: 将map中的元素循环打印
    //常规写法:
    for(kv <- map ){
      println(kv._1 + "->" + kv._2)
    }
    println("---------------------------------")
    //模式匹配
    for(kv <- map ){
      kv match {
        case (k,v) => println(k + "->" + v)
        case _ => println("error")
      }
    }
    println("---------------------------------")

    for( (k,v) <- map ){
      println(k + "->" + v )
    }

    println("---------------------------------")

    // 需求二: 遍历value=0的 k-v ,如果v不是0,过滤
    // 常规写法:
    for(kv <- map ){
      if(kv._2 == 0) {
        println(kv._1 + "->" + kv._2)
      }
    }
    println("---------------------------------")
    //模式匹配
    for(kv <- map ){
      kv match {
        case (k,0) => println(k + "->" + 0)
        case _ => "error"
      }
    }
    println("---------------------------------")

    for( (k,0) <- map ){
      println(k + "->" + 0 )
    }

    println("---------------------------------")
    // 需求三: 过滤掉v==0的kv
    //常规写法:
    for(kv <- map ){
      if(kv._2 != 0) {
        println(kv._1 + "->" + kv._2)
      }
    }
    println("---------------------------------")
    // 模式匹配:
    for(kv <- map ){
      kv match {
        case (k,v) => { if(v!=0) println( k + "->" + v ) }
      }
    }

    println("---------------------------------")

    for( ( k ,v ) <- map if v !=0){
      println( k + "->" + v )
    }

    println("---------------------------------")

    //3. 函数参数
    // 需求:  将如下list中的tuple的第二个元素乘以2
    val list = List(("a",1),("b",2),("c",3))

    //常规写法:
    println(list.map(t => (t._1, t._2 * 2)))

    //模式匹配
    println(list.map(
      t => t match {
        case (word, count) => (word, count * 2)
      }
    ))

    println(list.map{
      case (word,count) => (word, count * 2 )
    })

    // 需求: 获取map中的value的第二个元素乘以2的结果
    val map1 = Map("a"->("aa",1),"b"->("bb",2),"c"->("cc",3))
    //常规写法
    println(map1.map(t => (t._1, (t._2._1, t._2._2 * 2))))

    //模式匹配
    println(map1.map{
      case (outk,(innerk,innerv)) => (outk,(innerk,innerv*2))
    })


  }
}

case class Person(name: String, age: Int)