package chapter13

import java.io.ObjectInputStream
import java.net.{ServerSocket, Socket}
import java.util.concurrent.TimeUnit

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * 资源中心
  *
  * 1. 启动计算节点
  *
  * 2. 管理计算节点
  *
  * 3. 接收驱动中心资源的申请
  *
  * 4. 分配资源
  *
  * 5. 回收资源
  *
  */
object ResourceCenter {

  def main(args: Array[String]): Unit = {
    startExecutor()
    statusExecutor()
    handleDriverResourceRequest()
    acceptDriverResourceRequest()
  }

  // 定义集合维护计算节点(可用)
  private val canUseExecutors: ListBuffer[Executor] = ListBuffer[Executor]()

  // 定义集合维护正在使用的计算节点
  private val inUseExecutors: mutable.Set[Executor] = mutable.Set[Executor]()

  // 定义队列维护驱动中心的资源申请
  private val resourceRequests = new mutable.Queue[Socket]()


  /**
    * 5. 回收资源
    */
  def returnResource(executor: Executor): Unit ={
    //从正在使用的列表中移除
    inUseExecutors.remove(executor)
    //加入到可用的列表中
    canUseExecutors.append(executor)
  }

  /**
    * 4. 分配资源
    */
  def handleDriverResourceRequest(): Unit ={
    new Thread(
      new Runnable {
        override def run(): Unit = {
          while(true){
            //查看是否有资源申请
            while(resourceRequests.isEmpty){
              println("===============> 暂时没有资源申请，1秒后重试 <==============")
              TimeUnit.SECONDS.sleep(1)
            }

            // 从队首取出资源申请 (FIFO)
            val driver: Socket = resourceRequests.dequeue()

            // 通信， 获取驱动中心发送的消息
            val in = new ObjectInputStream( driver.getInputStream )
            val message: Message = in.readObject().asInstanceOf[Message]
            val executors: Int = message.nums

            //分配资源
            for(i <- 1 to executors) {
              // 查看是否有可用资源
              while(canUseExecutors.isEmpty){
                println("===============> 暂时没有可用资源，1秒后重试 <==============")
                TimeUnit.SECONDS.sleep(1)
              }
              val executor: Executor = canUseExecutors.remove(0)
              //放入到正在使用的列表中
              inUseExecutors.add(executor)
              //让计算节点开始工作
              executor.start()
            }
          }
        }
      }
    ).start()
  }

  /**
    * 3. 接收驱动中心资源申请
    */
  def acceptDriverResourceRequest(): Unit ={

    //读取资源中心的  port
    val port: Int = PropsUtils.getValue(Config.RESOURCE_PORT).toInt
    //起服务
    val resourceCenter = new ServerSocket(port)

    //持续监听驱动中心的资源申请
    while(true){
      val driver: Socket = resourceCenter.accept()
      // 将资源申请入队
      resourceRequests.enqueue(driver)
    }

  }


  /**
    * 2. 管理计算节点
    */
  def statusExecutor(): Unit ={
    new Thread(
      new Runnable {
        override def run(): Unit = {
          while(true){
            println("canUse : " + canUseExecutors)
            println()
            TimeUnit.SECONDS.sleep(3)
          }
        }
      }
    ).start()
  }

  /**
    * 1. 启动计算节点
    */
  def startExecutor(): Unit ={
    //读取计算节点的个数
    val executorNums: Int = PropsUtils.getValue(Config.EXECUROT_NUMS).toInt
    for(i <- 1 to  executorNums) {
      val executor = new Executor("E"+ i)
      canUseExecutors.append(executor)
    }
  }
}
