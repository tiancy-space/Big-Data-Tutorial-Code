package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark17_RDD_Transform_AggregateByKey {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO aggregateByKey 中初始值的用法
    // 分区内和分区间进行区分
    // reduceByKey：分区内和分区间的计算规则相同
    val keyValueRDD = sc.makeRDD(
      List(
        ("a", 1), ("a", 2), ("b", 3),
        ("b", 4), ("b", 5), ("a", 6)
      ), 2
    )
    /*
    aggregateByKey(0)(_ + _, _ + _)
        分区内求和: ("a",3),("b",3) | ("b",9),("a",6)
        分区间求和: ("a",9),("b",12)
    aggregateByKey(1)(_ + _, _ + _)
        分区内求和: 每个分区内的初始值会和相同key的value进行一次运算.
          例如第一个分区中: ("a", 1), ("a", 2), ("b", 3) ==> ("a", 1 + 1), ("a", 2), ("b", 3 + 1), ==> ("a", 4), ("b", 4)
          第二个分区中: ("b", 4), ("b", 5), ("a", 6) ==> ("b", 4 + 1), ("b", 5), ("a", 6 + 1) ==> ("b", 10), ("a", 7)
         分区见求和: ("b", 14) ,("a",11)
     */
    keyValueRDD.aggregateByKey(1)(_ + _, _ + _).collect().foreach(println)

    sc.stop()
  }
}
