package chapter05_外部数据源

import java.util.ResourceBundle

object ConfigUtil {

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
    val url: String = ConfigUtil("jdbc.url")
    println(url) // jdbc:mysql://hadoop202:3306/spark-sql
  }
}