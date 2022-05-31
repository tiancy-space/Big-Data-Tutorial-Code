package chapter07

/**
  * Scala - 集合 - 常用方法 ******
  */
object Scala09_method {
  def main(args: Array[String]): Unit = {
    //41. map : 集合映射. 将集合中的元素按照指定的规则(函数)进行转换.
    //需求: 将list集合中的元素乘以2
    val list = List(1,2,3,4,5)
    // 迭代处理
    var newList = for(i <- list ) yield {
      i * 2
    }
    println(newList)

    println(list.map(_ * 2))

    //需求: 将list1中的元素全部转换为大写形式
    var list1 = List("hello" , "scala", "spark" , "hadoop")

    println(list1.map(_.toUpperCase))

    //需求:将list2中的元组中的第二个元素提取首字母
    var list2 = List( (1,"Hello"), (2,"Scala") , ( 3,"Zookeeper")  )

    println(list2.map((t: (Int, String)) => {
      (t._1, t._2.charAt(0))
    }))
    println(list2.map(t => (t._1, t._2.charAt(0))))

    println(list2.map(_._2.charAt(0)))


    //42. flattern :  集合扁平化 ，将集合中的元素(一定是集合)拆分成一个一个的个体

    // 需求: 将list3转换为: List (1,2,3,4,5,6)
    var list3 = List (List(1,2), List(3,4),List(5,6))
    println(list3.flatten)

    var list4 = List("1","2","3","4","5")

    println(list4.flatten)

    var list5 = List ( "abc" ,"def" ,"123")
    println(list5.flatten)

    var list6 = List( List( List (1,2 ), List(3,4 )), List(List(5,6),List(7,8)))

    println(list6.flatten.flatten)


    //43. flatMap:  集合扁平映射
    // 需求: 将如下集合中的元素按照指定的分隔符拆分成一个个的单词
    val list7 = List("hello scala","hello spark")
    //map + flattern =>   List( Array(hello ,  scala) ,Array( hello , spark) )
    println(list7.map(_.split(" ")).flatten)
    println(list7.flatMap(_.split(" ")))

    //44. filter : 集合过滤 , 把集合中的满足指定规则（函数）的元素过滤出来

    // 需求: 将list8中的偶数过滤出来
    var list8 = List(1,2,3,4,5,6)
    println(list8.filter(_ % 2 == 0))

    //需求: 将list9中的单词首字母为"h"的单词提取出来
    val list9 = List("hello scala","hive spark")

    println(list9.flatMap(_.split(" ")).filter(_.startsWith("h")))


    //45. groupBy : 集合分组.  将集合中的元组按照指定的规则（函数）进行分组。
    //              处理后的结果是一个map， map中的key就是每个组计算的结果，value就是每个组中的元素.
    //需求: 将list10中的元素按照奇偶分组

    val list10 = List(1,2,3,4)
    println(list10.groupBy(_ % 2 ))

    //需求: 将如下集合中的单词按照首字母分组
    val list11 = List("hello scala","hive spark")

    println(list11.flatMap(_.split(" ")).groupBy(_.charAt(0)))

    //46. sortBy:  集合排序  , 将集合中的元素按照指定的规则进行排序
    // 需求: 将list12中的元素按照数字大小进行升序排序
    val list12 = List( 5, 2, 1, 7, 3, 9 ,4 )
    println(list12.sortBy(num => num))

    println(list12.sortBy(num => -num))

    println(list12.sortBy(num => num )(Ordering.Int.reverse)  )

    // 需求: 将list13中的元素按照元组的第一个元素进行升序排序
    val list13 = List( (30,"zhangsan"),(20,"wangwu"), (10,"lisi") , (20,"apple") , (10,"mark") )

    println(list13.sortBy(_._1))
    println(list13.sortBy(t => t ))  // 默认规则就是按照tuple中的元素升序比较

    // 需求: 将list13中的元素按照元组的第一个元素降序，第二个元素升序排序.

    println(list13.sortBy(t => t)(Ordering.Tuple2(Ordering.Int.reverse, Ordering.String)))


    // 47. sortWith :  自定义排序.

    // 需求: 将list13中的元素按照元组的第一个元素降序，第二个元素升序排序.
    //  升序：  左小右大   左 < 右
    //  降序:   左大右小   左 > 右
    println(list13.sortWith((t1, t2) => {

      if (t1._1 == t2._1) {
        t1._2 < t2._2
      } else {
        t1._1 >  t2._1
      }

    }))




  }
}
