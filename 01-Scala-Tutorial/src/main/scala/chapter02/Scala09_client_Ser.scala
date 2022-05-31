package chapter02

import java.io.ObjectOutputStream
import java.net.Socket

/**
  * 客户端 - 序列化
  */
object Scala09_client_Ser {
  def main(args: Array[String]): Unit = {
    val socket = new Socket("localhost",8888)
    val objectOutputStream = new ObjectOutputStream( socket.getOutputStream )
    val user = new User()
    objectOutputStream.writeObject(user)
    objectOutputStream.close()
    socket.close()
  }
}
