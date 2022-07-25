package util

import org.apache.spark.sql.SparkSession

/**
 * Hive的常用配置参数
 */
object HiveUtil {

  /**
    * 调大最大分区个数
    * @param spark
    * @return
    */
   def setMaxpartitions(spark: SparkSession)={
     spark.sql("set hive.exec.dynamic.partition=true")
     spark.sql("set hive.exec.dynamic.partition.mode=nonstrict")
     spark.sql("set hive.exec.max.dynamic.partitions=100000")
     spark.sql("set hive.exec.max.dynamic.partitions.pernode=100000")
     spark.sql("set hive.exec.max.created.files=100000")
   }
  /**
    * 开启压缩
    *
    * @param spark
    * @return
    */
  def openCompression(spark: SparkSession) = {
    spark.sql("set mapred.output.compress=true")
    spark.sql("set hive.exec.compress.output=true") // spark默认支持snappy.
  }

  /**
    * 开启动态分区，非严格模式 : 主要实现的效果 在sql执行时,根据查询到的数据,动态插入到指定的分区中. 执行期动态导入历史数据的作用.
    *
    * @param spark
    */
  def openDynamicPartition(spark: SparkSession) = {
    spark.sql("set hive.exec.dynamic.partition=true")
    spark.sql("set hive.exec.dynamic.partition.mode=nonstrict")
  }

  /**
    * 使用lzo压缩
    *
    * @param spark
    */
  def useLzoCompression(spark: SparkSession) = {
    spark.sql("set io.compression.codec.lzo.class=com.hadoop.compression.lzo.LzoCodec")
    spark.sql("set mapred.output.compression.codec=com.hadoop.compression.lzo.LzopCodec")
  }

  /**
    * 使用snappy压缩
    * @param spark
    */
  def useSnappyCompression(spark:SparkSession)={
    spark.sql("set mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec");
    spark.sql("set mapreduce.output.fileoutputformat.compress=true")
    spark.sql("set mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec")
  }

}
