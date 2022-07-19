package spark

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import util.ReadConfigPropUtil

/**
 * @Description: 定义一个类,用来通过Spark读取数据源或者将DF、DS写入到到指定的位置上.
 * @Author: tiancy
 * @Create: 2022/7/18
 */
object SparkIO {

  // 从本地文件读取数据
  private val rootPath: String = System.getProperties.getProperty("user.dir")


  /**
   * 定义读取 `csv`格式的数据集的方法 readCsv
   *
   * @param childPath          指定文件位置
   * @param fileName           指定文件名称
   * @param schema             手动定义表结构
   * @param defaultDelimiter   指定文件的分隔符,是一个默认参数,可以不传,默认是`,`
   * @param defaultHeader      指定读取csv格式的文件,是否包含表头,默认包含.可以通过参数控制. false
   * @param defaultInferSchema 指定是否开启类型推断. 默认值为 true,可以手动关闭: false
   * @return
   */
  def readCsv(spark: SparkSession, childPath: String, fileName: String, schema: StructType, defaultDelimiter: String = ",", defaultHeader: String = "true", defaultInferSchema: String = "true"): DataFrame = {
    val csvMapOps = Map("header" -> s"$defaultHeader", "delimiter" -> s"$defaultDelimiter", "inferSchema" -> s"$defaultInferSchema")
    spark.read.format("csv").schema(schema).options(csvMapOps).load(s"$rootPath/$childPath/$fileName")
  }

  /**
   * 使用spark通过jdbc的方式,读取MySql中指定的表,返回DF.
   *
   * @param spark          spark sql 上下文环境
   * @param mysqlTableName 表名
   * @return
   */
  def readMySqlByJDBC(spark: SparkSession, mysqlTableName: String): DataFrame = {
    val jdbcMap = Map(
      "url" -> ReadConfigPropUtil("jdbc.url"),
      "driver" -> ReadConfigPropUtil("jdbc.driver"),
      "user" -> ReadConfigPropUtil("jdbc.user"),
      "password" -> ReadConfigPropUtil("jdbc.password"),
      "dbtable" -> mysqlTableName
    )
    // 需要通过 options 指定: url、driver、user、password、表名
    spark.read.format("jdbc").options(jdbcMap).load()
  }
}


