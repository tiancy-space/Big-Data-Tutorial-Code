package chapter06_sparksql常用函数

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}

/**
 * @Description: spark sql 自定义UDAF,3.0版本以后的强类型写法.
 * @Author: tiancy
 * @Create: 2022/7/11
 */
object SparkSql07_UDAF_After_强类型 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Spark-sql DataFrame-Dataset-RDD")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    /*
  TODO 什么是 UDAF ?
    所谓的UDAF,就是在使用SApark-sql时,spark-sql模块提供的聚合函数不能满足我们的需求时,需要自己指定函数,并且需要指定聚合规则,从而实现具体功能.
    UDAF,也就是在sql文中能使用的自定义聚合函数,且UDAF需要自己定义类来继承具体的类`Aggregator`.
    下面提供一个使用UDAF案例 : 如果说,我们的sql中没有提供求平均值的函数,那么我们自己该如何实现`求具体列的平均值`
 */
    val dataFrame: DataFrame = spark.read.json("./02-Spark-Tutorial/data/user.json")
    dataFrame.createOrReplaceTempView("user")
    /*
    1、声明注册udAf函数
      通过spark.udf.register()来注册自定义UDAF,需要指定自定义函数名称和通过这种特殊方式: functions.udaf自定义聚合函数的类
    2、通过sql文的方式使用自定义聚合函数验证实现结果
    */
    import org.apache.spark.sql.functions._
    spark.udf.register("avgAge", functions.udaf(new MyUDAF))

    spark.sql("select avg(age) from user").show()
    //+--------+
    //|avg(age)|
    //+--------+
    //|    14.0|
    //+--------+

    spark.sql("select avgAge(age) from user").show()
    //+-----------+
    //|avgage(age)|
    //+-----------+
    //|       14.0|
    //+-----------+

    spark.stop()

  }

  case class User(id: Long, name: String, age: Long, gender: String)

  // TODO 自定义样例类,用来作为UDAF的缓冲区,存储年龄总和以及统计的个数.
  case class AvgBuffer(var sum: Long, var count: Long)

  /*
    TODO 用户自定义聚合函数定义类写法,这里以 MyUDAF类 来模拟对年龄求平均值
      1、查看自定义聚合函数的规范 Aggregator抽象类,根据它的要求重写方法
      2、在实现方法前,需要查看Aggregator类中要求的泛型,[-IN, BUF, OUT]
        从泛型的字面上和我们需要实现的功能上,我们需要考虑 函数的输入、输出以及聚合前的数据暂时存储相关问题,也就确定了这三个函数的含义
        in : 执行UDAF函数的输入,也就是查询虚拟视图的每一条结果,这里是一各个的年龄,因此给一个Long
        out: 通过UDAF函数,最终确定执行完函数的输出,这里需要年龄的平均值,Double
        BUF: 给我们的感觉很像缓冲区,当执行UDAF函数时,我们需要获取到所有的年龄,并且还需要统计年龄的个数,
        这里涉及到了两个量的存储,因此自己定义一个样例类AvgBuffer,给两个属性,分别记录年龄总和和统计年龄个数
      3、确定了泛型参数,就可以重写它的6个方法,由于当前使用的是DataFrame数据集,会在分布式的环境下执行,因此需要考虑在Driver端进行多个聚合结果的合并
 */
  class MyUDAF extends Aggregator[Long, AvgBuffer, Double] {
    override def zero: AvgBuffer = AvgBuffer(0L, 0L)

    override def reduce(buffer: AvgBuffer, age: Long): AvgBuffer = {
      // TODO 函数的输入值和缓冲区中的值进行聚合操作,这里计算年龄总和以及统计年龄出现的次数
      buffer.sum += age
      buffer.count += 1
      buffer

    }

    override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
      // TODO 分布式环境下,多个计算节点上的缓冲区的合并
      b1.sum += b2.sum //b1.sum = b1.sum + b2.sum,最终将b1返回.
      b1.count += b2.count
      b1
    }

    override def finish(reduction: AvgBuffer): Double = {
      // TODO 返回最终计算的结果
      reduction.sum / reduction.count
    }

    override def bufferEncoder: Encoder[AvgBuffer] = {
      // TODO 这里所谓的编解码,考虑的是分布式环境下,对象之间的传输问题,因此序列化和反序列化就是一种编解码的声明,固定写法
      Encoders.product
    }

    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
  }

}
