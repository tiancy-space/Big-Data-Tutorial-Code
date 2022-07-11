package chapter06_sparksql常用函数

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.matching.Regex

/**
 * @Description: 自定义UDF
 * @Author: tiancy
 * @Create: 2022/7/11
 */
object SparkSql04_UDF {
  /*
    TODO 1、自定义函数 UDF: 输入和输出都是一行操作. 给每个名字加一个前缀等操作.并且自定义UDF函数和Scala中定义的函数没有什么区别,仅仅多一部:注册 spark.udf.register()
   */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("自定义UDF,实现字段内容长度判断")
      .getOrCreate()

    // 定义一个udf,用来判断但前行中数据长度.直接使用一个匿名函数的方式,直接写就好.
    spark.udf.register("strLen", (vale: String) => vale.length)

    import spark.implicits._
    val pageViewDF: DataFrame = spark
      .sparkContext
      .makeRDD(
        List(
          ("2018-01-01", 1, "www.baidu.com", "10:01"),
          ("2018-01-01", 1, "www.sina.com", "10:01"),
          ("2018-01-01", 1, "www.sina.com", "10:01"),
          ("2018-01-01", 2, "www.baidu.com", "10:01"),
          ("2018-01-01", 3, "www.baidu.com", "10:01"),
          ("2018-01-01", 3, "www.baidu.com", "10:01")
        )
      )
      .toDF("day", "user_id", "page_id", "time")
    pageViewDF.createOrReplaceTempView("page_view")

    spark.sql(
      """
        |select strLen(page_id) as strLen
        |from page_view
        |""".stripMargin).show(10, false)

    /*
      TODO 2、编写的UDF可以放到SQL语句的fields部分，也可以作为where、groupBy或者having子句的一部分。也可以在使用UDF时，传入常量而非表的列名
        eg: 定义一个自定义UDF,实现功能: 判断当前字符串长度是否大于10 ? 成立 true,否则 false;
        自定义函数使用位置,除了对当前查询的表中字段值进行处理,还可以出现在 where 、groupBy、Having中作为条件使用.
     */
    spark.udf.register("isLengthBoolean", (strValue: String, len: Int) => strValue.length < len)

    spark.sql(
      """
        |select * from page_view
        |where isLengthBoolean('aaaaaaaaaaaaaa',10)
        |""".stripMargin).show(10, false)

    /*
      TODO 3、在 DataFrame中使用 自定义UDF函数. 不需要注册,而是需要按照下面的语法:将函数传递
        1、import org.apache.spark.sql.functions._
        2、val userNamePrrFix: UserDefinedFunction = udf((str: String, prefix: String) => s"$prefix == $str")
        3、在DataFrame中使用.
     */
    import org.apache.spark.sql.functions._
    // 定义一个函数,用于在 DataFrame中使用. 需要导入 spark.sql.functions._ 下的所有的包.
    val userNamePrrFix: UserDefinedFunction = udf((str: String, prefix: String) => s"$prefix == $str")
    pageViewDF.select(userNamePrrFix($"page_id", lit("address"))) show(10, false)
    //+------------------------+
    //|UDF(page_id, address)   |
    //+------------------------+
    //|address == www.baidu.com|
    //|address == www.sina.com |
    //|address == www.sina.com |
    //|address == www.baidu.com|
    //|address == www.baidu.com|
    //|address == www.baidu.com|
    //+------------------------+

    /*
      TODO 4、udf的使用场景
        1、校验身份证号是否合法: 可以参考当前包中: `CheckIdCardUDF`
        2、隐藏手机号中间四位
        3、 手机号转虚拟号码
        4、数据格式根据业务进行复杂校验
        5、UDF的本质: 将公司的业务逻辑封装成一段代码(数据格式校验、数据本身进行map[男 -> 1、女 -> 0]、复杂bitMap进行位运算),简化sql.
     */

    // 2.隐藏手机号  外卖 138****5678
    def hidePhone(phone: String): String = {
      // 先去除手机号的空格.再截取 [0,3) 和 [7,末尾]
      val trimPhone: String = phone.replace(" ", "")
      trimPhone.substring(0, 3) + "****" + trimPhone.substring(7)
    }

    spark.udf.register("hidePhone", hidePhone _)
    spark.sql("select hidePhone('138 8888 5678')").show()
    //+------------------------+
    //|hidePhone(138 8888 5678)|
    //+------------------------+
    //|             138****5678|
    //+------------------------+

    // 校验手机的功能 正则表达式
    // "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))[0-9]{8}$".r

    def isMobileNumber(number: String): Boolean = {
      val regex: Regex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))[0-9]{8}$".r
      val len: Int = number.length
      regex.findFirstMatchIn(number.slice(len - 11, len)).isDefined
    }

    spark.udf.register("isMobileNumber", isMobileNumber _)
    spark.sql("select isMobileNumber(17718312345)").show(10, false)
    //+---------------------------+
    //|isMobileNumber(17718312345)|
    //+---------------------------+
    //|true                       |
    //+---------------------------+


    spark.stop()
  }
}
