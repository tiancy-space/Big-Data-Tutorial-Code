package chapter02

import org.apache.spark.sql.{DataFrame, SparkSession}


/**
 * @Description: DataFrame的使用. 首先要获取到当前数据源的df对象. DSL语法: 通过面向对象的方式写SQL.
 * @Author: tiancy
 * @Create: 2022/6/28
 */
object SparkSql02_DF调用方法 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession
      .builder()
      // .config() ,一组 key-value类型的字符串,主要通过这些配置项,开启一些系统配置.
      .appName("spark sql SparkSession")
      .master("local[*]")
      .getOrCreate()

    val path: String = System.getProperties.getProperty("user.dir")
    println(path)

    // SparkSession对象调用read方法.来读取指定格式的本地数据.
    val tweetsJsonDF: DataFrame = spark.read.json("./02-Spark-Tutorial/data/tweets.txt")
    /*
      展示当前DF中的数据,是一张Schema.
      +-------------+------------------+-----------+-------------------------------------------------------------------------------------------------------------------------------------------+----------------+
      |country      |id                |place      |text                                                                                                                                       |user            |
      +-------------+------------------+-----------+-------------------------------------------------------------------------------------------------------------------------------------------+----------------+
      |India        |572692378957430785|Orissa     |@always_nidhi @YouTube no i dnt understand bt i loved the music nd their dance awesome all the song of this mve is rocking                 |Srkian_nishu    |
      |United States|572575252020109313|Vienna     |idk why people hate being stuck in traffic if theres no rush to get somewhere like traffic is an excuse to think and listen to music       |someone actually|
      |Indonesia    |572647831053312000|Mario Riawa|Serasi ade haha @AdeRais "@SMTOWNGLOBAL: #SHINee ONEW(@skehehdanfdldi) and #AMBER(@llama_ajol) at KBS ‘Music Bank’. http://t.co/ZhUKPhk8am"|Rinie Syamsuddin|
      |United States|572647841220337664|Norwalk    |@BeezyDH_ it’s like one of us got locked up… It’s all music now                                                                            |Cas             |
      +-------------+------------------+-----------+-------------------------------------------------------------------------------------------------------------------------------------------+----------------+
     */
    tweetsJsonDF.show(false)

    /*
      可以通过当前DF对象的printSchema方法,获取当前DF的 schema对象中的值,也就是表头信息. 表中字段名称、字段类型、当前字段是否可以存在null值.
          root
           |-- country: string (nullable = true)
           |-- id: string (nullable = true)
           |-- place: string (nullable = true)
           |-- text: string (nullable = true)
           |-- user: string (nullable = true)
      */
    tweetsJsonDF.printSchema()

    // 在进行Spark中数据格式转化过程中,需要使用隐式转换. Spark版本升级过程中,和以前的代码兼容.也为了将RDD这种类型转化为弱类型的DF. DF = RDD[Row] + Schema
    import spark.implicits._

    // 也可以直接使用df对象,调用方法的方式查询schema中的数据. eg: 查询指定列.
    tweetsJsonDF.select("user", "id", "place").show()

    // 还可以在指定的列上,进行数据操作.
    tweetsJsonDF.select($"user", $"id" + 15, $"country").show(false)

    //可以直接使用 filter()方法进行数据过滤.
    tweetsJsonDF.select($"user",$"id").filter("user = 'Cas' ").show()

    // 直接使用df对象,对当前df内的 schema 进行条数统计
    val totalCount: Long = tweetsJsonDF.select($"id", $"user", $"country", $"place").count()
    println(totalCount)

    spark.stop()
  }
}
