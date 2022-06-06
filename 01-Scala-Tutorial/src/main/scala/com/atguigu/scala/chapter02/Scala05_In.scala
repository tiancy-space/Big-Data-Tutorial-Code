package com.atguigu.scala.chapter02

import java.io.{BufferedReader, File, FileInputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import scala.io.{BufferedSource, Source, StdIn}

/**
  * Scala - 输入
  */
object Scala05_In {
  def main(args: Array[String]): Unit = {
    // 1. 从控制台输入
    // Java  : Scanner
    // Scala : StdIn
    //println("请输入: ")
    //val line: String = StdIn.readLine()
    //println("输入: " + line)


    //2. 从文件中输入
    //2.1 IO
    // 字节流
    val fis =
        new FileInputStream(new File("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt"))
    // 包装成字符流
    val reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))

    //var readLine = ""
    // Java : (readLine = reader.readLine()) != null  => 判断readLine是否为null
    // Scala: (readLine = reader.readLine()) != null  => 判断(readLine = reader.readLine()) 表达式是否为null
//    while( (readLine = reader.readLine()) != null){
//      println(readLine)
//    }

    var flag  = true
    while(flag) {
      val line: String = reader.readLine()
      if(line != null ){
        println(line)
      }else{
        flag = false
      }
    }
    reader.close()
    fis.close()

    println("===================================================")
    // 2.2  Source

    val lines: Iterator[String] =
      Source.fromFile("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt").getLines()
    while(lines.hasNext){
      val line: String = lines.next()
      println(line)
    }

    // 如下代码先了解

    println("===================================================")
    val lines1: Iterator[String] =
      Source.fromFile("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt").getLines()

    lines1.foreach( (line)=>{println(line)} )

    println("===================================================")
    val lines2: Iterator[String] =
      Source.fromFile("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt").getLines()

    lines2.foreach( println(_) )

    /*
    println("===================================================")
    val lines3: Iterator[String] =
      Source.fromFile("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt").getLines()

    lines3.foreach( println )

    println("===================================================")
    Source.fromFile("D:\\IdeaProjects\\BigData210609\\Scala0609\\input\\word.txt").getLines().foreach(println)

    */





  }
}
