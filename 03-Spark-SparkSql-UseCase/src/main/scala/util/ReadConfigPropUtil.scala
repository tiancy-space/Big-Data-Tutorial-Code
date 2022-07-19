package util

import java.util.ResourceBundle

/**
 * 定义一个读取配置文件`config.properties`下的配置项的值.
 */
object ReadConfigPropUtil {

  val config: ResourceBundle = ResourceBundle.getBundle("config")

  /**
   * 读取配置文件`config.properties`文件中指定key的value 工具类.具体使用如下
   * {{{
   *    val url: String = ConfigUtil("jdbc.url")
   *    println(url) // jdbc:mysql://hadoop202:3306/spark-sql
   * }}}
   */
  def apply(key: String): String = {
    config.getString(key)
  }

  def main(args: Array[String]): Unit = {
    val url: String = ReadConfigPropUtil("jdbc.url")
    println(url) // jdbc:mysql://hadoop202:3306/spark-sql
  }
}