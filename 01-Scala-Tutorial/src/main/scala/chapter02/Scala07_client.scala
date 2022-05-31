package chapter02

import java.io.{BufferedReader, InputStreamReader, OutputStreamWriter, PrintWriter}
import java.net.Socket

/**
  * 网络通信 - 客户端
  */
object Scala07_client {
  def main(args: Array[String]): Unit = {
    val socket = new Socket("localhost",9999)

     //给服务端发送消息
    val out = new  PrintWriter(new OutputStreamWriter(socket.getOutputStream),true)
    out.println("hello a")

    // 接收服务端的消息
    val in = new BufferedReader( new InputStreamReader( socket.getInputStream))
    val line: String = in.readLine()
    println("服务端说: " + line )

    //out.close()
    //in.close()

    //socket.close()
  }
}
