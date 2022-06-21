package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark07_RDD_Transform_Test {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO filter : **从服务器日志数据apache.log中获取2015年5月17日的请求路径**
    val rdd = sc.textFile("./02-Spark-Tutorial/data/apache.log")
    // rdd.collect().foreach(println)
    /** 83.149.9.216 - - 17/05/2015:10:05:03 +0000 GET /presentations/logstash-monitorama-2013/images/kibana-search.png */
    val tuple_time_url: RDD[(String, String)] = rdd.map(
      line => {
        val lineArray: Array[String] = line.split(" ")
        (lineArray(3), lineArray(6))
      }
    )
    // 在scala中的spark,提供了非常多的函数和方法,比如下面用到的这个: startsWith(), 以 ... 开头,非常好用.可以记一下.
    tuple_time_url.filter(_._1.startsWith("20/05/2015:")).map(_._2).collect().foreach(println)
    sc.stop()
  }
}
