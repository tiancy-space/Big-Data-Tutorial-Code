package rdd.acc

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @Description: 广播变量的使用.
 * @Author: tiancy
 * @Create: 2022/6/23
 */
object Spark04_BroadCast_广播变量使用 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("acc1").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val kvRdd: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("a", 2), ("b", 4), ("c", 5), ("d", 6)), 2)
    var kvMap: mutable.Map[String, Int] = mutable.Map(
      ("a", 19),
      ("b", 16),
      ("c", 15),
      ("d", 14),
    )
    // 将当前Map集合进行广播.并在Executor端 RDD内的数据和Map中的元素进行相同key的合并.
    val kvMapBroadCast: Broadcast[mutable.Map[String, Int]] = sc.broadcast(kvMap)
    kvRdd.map {
      case (word, count) => {
        // 取当前RDD算子内的key,判断在当前广播变量集合中是否存在,存在则获取Map的value值,不存在则给一个0
        val ctn2: Int = kvMapBroadCast.value.getOrElse(word, 0)
        (word, count + ctn2)
      }
    }.collect().foreach(println)
    /*
        (a,20)
        (a,21)
        (b,20)
        (c,20)
        (d,20)
     */
  }
}