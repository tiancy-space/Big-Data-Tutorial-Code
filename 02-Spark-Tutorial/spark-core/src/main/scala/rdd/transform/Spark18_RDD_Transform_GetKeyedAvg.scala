package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark18_RDD_Transform_GetKeyedAvg {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
    val sc = new SparkContext(conf)

    // TODO 取出每个相同key的平均值
    val rdd = sc.makeRDD(
      List(
        ("a", 1), ("a", 2), ("b", 3),
        ("b", 4), ("b", 5), ("a", 6)
      ), 2
    )

    /** 使用map数据格式转化 + reduceByKey 实现每个key 的平均值 */
    rdd.map {
      // (key, value) => (key,value的值,出现1次)
      case (key, value) => (key, (value, 1))
    }.reduceByKey(
      // 相同key的两个元素的value进行操作,上面map后的结果是两个tuple,第一个元素:相同key对应的值.第二个元素: 出现的次数.
      (tuple1, tuple2) => {
        // 相同key的value值的和,出现次数
        (tuple1._1 + tuple2._1, tuple1._2 + tuple2._2)
      }
    ).collect().foreach(println) // (b,(12,3) (a,(9,3))

    println("****" * 4)
    /*
           TODO combineByKey算子可以传递三个参数
            1. 相同key的第一个value如何转换
            2. 分区内计算规则
            3. 分区间计算规则
                 rdd.combineByKey(
                      v => (v, 1),
                     (t:(Int, Int), v) => {
                         (t._1 + v, t._2 + 1)
                     },
                     (t1:(Int, Int), t2:(Int, Int)) => {
                         (t1._1 + t2._1, t1._2 + t2._2)
                     }
                 ).collect().foreach(println)
     */

    rdd.combineByKey(
      value => (value, 1), // 相同key的第一个value如何转换. 比如: ("a",1),("a",3) 相同key "a"的第一个value 1 => (1,1)
      (t: (Int, Int), v) => { // 分区内的计算逻辑: 两个相同key的value值相加,第一个value的(1,1) 后面的1需要每次加1
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => { // 分区间的计算逻辑.
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    ).collect().foreach(println)

    println("======" * 5)

    /** 按照自己的理解写 */
    rdd.combineByKey(
      (value: (Int)) => {
        (value, 1)
      },
      // t代表相同key的第一个元素:是一个经过转化后的元组(value值,1),v代表相同key的第二个元素,是一个单一的值,value值 ==> value值相加,第一个元素的1 加 1
      (t: (Int, Int), v: (Int)) => {
        (t._1 + v, t._2 + 1)
      },
      // 分区间的计算逻辑,每个分区内,都对相同key的第一个值进行了转化操作,因此经过分区内的计算后,都是元组.元组的第一个值为相同key的value值,第二个元素为相同key出现的次数.
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    ).collect().foreach(println)


    sc.stop()

  }
}
