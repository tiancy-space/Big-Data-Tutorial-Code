package com.atguigu.scala.chapter13

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket
import java.util.Random
import java.util.concurrent.TimeUnit


/**
  * 计算节点
  *
  * 1. 接收驱动中心下发的任务,完成计算，返回结果
  */
class Executor(var name : String ) {

  override def toString: String = name

  def start(): Unit ={
    var thisExecutor = this
    new Thread(
      new Runnable {
        override def run(): Unit = {
          // 读取驱动中心的 host  port
          val host: String = PropsUtils.getValue(Config.DRIVER_HOST)
          val port: Int = PropsUtils.getValue(Config.DRIVER_PORT).toInt
          val executor = new Socket(host,port)

          //读取任务
          val in = new ObjectInputStream( executor.getInputStream )
          val task: Task = in.readObject().asInstanceOf[Task]

          val random = new Random()
          val ses: Int = random.nextInt(10) + 1
          TimeUnit.SECONDS.sleep(ses)

          //计算
          val result: Int = task.logic(task.datas)

          //返回结果
          val out = new ObjectOutputStream( executor.getOutputStream )
          out.writeObject(result)

          //回收资源
          ResourceCenter.returnResource(thisExecutor)
        }
      }
    ).start()
  }
}
