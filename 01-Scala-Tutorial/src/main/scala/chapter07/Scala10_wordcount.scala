package chapter07

import scala.io.Source

/**
  *  Scala - 集合 - wordcount
  */
object Scala10_wordcount {

  def main(args: Array[String]): Unit = {
    //1. 将文件中的数据读取到集合中
    val datas: List[String] =
      Source.fromFile("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt").getLines().toList
    println("datas => " + datas)
    // datas => List(hello scala world, hello scala hive, hello hello scala)

    //2. 将集合中的每个元素(一行数据)处理成一个一个的单词
    val words: List[String] = datas.flatMap( _.split(" "))
    println("words => " + words )

    //3. 按照单词分组

    val wordGroup: Map[String, List[String]] = words.groupBy(word => word)
    println("wordGroup => " + wordGroup )

    //4. 统计每个单词出现的次数
    // scala -> List(scala, scala, scala)   =>  scala -> 3
    //val wordCount: Map[String, Int] = wordGroup.map(t => (t._1,t._2.size) )
    val wordCount: Map[String, Int] = wordGroup.mapValues(_.size)
    println("wordcount => " + wordCount)

    //5. 取top2
    val finalResult: List[(String, Int)] = wordCount.toList.sortBy(_._2)(Ordering.Int.reverse).take(2)
    println("finalResult => " + finalResult)

    println("----------------------------------------------------------")

    val results: List[(String, Int)] = datas.flatMap(_.split(" ")).
      groupBy(word => word).
      mapValues(_.size).
      toList.
      sortBy(_._2)(Ordering.Int.reverse).
      take(3)
    println(results)


  }
}
