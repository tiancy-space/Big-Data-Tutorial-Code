package chapter06_sparksql常用函数

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, StructField, StructType}


/**
 * @Description: 自定义UDAF, 3.0版本以前的弱类型.
 * @Author: tiancy
 * @Create: 2022/7/11
 */
object SparkSql05_UDAF_Before_弱类型 {
  /*
    TODO 在使用 UDAF 时,需要注意版本.
      Spark3.0以前的SQL文中,不能使用强类型的UDAF,可以使用弱类型.
      Spark3.0以后,可以使用强类型的Aggregate方式来进行SQL文的自定义聚合.
   */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("自定义UDAF")
      .getOrCreate()


    val df: DataFrame = spark.read.json("./02-Spark-Tutorial/data/user.json")
    df.show(10, false)
    //+---+--------+
    //|age|username|
    //+---+--------+
    //|20 |zhangsan|
    //|21 |lisi    |
    //|1  |wangwu  |
    //+---+--------+

    df.createOrReplaceTempView("user")
    // 注册自定义UDAF函数.
    spark.udf.register("avgAge", new MyAvgAgeUDAF)
    // 在 sql 中使用.
    spark.sql("select avgAge(age) from user").show
    spark.stop()
  }

   /*
      TODO 自定义聚合函数（UDAF）(弱类型) 实现功能: 求平均值.
       1. 继承类 UserDefinedAggregateFunction
       2. 重写方法 8个 = 4个 + 4个
       输入数据schema、缓冲区schema、输出类型、初始化缓冲区
       更新缓冲区、合并缓冲区以及 最终计算`evaluate`
    */
  class MyAvgAgeUDAF extends UserDefinedAggregateFunction {

    // TODO 定义输入数据 schema,也就是表中字段名称以及字段值的类型.
    override def inputSchema: StructType = {
      StructType(
        Array(
          StructField("age", LongType)
        )
      )
    }

    // TODO 缓冲区的数据结构,这里主要和待实现的功能相关,eg: 实现求平均值,需要两个参数 当前列的总和以及总行数
    override def bufferSchema: StructType = {
      StructType(
        Array(
          StructField("sum", LongType),
          StructField("cnt", LongType)
        )
      )
    }

    // TODO 输出数据的类型,一行数据 Long类型.
    override def dataType: DataType = {
      LongType
    }

    // TODO 稳定性
    override def deterministic: Boolean = true

    // TODO 初始化缓冲区数据
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer.update(0, 0L)
      buffer.update(1, 0L)
    }

    // TODO 使用输入的值更新缓冲区数据
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer.update(0, buffer.getLong(0) + input.getLong(0))
      buffer.update(1, buffer.getLong(1) + 1)
    }

    // TODO 合并缓冲区
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1.update(0, buffer1.getLong(0) + buffer2.getLong(0))
      buffer1.update(1, buffer1.getLong(1) + buffer2.getLong(1))
    }

    // TODO 计算结果
    override def evaluate(buffer: Row): Any = {
      buffer.getLong(0) / buffer.getLong(1)
    }
  }

}
