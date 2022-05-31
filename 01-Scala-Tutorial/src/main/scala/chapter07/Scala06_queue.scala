package chapter07

import scala.collection.mutable.Queue

/**
  * Scala - 集合 - 队列 （先进先出）
  */
object Scala06_queue {
  def main(args: Array[String]): Unit = {
    // Queue
    val queue = new Queue[String]

    //入队
    // a b c d
    queue.enqueue("a","b","c")
    queue.enqueue("d")

    //出队

    println(queue.dequeue())  //a
    println(queue.dequeue())  //b
    println(queue.dequeue())  //c
    println(queue.dequeue())  //d



  }
}
