package chapter02

import org.apache.spark.sql.{DataFrame, SparkSession}


/**
 * @Description: DataFrame的使用. 首先要获取到当前数据源的df对象.
 * @Author: tiancy
 * @Create: 2022/6/28
 */
object SparkSql03_SparkSql使用 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("spark sql")
      .getOrCreate()

    // 通过spark sql的上下文对象,将文件读取到本地,并注册成一张临时表`json`
    val jsonDF: DataFrame = spark.read.json("./02-Spark-Tutorial/data/tweets.txt/")
    // 通过spark执行 spark sql
    /*
      Spark SQL中的临时视图属于会话范围，如果创建临时视图的会话终止，临时视图将消失。
      如果希望在所有会话之间共享临时视图，并在Spark应用程序终止之前保持活动状态，则可以创建全局临时视图`createOrReplaceTempView`。
      全局临时视图绑定到系统保留的数据库global_temp，我们必须使用限定名称来引用它，例如从Global_temp
     */
    jsonDF.createGlobalTempView("json")
    spark.sql(
      """
        |select *
        |from global_temp.json
        |""".stripMargin).show()

    spark.sql(
      """
        |select count(1) from global_temp.json
        |""".stripMargin).show()

    // 全局临时视图是跨会话的
    val totalCtn: Long = spark.newSession().sql("select * from global_temp.json ").count()
    println(s"当前表中总行数为: $totalCtn")

    // 为当前的DataFrame对象中的数据,创建临时视图,并且当前临时表的生命周期和当前的SparkSession绑定.
    jsonDF.createOrReplaceTempView("ss")
    spark.sql(
      """
        |select * from ss where id = 572692378957430785
        |""".stripMargin).show()
    // 下面这种方式会找不到表.Table or view not found: ss; 并且在写SQL文中,字符串并不是强制使用单引号的,但是习惯会加,方便做间隔停顿.
    spark.newSession().sql(
      """
        |select * from ss where user = 'Srkian_nishu'
        |""".stripMargin).show()

    // TODO spark sql 使用步骤: 先读取数据源形成 DataFrame对象,在将当前对象中的数据,注册成一张临时表. 再通过 sparkSession对象调用 .sql,写SQL文执行.
    spark.stop()
  }
}
