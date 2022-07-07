package chapter04_内建数据源

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description: 演示内建数据源的使用. Parquet的理解以及使用.
 * @Author: tiancy
 * @Create: 2022/7/5
 */
object Spark02_Parquet理解与使用 {
  /*
      TODO Parquet格式的数据,列式存储 + 支持嵌套结构内容存储
        1、回顾行列存储的优缺点:
          行存储: 查询某个数据块,比如 1 - 100 所有字段的数据,检索查询效率可以,缺点: 多个字段数据类型不统一,压缩比不高.
          列存储: 按照每列进行存储,都是相同类型的数据,压缩比很高,适合做聚合操作,也适合查询指定的某几个列,检索和加载数据很快. 缺点: 不支持hadoop系统的快速响应.
        2、列式存储优势,正式点的描述
          2.1. 指定查询的列,跳过不符合条件的数据,只读取明确的列,降低IO
          2.2. 每个列中字段类型相同,压缩比高,节省磁盘空间.
          2.3. 只读取需要的列，支持向量运算，能够获取更好的扫描性能
        3、Parquet读取和写入
        4、spark sql `parquet`类型的分区文件 自动进行分区推断 : Spark SQL中的Parquet数据源，支持自动根据目录名推断出分区信息. 仅仅支持推断出: 数字和字符串类型的列.其他的推断不出来. 新版本可以推断出: Data类型.
        5、spark sql `parquet文件` 开启元数据的合并操作.
        6、使用parquet面临的两个问题 :
          6.1、Parquet类型冲突问题剖析 : 同一个文件中相同字段数据类型不一致问题.一个是String,一个是BigInt  "user_id":"8482158" . "user_id":8482158
          6.2、 Parquet嵌套结构重复列问题剖析 :如果外层JSON有重复列,Spark 读取JSON会直接报错.
          对于嵌套JSON导致的重复列问题,会导致Spark无法读取数据,我们可以在Spark read json指定明确的Schema来解决此问题
        7、参数 Default	含义
        spark.sql.optimizer.nestedSchemaPruning.enabled	 false	 默认false,设置为true时可开启嵌套结构列裁剪
        spark.sql.parquet.filterPushdown	 true	 开启paruqet结构谓词下推
        spark.sql.parquet.compression.codec	 snappy	 设置paruqet的压缩格式,支持none, uncompressed, snappy, gzip, lzo, brotli, lz4, zstd.
   */
  def main(args: Array[String]): Unit = {


    import org.apache.spark.sql._
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("内建数据源的使用").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // TODO 3、Parquet格式的数据读取和写入.
    val peopleParquetDF: DataFrame = spark.read.format("parquet").load("./02-Spark-Tutorial/data/people.parquet")
    peopleParquetDF.show(20, false)

    // 3.1、如果不指定读取文件的格式,默认读取的也是 parquet 格式的文件.
    val defaultLoadDF: DataFrame = spark.read.load("./02-Spark-Tutorial/data/people.parquet")
    defaultLoadDF.show(20, false)

    // 3.2、读取指定位置上的文件,并生成临时视图来查询.
    spark.sql(
      """
        |create or replace temporary view people
        |using parquet
        |options(path "./02-Spark-Tutorial/data/people.parquet")
        |""".stripMargin) // 读取指定位置上的文件,并生成临时视图.


    println("读取指定位置上的文件并创建临时视图,通过sql查询临时视图 ========== ")
    val peopleViewDF: DataFrame = spark.sql(
      """
        |select distinct name,age from people
        |""".stripMargin)


    // 3.3 parquet文件格式的数据,写出并指定文件格式.默认的是 parquet,指定压缩方式为 snappy.
    peopleViewDF.write.mode(SaveMode.Append).option("compression", "snappy").save("./02-Spark-Tutorial/data/people.snappy")

    /*
      TODO 4、SparkSql的分区推断.
        表分区是Hive等系统中使用的一种常见优化方法。在分区表中，数据通常存储在不同的目录中，分区列值编码在每个分区目录的路径中。所有内置文件源（包括Text/CSV/JSON/ORC/Parquet）都能够自动发现和推断分区信息。
        Notice : 分区列的数据类型是自动推断的。目前，支持数字数据类型、日期、时间戳和字符串类型。老版本仅仅支持: 字符串和数字类型.
        spark sql中分区推断默认是开启的,可以通过参数: `spark.sql.sources.partitionColumnTypeInference.enabled` 来设置,如果指定关闭,字符串类型将用于分区列。
     */


    /*
        TODO 5、Parquet 合并元数据
          用户可以在一开始就定义一个简单的元数据，然后随着业务需要，逐渐往元数据中添加更多的列.
          在添加多个列的过程中,会产生多个 parquet 文件,但是多个文件之间的元数据是兼容的,因此可以指定元数据的合并操作.
          并且元数据的合并操作是一个重的操作,不是一种必要的特性,默认是关闭的,可以通过参数指定开启.
          开启元数据合并选项可以在如下位置:
            - 读取文件时,指定option : spark.read.option("mergeSchema", "true")
            - 使用spark执行sql时 : spark.sql("set spark.sql.parquet.mergeSchema=true")
            - 全局配置,在声明环境时,spark.builder().config("spark.sql.parquet.mergeSchema","true")
     */
    runParquetSchemaMergingExample(spark)

    /*
        TODO 6、Parquet类型冲突问题剖析
          文件名称:  ./02-Spark-Tutorial/spark-sql/src/main/resources/game1.json
     */
    // ./02-Spark-Tutorial/spark-sql/src/main/resources/game/game1.json
    spark.read.json("./02-Spark-Tutorial/spark-sql/src/main/resources/game/game1.json").write.parquet("./data/parquet/raw/5/")
    spark.read.json("./02-Spark-Tutorial/spark-sql/src/main/resources/game/game2.json").write.mode("append").parquet("./data/parquet/raw/5/")

    spark.read.json("./02-Spark-Tutorial/spark-sql/src/main/resources/game/game3.json").write.mode("append").parquet("./data/parquet/raw/6/")

    spark.read.option("mergeSchema", "true").parquet("./data/parquet/raw/6/").printSchema()



    sc.stop()
    spark.stop()
  }

  /**
   * spark sql parquet文件格式的元数据合并操作演示.
   *
   * @param spark
   */
  private def runParquetSchemaMergingExample(spark: SparkSession): Unit = {
    // $example on:schema_merging$
    // This is used to implicitly convert an RDD to a DataFrame.
    import spark.implicits._

    // Create a simple DataFrame, 存储到一个分区目录中.
    val squaresDF: DataFrame = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
    squaresDF.show(10, false)
    squaresDF.write.parquet("./02-Spark-Tutorial/out/test_table/key=1")

    // Create another DataFrame in a new partition directory,
    // 添加新的列并删除现有的列.
    val cubesDF: DataFrame = spark.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube")
    cubesDF.show(10, false)
    cubesDF.write.parquet("./02-Spark-Tutorial/out/test_table/key=2")

    // 读取分区表,并指定开启元数据合并操作.
    val mergedDF: DataFrame = spark.read.option("mergeSchema", "true").parquet("./02-Spark-Tutorial/out/test_table/")
    mergedDF.printSchema()
    // The final schema consists of all 3 columns in the Parquet files together
    // with the partitioning column appeared in the partition directory paths
    // root
    //  |-- value: int (nullable = true)
    //  |-- square: int (nullable = true)
    //  |-- cube: int (nullable = true)
    //  |-- key: int (nullable = true)
    // $example off:schema_merging$

    mergedDF.show(20, false)
    //+-----+------+----+---+
    //|value|square|cube|key|
    //+-----+------+----+---+
    //|1    |1     |null|1  |
    //|2    |4     |null|1  |
    //|4    |16    |null|1  |
    //|5    |25    |null|1  |
    //|3    |9     |null|1  |
    //|6    |null  |216 |2  |
    //|7    |null  |343 |2  |
    //|8    |null  |512 |2  |
    //|9    |null  |729 |2  |
    //|10   |null  |1000|2  |
    //+-----+------+----+---+
  }


}
