package chapter04_内建数据源

import org.apache.spark.SparkContext

/**
 * @Description: 演示内建数据源的使用.
 * @Author: tiancy
 * @Create: 2022/7/5
 */
object Spark01_内建数据源_读写文件步骤 {
  /*
      TODO 通过spark sql操作数据源.
        1、SparkSQL内建支持的数据源包括：Parquet、JSON、CSV、Avro、Images、BinaryFiles（Spark 3.0）。其中Parquet是默认的数据源。
        2、包括以前使用的读取文件的方式: spark.read.json,它的底层调用的还是 spark.read.format().load().
        3、对于spark的DataFrame来讲:都存在共性的方法,读取load、保存save.具体的写法如下 :
        - spark.read.formate("读取的文件格式").load("文件路径")
        - df.write.format("保存的文件格式,如果不指定,则默认为parquet").save("保存的位置")
        4、数据保存时,指定SaveMode以及SaveMode的选择.

   */
  def main(args: Array[String]): Unit = {

    import org.apache.spark.sql._
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("内建数据源的使用").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // 声明读取 csv类型的数据格式,是否将第一行数据作为表头、每个字段之间分隔符、是否开启类型转化.
    val csvMapOps = Map("header" -> "true", "delimiter" -> ",", "inferSchema" -> "true")
    // spark.read.cvs()底层的实现也是 spark.read.format().load() ==> def csv(paths: String*): DataFrame = format("csv").load(paths : _*)
    val addressInfoDF: DataFrame = spark.read.options(csvMapOps).csv("./02-Spark-Tutorial/data/address_info.csv")
    addressInfoDF.show(20, false)

    // 读取一个文件,并保存到指定位置上.
    val peopleDF: DataFrame = spark.read.format("json").load("./02-Spark-Tutorial/data/people.json")
    peopleDF.show(10, false)

    peopleDF.write.save("./02-Spark-Tutorial/data/people.parquet")

    /*
      Save Mode选择与使用.
      SaveMode.ErrorIfExists (默认)	如果目标位置已经存在数据，那么抛出一个异常
      SaveMode.Append	如果目标位置已经存在数据，那么将数据追加进去
      SaveMode.Overwrite	如果目标位置已经存在数据，那么就将已经存在的数据删除，用新数据进行覆盖
      SaveMode.Ignore	如果目标位置已经存在数据，那么就忽略，不做任何操作。
     */
    peopleDF.write.mode(SaveMode.Append).save("./02-Spark-Tutorial/data/people.parquet")

    sc.stop()
    spark.stop()
  }
}
