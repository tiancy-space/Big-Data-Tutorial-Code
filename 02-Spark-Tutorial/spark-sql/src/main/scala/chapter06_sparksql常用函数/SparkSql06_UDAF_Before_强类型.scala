package chapter06_sparksql常用函数

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator

/**
 * @Description: spark sql 自定义UDAF,3.0版本以前的强类型
 * @Author: tiancy
 * @Create: 2022/7/11
 */
object SparkSql06_UDAF_Before_强类型 {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL")
    val spark =
      SparkSession.builder()
        .config(conf)
        .getOrCreate()
    import spark.implicits._

    val df = spark.read.json("./02-Spark-Tutorial/data/user.json")

    // Spark早期版本没有办法将强类型的UDAF函数应用在SQL文中
    // 强类型
    val ds = df.as[User]
    // DSL语法
    // 创建UDAF函数
    val udaf = new MyAvgAgeUDAF
    ds.select(udaf.toColumn).show


    spark.stop()

  }

  case class User(id: Long, name: String, age: Long)

  case class AvgBuffer(var sum: Long, var cnt: Long)

  /*
     TODO 自定义聚合函数（UDAF）(强类型)
       1. 继承类 Aggregator
       2. 定义泛型
          IN : User
          BUFF: AvgBuffer
          OUT : Long
       3. 重写方法
       spark sql 自定义UDAF强类型写法总结: 继承 `Aggregator` ,定义泛型[输入、缓冲区、输出] 重写方法: 初始化缓冲区、reduce、merge、finish
   */
  class MyAvgAgeUDAF extends Aggregator[User, AvgBuffer, Long] {
    override def zero: AvgBuffer = {
      AvgBuffer(0L, 0L)
    }

    override def reduce(buff: AvgBuffer, user: User): AvgBuffer = {
      buff.sum += user.age
      buff.cnt += 1
      buff
    }

    override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
      b1.sum += b2.sum
      b1.cnt += b2.cnt
      b1
    }

    override def finish(buff: AvgBuffer): Long = {
      buff.sum / buff.cnt
    }

    override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

    override def outputEncoder: Encoder[Long] = Encoders.scalaLong
  }

}
