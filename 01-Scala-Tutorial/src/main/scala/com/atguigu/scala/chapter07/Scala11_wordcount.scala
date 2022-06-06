package com.atguigu.scala.chapter07

/**
  * Scala - 集合 - wordcount
  */
object Scala11_wordcount {

  def main(args: Array[String]): Unit = {

    val dataList = List(
      ("Hello Scala", 4),
      ("Hello Spark", 2),
      ("Scala Hive",3)
    )

    //思路一:  ("Hello Spark", 2) =>  Hello Spark Hello Spark
    val wordLines: List[String] = dataList.map( t => (t._1 + " ") * t._2)
    println(wordLines)
    val result1: Map[String, Int] = wordLines.flatMap(_.split(" ")).groupBy(word => word ).mapValues(_.size)
    println(result1)

    //思路二: ("Hello Spark", 2)  => (Hello,2) (Spark ,2 )
    // ("Hello Spark", 2)  => "Hello Spark" => [ Hello Spark ] =>  ( Hello,2 ) (Spark,2)
    val wordnums: List[(String, Int)] = dataList.flatMap( t => t._1.split(" ").map( (_,t._2) ) )
    println(wordnums)

    val wordGroup: Map[String, List[(String, Int)]] = wordnums.groupBy(_._1)
    println(wordGroup)

    //Hello -> List((Hello,4), (Hello,2))   => ( Hello ,6 )

    val wordcount: Map[String, Int] = wordGroup.mapValues( _.map(_._2).sum )
    println(wordcount)

  }
}
