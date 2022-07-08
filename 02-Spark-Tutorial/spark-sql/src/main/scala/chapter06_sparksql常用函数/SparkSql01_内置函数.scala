package chapter06_sparksql常用函数

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/7/8
 */

object SparkSql01_内置函数 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("spark sql 内置函数")
      .master("local[*]")
      .getOrCreate()

    /*
        TODO 1、日期相关的函数: 日期格式化、时间戳转化指定格式的日期、当前日期+-操作、两个日期间插值.
          所有的函数使用,可以查看`spark sql`的官网:https://spark.apache.org/docs/latest/api/sql/index.html
          日期格式手动指定查看的地址: https://spark.apache.org/docs/latest/sql-ref-datetime-pattern.html
     */
    // 日期格式化
    spark.sql("select to_date('20220401','yyyyMMdd')").show()
    //+---------------------------+
    //|to_date(20220401, yyyyMMdd)|
    //+---------------------------+
    //|                 2022-04-01|
    //+---------------------------+

    //date_format(timestamp, fmt) - 将`时间戳转`换为日期格式 fmt 指定格式的字符串值
    spark.sql("SELECT date_format('2016-04-08', 'y')").show
    //+--------------------------+
    //|date_format(2016-04-08, y)|
    //+--------------------------+
    //|                      2016|
    //+--------------------------+


    // date_add(start_date, num_days) - 返回 `start_date` 之后 `num_days` 的日期
    spark.sql("SELECT date_add('2016-07-30', 1)").show(10, false)
    //|date_add(2016-07-30, 1)|
    //+-----------------------+
    //|2016-07-31             |
    //+-----------------------+


    // date_sub date_sub(start_date, num_days) - 返回 `start_date` 之前 `num_days` 的日期。
    spark.sql("SELECT date_sub('2016-07-30', 1)").show()
    //+-----------------------+
    //|date_sub(2016-07-30, 1)|
    //+-----------------------+
    //|             2016-07-29|
    //+-----------------------+


    // datediff(endDate, startDate) -  返回 `endDate` - `startDate`之间的天数 .
    spark.sql("SELECT datediff('2009-07-31', '2009-07-30')").show()
    //+--------------------------------+
    //|datediff(2009-07-31, 2009-07-30)|
    //+--------------------------------+
    //|                               1|
    //+--------------------------------+

    /*
        TODO 2、unix系统时间相关.
     */
    // from_unixtime(unix_time, format) - 以指定格式返回 unix_time。
    spark.sql("SELECT from_unixtime(0, 'yyyy-MM-dd HH:mm:ss')").show()
    //+-------------------------------------+
    //|from_unixtime(0, yyyy-MM-dd HH:mm:ss)|
    //+-------------------------------------+
    //|                  1970-01-01 08:00:00|
    //+-------------------------------------+


    // from_utc_timestamp(timestamp, timezone) - 给定一个时间戳，如“2017-07-14 02:40:00.0”，将其解释为 UTC 时间，并将该时间呈现为给定时区的时间戳。 例如，“GMT+1”将产生“2017-07-14 03:40:00.0”。
    spark.sql("  SELECT from_utc_timestamp('2016-08-31', 'Asia/Seoul');").show()
    //+------------------------------------------+
    //|from_utc_timestamp(2016-08-31, Asia/Seoul)|
    //+------------------------------------------+
    //|                       2016-08-31 09:00:00|
    //+------------------------------------------+

    /*
          TODO 3、返回当前月的最后一天
     */
    // last_day(date) - 返回日期所属月份的最后一天。
    spark.sql("SELECT last_day('2009-01-12')").show()
    //+--------------------+
    //|last_day(2009-01-12)|
    //+--------------------+
    //|          2009-01-31|
    //+--------------------+

    /*
        TODO 4、炸裂函数,将一行数据,数据本身使用 Array包裹,变成 一列多行.  ==> 一列变多行,列转行. explode函数.
     */
    // explode(expr) - 将数组 expr 的元素分成多行，或将 map expr 的元素分成多行和多列。
    spark.sql("SELECT explode(array(10, 20))").show()
    //+---+
    //|col|
    //+---+
    //| 10|
    //| 20|
    //+---+

    /*
        TODO 5.1 将`json`字符串转化成指定的`schema` struct结构的 的的字符串.
     */
    // from_json(jsonStr, schema[, options])  - Returns a struct value with the given jsonStr and schema.
    spark.sql("SELECT from_json('{\"a\":1, \"b\":0.8}', 'a INT, b DOUBLE')").show()
    //+---------------------------+
    //|from_json({"a":1, "b":0.8})|
    //+---------------------------+
    //|                   {1, 0.8}|
    //+---------------------------+

    spark.sql("SELECT from_json('{\"a\":1, \"b\":0.8}', 'a INT, b DOUBLE')").printSchema()
    //root
    // |-- from_json({"a":1, "b":0.8}): struct (nullable = true)
    // |    |-- a: integer (nullable = true)
    // |    |-- b: double (nullable = true)


    spark.sql("SELECT from_json('{\"time\":\"26/08/2015\"}', 'time Timestamp', map('timestampFormat', 'dd/MM/yyyy'))").show(10, false)
    //+--------------------------------+
    //|from_json({"time":"26/08/2015"})|
    //+--------------------------------+
    //|{2015-08-26 00:00:00}           |
    //+--------------------------------+
    /*
        TODO 5.2 取出struct结构体中的每一个属性值. 如果是 struct中的某个属性,可以直接使用当前生成的临时表的列名`.`属性名的方式.数组中的每一项,则使用列名[索引位置]获取.
          具体的复杂格式的数据处理,可以参照下面的文档: https://juejin.cn/post/6844903861325430797#heading-1
          原始的json字符串: {"teacher": "Alice", "student": [{"name": "Bob", "rank": 1}, {"name": "Charlie", "rank": 2}]}
          schema 信息: 'STRUCT<teacher: STRING, student: ARRAY<STRUCT<name: STRING, rank: INT>>>'
     */

    val teacherStructDF: DataFrame = spark.sql(
      """
        |select from_json(
        |   '{"teacher": "Alice", "student": [{"name": "Bob", "rank": 1}, {"name": "Charlie", "rank": 2}]}',
        |   'Struct<teacher:String,student:Array<Struct<name:String,rank:Int>>>'
        |) as structCol
        |""".stripMargin)
    //+---------------------------------+
    //|structCol                        |
    //+---------------------------------+
    //|{Alice, [{Bob, 1}, {Charlie, 2}]}|
    //+---------------------------------+

    teacherStructDF.createOrReplaceTempView("teacher_info")
    import org.apache.spark.sql.functions._
    spark.sql(
      """
        |select structCol,
        |   structCol.teacher as teacher_name,
        |   structCol.student as studentArr,
        |   structCol.student[0],
        |   structCol.student[1],
        |   structCol.student[2]
        |from teacher_info
        |""".stripMargin).show(10, false)
    //+---------------------------------+------------+------------------------+--------------------+--------------------+--------------------+
    //|structCol                        |teacher_name|studentArr              |structCol.student[0]|structCol.student[1]|structCol.student[2]|
    //+---------------------------------+------------+------------------------+--------------------+--------------------+--------------------+
    //|{Alice, [{Bob, 1}, {Charlie, 2}]}|Alice       |[{Bob, 1}, {Charlie, 2}]|{Bob, 1}            |{Charlie, 2}        |null                |
    //+---------------------------------+------------+------------------------+--------------------+--------------------+--------------------+

    // 使用如下方式,将一列变成多行数据. 炸裂函数 + 侧写的效果.
    import spark.implicits._
    teacherStructDF.withColumn("explodeStu", explode($"structCol.student")).show(10, false)
    //+---------------------------------+------------+
    //|structCol                        |explodeStu  |
    //+---------------------------------+------------+
    //|{Alice, [{Bob, 1}, {Charlie, 2}]}|{Bob, 1}    |
    //|{Alice, [{Bob, 1}, {Charlie, 2}]}|{Charlie, 2}|
    //+---------------------------------+------------+


    /*
      TODO 6、get_json_object(json_txt, path) - 从指定路径中获取jsonObject的对象. get_json_object(json_txt,'$.jsonObject的属性值')
        原始json格式数据: {"programmers":{"firstName":"Brett","lastName":"McLaughlin","email":"aaaa"},"authors":{"firstName":"Isaac","lastName":"Asimov","genre":"science fiction"}}
     */
    spark.sql("SELECT get_json_object('{\"programmers\":{\"firstName\":\"Brett\",\"lastName\":\"McLaughlin\",\"email\":\"aaaa\"},\"authors\":{\"firstName\":\"Isaac\",\"lastName\":\"Asimov\",\"genre\":\"science fiction\"}}', '$.programmers') as res").show(10, false)
    //+------------------------------------------------------------+
    //|res                                                         |
    //+------------------------------------------------------------+
    //|{"firstName":"Brett","lastName":"McLaughlin","email":"aaaa"}|
    //+------------------------------------------------------------+

    /*
        TODO 7、json_tuple('json串',属性值,属性值...)
     */
    spark.sql("SELECT json_tuple('{\"a\":1, \"b\":2}', 'a', 'b');").show()

    /*
      TODO 8、to_json(expr[, options]) - 返回具有给定结构值的 json 字符串
     */
    spark.sql("select to_json(named_struct('a', 1, 'b', 2))").show()
    //+---------------------------------+
    //|to_json(named_struct(a, 1, b, 2))|
    //+---------------------------------+
    //|                    {"a":1,"b":2}|
    //+---------------------------------+

    spark.sql("select to_json(named_struct('time', to_timestamp('2015-08-26', 'yyyy-MM-dd')), map('timestampFormat', 'dd/MM/yyyy'))").show(10, false)
    //+-----------------------------------------------------------------+
    //|to_json(named_struct(time, to_timestamp(2015-08-26, yyyy-MM-dd)))|
    //+-----------------------------------------------------------------+
    //|{"time":"26/08/2015"}                                            |
    //+-----------------------------------------------------------------+

    spark.sql(
      """
        |SELECT to_json(array(named_struct('a', 1, 'b', 2)))
        |""".stripMargin).show(10, false)
    //+----------------------------------------+
    //|to_json(array(named_struct(a, 1, b, 2)))|
    //+----------------------------------------+
    //|[{"a":1,"b":2}]                         |
    //+----------------------------------------+


    spark.sql("select to_json(map('a', named_struct('b', 1)))").show()
    //+-----------------------------------+
    //|to_json(map(a, named_struct(b, 1)))|
    //+-----------------------------------+
    //|                      {"a":{"b":1}}|
    //+-----------------------------------+


    spark.sql("SELECT to_json(map(named_struct('a', 1),named_struct('b', 2)))").show(10, false)
    //+----------------------------------------------------+
    //|to_json(map(named_struct(a, 1), named_struct(b, 2)))|
    //+----------------------------------------------------+
    //|{"[1]":{"b":2}}                                     |
    //+----------------------------------------------------+


    spark.sql("SELECT to_json(map('a', 1))").show()
    //+------------------+
    //|to_json(map(a, 1))|
    //+------------------+
    //|           {"a":1}|
    //+------------------+


    spark.sql("SELECT to_json(array((map('a', 1))))").show()
    // +-------------------------+
    //|to_json(array(map(a, 1)))|
    //+-------------------------+
    //|                [{"a":1}]|
    //+-------------------------+


    // TODO 9、数组相关操作.
    //数组 array(expr, ...) - 返回给定值组成的数组。
    spark.sql("select array(1,3,4)").show()
    //+--------------+
    //|array(1, 3, 4)|
    //+--------------+
    //|     [1, 3, 4]|
    //+--------------+

    // array_contains(array, value) - 如果数组包含了 value，则返回 true。
    spark.sql("select array_contains(array(1,3,4),1)").show()
    //+---------------------------------+
    //|array_contains(array(1, 3, 4), 1)|
    //+---------------------------------+
    //|                             true|
    //+---------------------------------+

    // size size(expr) - 返回数组或映射的大小。如果为 null，则返回 -1。
    spark.sql("SELECT size(array('b', 'd', 'c', 'a'))")

    // sort_array(array[, ascendingOrder]) - 根据数组元素的自然顺序对输入数组进行升序或降序排序。
    spark.sql("SELECT sort_array(array('b', 'd', 'c', 'a'), true)").show()
    //+-----------------------------------+
    //|sort_array(array(b, d, c, a), true)|
    //+-----------------------------------+
    //|                       [a, b, c, d]|
    //+-----------------------------------+

    // collect_list(expr) - 收集并返回非唯一元素列表。 多行数据经过 group by + collect_list ==> 一列数据. 多行转一列. 行转列操作.
    // spark.sql("select a,collect_list(b) from table group a ")
    //

    // collect_set(expr) - 收集并返回唯一元素列表。
    // spark.sql("select a,collect_set(b) from table group a ")

    // TODO 10、map操作
    //map(key0, value0, key1, value1, ...) - 使用给定的键值对创建映射
    spark.sql("SELECT map(1.0, '2', 3.0, '4')").show()
    //+--------------------+
    //| map(1.0, 2, 3.0, 4)|
    //+--------------------+
    //|{1.0 -> 2, 3.0 -> 4}|
    //+--------------------+

    // map_keys(map) - 返回包含map key 的无序数组
    spark.sql("SELECT map_keys(map(1, 'a', 2, 'b'))")
    // map_values(map) - 返回包含map values 的无序数组.
    spark.sql("SELECT map_values(map(1, 'a', 2, 'b'))")

    // TODO  11、struct的使用.
    // struct(col1, col2, col3, ...) - 创建具有给定字段值的结构。
    spark.sql("select struct(1 as a, 2 as b) s")
    //

    // coalesce(expr1, expr2, ...) - 返回第一个非空参数（如果存在）。 否则，返回 null。
    spark.sql("SELECT coalesce(NULL, 1, NULL)") // 1


    spark.stop()
  }
}
