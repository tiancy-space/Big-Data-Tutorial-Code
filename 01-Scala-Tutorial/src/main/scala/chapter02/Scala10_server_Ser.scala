package chapter02

import java.io.ObjectInputStream
import java.net.{ServerSocket, Socket}

/**
  *  服务端 - 序列化
  */
object Scala10_server_Ser {
  def main(args: Array[String]): Unit = {
    val serverSocket = new ServerSocket(8888)
    val socket: Socket = serverSocket.accept()
    //读取客户端发送过来的对象
    val objectInputStream = new ObjectInputStream(socket.getInputStream)
    val user: User = objectInputStream.readObject().asInstanceOf[User]

    println(user.username)

    objectInputStream.close()
    socket.close()
    serverSocket.close()
  }
}
