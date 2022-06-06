package com.atguigu.scala.chapter02

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter, PrintWriter}
import java.nio.charset.StandardCharsets

import scala.io.StdIn

/**
  * Scala - 输出
  */
object Scala06_Out {
  def main(args: Array[String]): Unit = {
    //1. 往控制台输出
    println("hello scala")

    //2. 往文件中输出
    // 需求: 从控制台读取输入的内容，将内容写到output目录下的output.txt文件中
    println("请输入: ")
    val line: String = StdIn.readLine()

    //字节流
    val fos =
      new FileOutputStream(new File("D:\\IdeaProjects\\BigData210609\\Scala0609\\output\\output.txt"))

    //包装成字符流
    //new BufferedWriter()
    val writer = new PrintWriter(new OutputStreamWriter(fos,StandardCharsets.UTF_8),true)

    writer.println(line)
    //writer.flush()

    writer.close()
    fos.close()

  }
}
