package com.atguigu.scala.chapter02

import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStreamWriter, PrintWriter}
import java.net.{ServerSocket, Socket}

/**
  * 网络通信 - 服务端  - ServerSocket
  */
object Scala08_server {
  def main(args: Array[String]): Unit = {
    // 起服务
    val serverSocket = new ServerSocket(9999)
    println("等待客户端的连接.......")
    //接收客户端的连接
    while(true){
      val socket: Socket = serverSocket.accept()  // accept阻塞方法.
      println("有客户端连接成功")
      //将当前客户端的请求交给线程处理
      new Thread(
        new Runnable {
          override def run(): Unit = {
            // 线程的处理
            // 获取客户端发送的消息
            val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
            val message: String = in.readLine()
            println("客户端说: " + message)

            // 给客户端发送消息
            val out = new PrintWriter( new OutputStreamWriter( socket.getOutputStream ),true)
            out.println("byebye....")

            //in.close()
            //out.close()
          }
        }
      ).start()
    }
  }
}
