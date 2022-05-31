package chapter13

import java.io.{ObjectInputStream, ObjectOutput, ObjectOutputStream}
import java.net.{ServerSocket, Socket}
import java.util.concurrent.TimeUnit

import scala.collection.mutable.ListBuffer

/**
  *  驱动中心
  *
  *  1. 规划任务(生成切片, 计算所需资源数)
  *
  *  2. 申请资源  Message(3)
  *
  *  3. 下发任务
  *
  *  4. 汇总结果
  */
object Driver {

  def main(args: Array[String]): Unit = {
    val srcDatas = List(1,2,3,4,5,6,7,8)
    val splitDatas: List[List[Int]] = split(srcDatas)
    requestResource(splitDatas.size)
    handleResults(splitDatas.size)
    sendTask(splitDatas)
  }

  private val results: ListBuffer[Int] = ListBuffer[Int]()

  /**
    * 4. 汇总结果
    */
  def handleResults(resultNums : Int ): Unit ={
    new Thread(
      new Runnable {
        override def run(): Unit = {
          while(results.size != resultNums){
            println("等待汇总结果中......")
            TimeUnit.SECONDS.sleep(1)
          }
          println("最终的结果为: " + results.sum)
        }
      }
    ).start()
  }

  /**
    * 3. 下发任务
    */
  def sendTask(splitDatas: List[List[Int]]): Unit = {
    // 读取驱动中心的port
    val port: Int = PropsUtils.getValue(Config.DRIVER_PORT).toInt
    val driver = new ServerSocket(port)
    // 接收计算节点的请求
    for(i <- 1 to splitDatas.size){
      new Thread(
        new Runnable {
          override def run(): Unit = {
            val executor: Socket = driver.accept()
            val out = new ObjectOutputStream( executor.getOutputStream )
            //下发任务
            val datas: List[Int] = splitDatas(i-1)
            val logic : List[Int] => Int = _.sum
            val task = Task(datas,logic)
            out.writeObject(task)

            // 等待结果

            val in = new ObjectInputStream( executor.getInputStream )
            val result: Int = in.readObject().asInstanceOf[Int]
            results.append(result)
            println( datas  + "的计算结果为: " + result)
          }
        }
      ).start()

    }
  }

  /**
    * 2.申请资源
    */
  def requestResource( resourceNums: Int): Unit = {
    // 读取资源中心的 host  和 port
    val host: String = PropsUtils.getValue(Config.RESOURCE_HOST)
    val port: Int = PropsUtils.getValue(Config.RESOURCE_PORT).toInt
    val socket = new Socket(host,port)
    //发送资源申请
    val out = new ObjectOutputStream( socket.getOutputStream )
    val message = Message(resourceNums)
    out.writeObject(message)
  }

  /**
    * 1. 规划任务
    */
  def split(srcDatas : List[Int]): List[List[Int]] = {
    // 读取计算节点可计算的数量
    val calNums: Int = PropsUtils.getValue(Config.EXECUTOR_CAL_NUMS).toInt
    val splitDatas: List[List[Int]] = srcDatas.sliding(calNums,calNums).toList
    splitDatas
  }

}
