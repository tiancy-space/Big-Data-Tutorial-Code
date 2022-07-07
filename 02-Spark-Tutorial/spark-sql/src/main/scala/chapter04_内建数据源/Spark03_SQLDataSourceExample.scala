package chapter04_内建数据源

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/7/6
 */
object Spark03_SQLDataSourceExample {
  private val path: String = "./02-Spark-Tutorial/spark-sql/src/main/resources/"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark SQL data sources example")
      .master("local[*]")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    /**spark sql 对数据源的通用 读取和写入写法 */
    runBasicDataSourceExample(spark)

    //    runBasicParquetExample(spark)
    //    runParquetSchemaMergingExample(spark)
    //    runJsonDatasetExample(spark)
    //    runCsvDatasetExample(spark)
    //    runTextDatasetExample(spark)
    //    runJdbcDatasetExample(spark)

    spark.stop()
  }

  private def runBasicDataSourceExample(spark: SparkSession): Unit = {
    // $example on:通用读取和保存数据的方法,什么都不指定则默认读取的是 parquet文件.  $
    val usersDF: DataFrame = spark.read.load("./02-Spark-Tutorial/spark-sql/src/main/resources/users.parquet")
    usersDF.select("name", "favorite_color").write.save("namesAndFavColors.parquet")
    // $example off:generic_load_save_functions$
    // $example on:手动 `format()`指定读取文件的格式
    val peopleDF: DataFrame = spark.read.format("json").load("./02-Spark-Tutorial/spark-sql/src/main/resources/people.json")
    // 手动指定保存文件的格式为parquet.
    peopleDF.select("name", "age").write.format("parquet").save("namesAndAges.parquet")
    // $example off:manual_load_options$
    // $example on:manual_load_options_csv$
    val peopleDFCsv: DataFrame = spark.read.format("csv")
      .option("sep", ";")
      .option("inferSchema", "true")
      .option("header", "true")
      .load(s"$path" + "people.csv")
    // $example off:manual_load_options_csv$
    // $example on:manual_save_options_orc$
    usersDF.write.format("orc")
      .option("orc.bloom.filter.columns", "favorite_color")
      .option("orc.dictionary.key.threshold", "1.0")
      .option("orc.column.encoding.direct", "name")
      .save("users_with_options.orc")
    // $example off:manual_save_options_orc$
    // $example on:manual_save_options_parquet$
    usersDF.write.format("parquet")
      .option("parquet.bloom.filter.enabled#favorite_color", "true")
      .option("parquet.bloom.filter.expected.ndv#favorite_color", "1000000")
      .option("parquet.enable.dictionary", "true")
      .option("parquet.page.write-checksum.enabled", "false")
      .save("users_with_options.parquet")
    // $example off:manual_save_options_parquet$

    // $example on:direct_sql$
    val sqlDF = spark.sql("SELECT * FROM parquet.`./02-Spark-Tutorial/spark-sql/src/main/resources/users.parquet`")
    // $example off:direct_sql$
    // $example on:write_sorting_and_bucketing$
    peopleDF.write.bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed")
    // $example off:write_sorting_and_bucketing$
    // $example on:write_partitioning$
    usersDF.write.partitionBy("favorite_color").format("parquet").save("namesPartByColor.parquet")
    // $example off:write_partitioning$
    // $example on:write_partition_and_bucket$
    usersDF
      .write
      .partitionBy("favorite_color")
      .bucketBy(42, "name")
      .saveAsTable("users_partitioned_bucketed")
    // $example off:write_partition_and_bucket$

    spark.sql("DROP TABLE IF EXISTS people_bucketed")
    spark.sql("DROP TABLE IF EXISTS users_partitioned_bucketed")
  }

  private def runBasicParquetExample(spark: SparkSession): Unit = {
    // $example on:basic_parquet_example$
    // Encoders for most common types are automatically provided by importing spark.implicits._
    import spark.implicits._

    val peopleDF = spark.read.json(s"$path" + "people.json")

    // DataFrames can be saved as Parquet files, maintaining the schema information
    peopleDF.write.parquet("people.parquet")

    // Read in the parquet file created above
    // Parquet files are self-describing so the schema is preserved
    // The result of loading a Parquet file is also a DataFrame
    val parquetFileDF = spark.read.parquet("people.parquet")

    // Parquet files can also be used to create a temporary view and then used in SQL statements
    parquetFileDF.createOrReplaceTempView("parquetFile")
    val namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19")
    namesDF.map(attributes => "Name: " + attributes(0)).show()
    // +------------+
    // |       value|
    // +------------+
    // |Name: Justin|
    // +------------+
    // $example off:basic_parquet_example$
  }

  private def runParquetSchemaMergingExample(spark: SparkSession): Unit = {
    // $example on:schema_merging$
    // This is used to implicitly convert an RDD to a DataFrame.
    import spark.implicits._

    // Create a simple DataFrame, store into a partition directory
    val squaresDF = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
    squaresDF.write.parquet("data/test_table/key=1")

    // Create another DataFrame in a new partition directory,
    // adding a new column and dropping an existing column
    val cubesDF = spark.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube")
    cubesDF.write.parquet("data/test_table/key=2")

    // Read the partitioned table
    val mergedDF = spark.read.option("mergeSchema", "true").parquet("data/test_table")
    mergedDF.printSchema()

    // The final schema consists of all 3 columns in the Parquet files together
    // with the partitioning column appeared in the partition directory paths
    // root
    //  |-- value: int (nullable = true)
    //  |-- square: int (nullable = true)
    //  |-- cube: int (nullable = true)
    //  |-- key: int (nullable = true)
    // $example off:schema_merging$
  }

  private def runJsonDatasetExample(spark: SparkSession): Unit = {
    // $example on:json_dataset$
    // Primitive types (Int, String, etc) and Product types (case classes) encoders are
    // supported by importing this when creating a Dataset.
    import spark.implicits._

    // A JSON dataset is pointed to by path.
    // The path can be either a single text file or a directory storing text files
    val path = "examples/src/main/resources/people.json"
    val peopleDF = spark.read.json(path)

    // The inferred schema can be visualized using the printSchema() method
    peopleDF.printSchema()
    // root
    //  |-- age: long (nullable = true)
    //  |-- name: string (nullable = true)

    // Creates a temporary view using the DataFrame
    peopleDF.createOrReplaceTempView("people")

    // SQL statements can be run by using the sql methods provided by spark
    val teenagerNamesDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19")
    teenagerNamesDF.show()
    // +------+
    // |  name|
    // +------+
    // |Justin|
    // +------+

    // Alternatively, a DataFrame can be created for a JSON dataset represented by
    // a Dataset[String] storing one JSON object per string
    val otherPeopleDataset = spark.createDataset(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    val otherPeople = spark.read.json(otherPeopleDataset)
    otherPeople.show()
    // +---------------+----+
    // |        address|name|
    // +---------------+----+
    // |[Columbus,Ohio]| Yin|
    // +---------------+----+
    // $example off:json_dataset$
  }

  private def runCsvDatasetExample(spark: SparkSession): Unit = {
    // $example on:csv_dataset$
    // A CSV dataset is pointed to by path.
    // The path can be either a single CSV file or a directory of CSV files
    val path = "examples/src/main/resources/people.csv"

    val df = spark.read.csv(path)
    df.show()
    // +------------------+
    // |               _c0|
    // +------------------+
    // |      name;age;job|
    // |Jorge;30;Developer|
    // |  Bob;32;Developer|
    // +------------------+

    // Read a csv with delimiter, the default delimiter is ","
    val df2 = spark.read.option("delimiter", ";").csv(path)
    df2.show()
    // +-----+---+---------+
    // |  _c0|_c1|      _c2|
    // +-----+---+---------+
    // | name|age|      job|
    // |Jorge| 30|Developer|
    // |  Bob| 32|Developer|
    // +-----+---+---------+

    // Read a csv with delimiter and a header
    val df3 = spark.read.option("delimiter", ";").option("header", "true").csv(path)
    df3.show()
    // +-----+---+---------+
    // | name|age|      job|
    // +-----+---+---------+
    // |Jorge| 30|Developer|
    // |  Bob| 32|Developer|
    // +-----+---+---------+

    // You can also use options() to use multiple options
    val df4 = spark.read.options(Map("delimiter" -> ";", "header" -> "true")).csv(path)

    // "output" is a folder which contains multiple csv files and a _SUCCESS file.
    df3.write.csv("output")

    // Read all files in a folder, please make sure only CSV files should present in the folder.
    val folderPath = "examples/src/main/resources";
    val df5 = spark.read.csv(folderPath);
    df5.show();
    // Wrong schema because non-CSV files are read
    // +-----------+
    // |        _c0|
    // +-----------+
    // |238val_238|
    // |  86val_86|
    // |311val_311|
    // |  27val_27|
    // |165val_165|
    // +-----------+

    // $example off:csv_dataset$
  }

  private def runTextDatasetExample(spark: SparkSession): Unit = {
    // $example on:text_dataset$
    // A text dataset is pointed to by path.
    // The path can be either a single text file or a directory of text files
    val path = "examples/src/main/resources/people.txt"

    val df1 = spark.read.text(path)
    df1.show()
    // +-----------+
    // |      value|
    // +-----------+
    // |Michael, 29|
    // |   Andy, 30|
    // | Justin, 19|
    // +-----------+

    // 定义行与行的分隔符.
    // The line separator handles all `\r`, `\r\n` and `\n` by default.
    val df2 = spark.read.option("lineSep", ",").text(path)
    df2.show()
    // +-----------+
    // |      value|
    // +-----------+
    // |    Michael|
    // |   29\nAndy|
    // | 30\nJustin|
    // |       19\n|
    // +-----------+

    // 您还可以使用 'wholetext' 选项将每个输入文件作为单行读取
    val df3 = spark.read.option("wholetext", true).text(path)
    df3.show()
    //  +--------------------+
    //  |               value|
    //  +--------------------+
    //  |Michael, 29\nAndy...|
    //  +--------------------+

    // “输出”是一个文件夹，其中包含多个文本文件和一个 _SUCCESS 文件。
    df1.write.text("output")

    // 您可以使用 'compression' 选项指定压缩格式。
    df1.write.option("compression", "gzip").text("output_compressed")

    // $example off:text_dataset$
  }

  private def runJdbcDatasetExample(spark: SparkSession): Unit = {
    // $example on:jdbc_dataset$
    // Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
    // Loading data from a JDBC source
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql:dbserver")
      .option("dbtable", "schema.tablename")
      .option("user", "username")
      .option("password", "password")
      .load()

    val connectionProperties = new Properties()
    connectionProperties.put("user", "username")
    connectionProperties.put("password", "password")
    val jdbcDF2 = spark.read
      .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)
    // Specifying the custom data types of the read schema
    connectionProperties.put("customSchema", "id DECIMAL(38, 0), name STRING")
    val jdbcDF3 = spark.read
      .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)

    // Saving data to a JDBC source
    jdbcDF.write
      .format("jdbc")
      .option("url", "jdbc:postgresql:dbserver")
      .option("dbtable", "schema.tablename")
      .option("user", "username")
      .option("password", "password")
      .save()

    jdbcDF2.write
      .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)

    // Specifying create table column data types on write
    jdbcDF.write
      .option("createTableColumnTypes", "name CHAR(64), comments VARCHAR(1024)")
      .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)
    // $example off:jdbc_dataset$
  }

  case class Person(name: String, age: Long)

}
