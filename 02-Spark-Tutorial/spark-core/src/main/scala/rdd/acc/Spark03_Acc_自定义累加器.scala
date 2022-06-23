package rdd.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @Description: 演示自定义累加器写法. 实现两个可变集合的合并操作.
 * @Author: tiancy
 * @Create: 2022/6/23
 */
object Spark03_Acc_自定义累加器 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("acc1").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val lineRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/word.txt", 2)
    val wordsRDD: RDD[String] = lineRDD.flatMap(_.split(" "))
    // Hello,World,Hello,Spark,Hello,Scala,Hello,RDD,hello
    //println(wordsRDD.take(9).mkString(","))

    /*
        TODO 自定义累加器的定义和使用.
          1、先确定当前RDD的输入类型以及参数个数,以及实现的功能: 这里当前RDD的输入为一个个的单词. 要实现的功能: 创建一个Scala中的可变Map集合,其中key = word,value = 当前单词在当前集合中出现的次数. 多个Map间相同的key进行合并就是最终的 统计单词的结果.
          2、自定义一个类 extends AccumulatorV2,确定输入和输出的数据类型,重写里面的方法.
          3、使用自定义累加器类.
            3.1、创建累加器类型的对象.
            3.2、注册累加器
            3.3、使用
     */
    val wcAcc = new MyWordCountAccumulator
    sc.register(wcAcc, "customWordCountAcc") // 名字任意.
    wordsRDD.foreach(
      word => {
        wcAcc.add(word)
      }
    )

    val wordCountMap: mutable.Map[String, Int] = wcAcc.value
    wordCountMap.foreach(println)

    sc.stop()
  }
}

/*
      TODO
       自定义累加器类,来继承  org.apache.spark.util.AccumulatorV2[Input,OutPut]这个类,从而使用累加器实现wordCount,因此输入一个字符串,返回一个集合
   */
class MyWordCountAccumulator extends AccumulatorV2[String, mutable.Map[String, Int]] {
  //TODO 定义一个可变集合,作为累加器的返回值类型
  private val wordCtn = mutable.Map[String, Int]()

  //TODO 判断当前累加器是否为初始化状态 此方法主要作用:防止多台Executor在分布式环境下,有的节点由于网络阻塞,而读取到已经计算好返回给 Driver的非空集合.
  override def isZero: Boolean = wordCtn.isEmpty

  //TODO 将当前Driver中定义的变量复制,再分发到不同的计算节点上使用.
  override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = {
    new MyWordCountAccumulator()
  }

  //TODO 如果当前节点使用的Driver端的变量不是空的,则说明这个集合是某台节点的返回值与本地合并的结果.我需要重置此集合再计算返回.
  override def reset(): Unit = {
    wordCtn.clear()
  }

  //TODO 将当前分区中的数据存放到集合中.
  override def add(word: String): Unit = {
    //判断当前单词在此map中是否存在,如果存在,则直接返回当前单词的ctn,如果不存在,则给一个默认值0
    val oldCtn: Int = wordCtn.getOrElse(word, 0)
    //单词存入map后,次数加 1
    wordCtn.update(word, oldCtn + 1)
  }

  /**
   * @param other 另一个计算节点上计算成功所返回的wordCount集合
   *              TODO 当前计算完成的集合 wordCtn 与另一台计算完成的集合 otherMap 进行相同key的value合并
   */
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit = {
    // TODO 多个executor执行结果,拉取到driver进行map间的合并
    //  Map间合并规则 : 可以将一个map作为参照,遍历另一个集合,拿每一个元素看在map中是否存在,如果存在则value值相加,如果不存在则给一个默认值0
    //判断当前传入的字符串,是否在统计单词的ctnMap中存在,如果存在,则更新当前word的cnt,否则给一个默认值 0

    //取另一个executor的Map,与当前的wordCtn合并
    val otherMap: mutable.Map[String, Int] = other.value
    /*
      TODO 遍历另一个机器上的集合otherMap,判断当前集合中是否存在,如果存在则数量相加,如果不存在,则给一个初始值
   */
    otherMap.foreach {
      //开始模式匹配
      case (word, count) => {
        val oldCtn: Int = wordCtn.getOrElse(word, 0)
        //修改当前集合中word的出现次数.
        wordCtn.update(word, oldCtn + count)
      }
    }
  }

  //最终计算的结果,一个Map集合返回给Driver端的调用者
  override def value: mutable.Map[String, Int] = {
    // TODO 将当前机器上的集合作为最终合并好的结果返回.
    wordCtn
  }
}
