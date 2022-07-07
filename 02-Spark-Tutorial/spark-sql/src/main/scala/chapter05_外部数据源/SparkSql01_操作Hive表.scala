package chapter05_外部数据源

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * @Description: 使用spark sql 操作 hive .
 * @Author: tiancy
 * @Create: 2022/7/6
 */
object SparkSql01_操作Hive表 {
  def main(args: Array[String]): Unit = {

    /*
        TODO 使用步骤:
          1、先将自己的Hive配置拷贝到当前项目下的resources中. 我的Hive的配置文件位置: `/opt/module/hive-3.1.2/conf/hive-site.xml`
          2、开启hadoop集群
          3、在声明的SparkSession时,指定Hive支持.
     */

    val spark: SparkSession = SparkSession
      .builder()
      .appName("spark sql operator Hive")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql._

    spark.sql("show databases").show()

    spark.sql("use spark_sql_db")

    spark.sql("show tables").show()
    /*
        TODO 注意: Hive SQL 和 Spark sql 中语法存在差异,例如下面这种: HQL可以通过下面这种方式解析 array、map、struct. 而 SparkSQL这种语法则解析出现问题.
          可以推断出每个字段的类型,却不能直接获取到具体指定的属性值. ==> 通过结果来看,是通过Hive建表时,指定的 array、map、struct这种结构,每个属性之间的分隔符元数据信息读不到.
     */
    spark.table("ds_data.employees").printSchema()
    //root
    // |-- name: string (nullable = true)
    // |-- salary: float (nullable = true)
    // |-- subordinates: array (nullable = true)
    // |    |-- element: string (containsNull = true)
    // |-- deductions: map (nullable = true)
    // |    |-- key: string
    // |    |-- value: float (valueContainsNull = true)
    // |-- address: struct (nullable = true)
    // |    |-- street: string (nullable = true)
    // |    |-- city: string (nullable = true)
    // |    |-- state: string (nullable = true)
    // |    |-- zip: integer (nullable = true)
    spark.read.table("ds_data.employees").show(20, false)

    val employeeDF: DataFrame = spark.sql(
      """
        |-- 针对 array、map、struct的使用上, hive sql取字段的时候, array使用列名[索引位置]、map使用 列名["key"]、struct使用 列名.属性名称
        |select name,
        |       salary,
        |       subordinates[0]             as leaderName,
        |       subordinates[1]             as employyeName,
        |       deductions["Federal Taxes"] as FederalValue,
        |       deductions["Insurance"]     as InsuranceValue,
        |       address.street              as streetName,
        |       address.city                as cityName,
        |       address.zip                 as zipName
        |from ds_data.employees;
        |""".stripMargin)

    employeeDF.show(20, false)
    //+---------+--------+---------------------+------------+------------+--------------+--------------------------------+--------+-------+
    //|name     |salary  |leaderName           |employyeName|FederalValue|InsuranceValue|streetName                      |cityName|zipName|
    //+---------+--------+---------------------+------------+------------+--------------+--------------------------------+--------+-------+
    //|John Doe |100000.0|Mary Smith_Todd Jones|null        |null        |null          |1 Michigan Ave._Chicago_1L_60600|null    |null   |
    //|Tom Smith|90000.0 |Jan_Hello Ketty      |null        |null        |null          |Guang dong._China_0.5L_60661    |null    |null   |
    //+---------+--------+---------------------+------------+------------+--------------+--------------------------------+--------+-------+

    /*
        TODO 通过spark sql读取Hive表中的两种方式:
          1、直接使用方法 spark.read.table("库名.表名")
          2、还可以简化来写 spark.table("库名.表名")
          3、可以直接执行SQL的方式. spark.sql("select * from 库名.表名")
     */
    val userDF: DataFrame = spark.read.table("ds_data.user_info")
    userDF.show(10, false)


    // spark.sql的执行结果为一个DF,可以直接使用一些常用的算子进行操作,也可以使用下面这种模式匹配.
    spark.sql(
      """
        |select * from ds_data.visit
        |""".stripMargin)
      .map {
        case Row(user_id: String, shop_name: String) => s"user_id: $user_id, shop_name: $shop_name"
      }.show(100, false)
    //+------------------------------+
    //|value                         |
    //+------------------------------+
    //|user_id: 104399, shop_name: s1|

    /*
        TODO 通过Spark SQL 操作外部数据源(Hive表)时,插入数据有两种方式: saveAstable 和 insertInto 的区别是什么？
          1、saveAstable : 如果待保存到表本身在Hive中不存在,则先创建表,在将当前 df里的数据写入到指定的表中. ==> 可以理解为将当前df中的数据,直接写入到不存在的表中.
                           如果待保存的表在hive表中存在,则会去比对schema信息,如果列字段个数不一致,则会将当前df内的表信息,直接覆盖原有的表信息以及表中的数据. 如果字段一致,则会按照mode()指定的方式,来插入数据.
          2、insertInto : 表必须存在否则插入报错,并且df中的schema表字段 必须和 存在的表schema中表字段相同.
          两种模式如何使用: saveAstable是一个危险操作,只能用来建表,防止已经存在的表以及表中的数据被覆盖情况.
          3、两种模式使用案例 : 如果表不存在,则使用 `saveAstable`创建表. 如果存在则使用 `insertInto` 插入数据
     */
    // 创建一张表 person表,里面字段 name String,age Int,salary Double,gender String . 用于演示上述的两种插入数据的方式 saveAstable和insertInto.
    spark.sql("drop table if exists ds_data.person")
    spark.sql(
      """
        |create table ds_data.person
        |(
        |    name   String,
        |    age    Int,
        |    salary Double,
        |    gender String
        |)
        |    -- 行格式分割
        |    row format delimited
        |    -- 表中的字段按照 tab 分割
        |    fields terminated by '\t'
        |""".stripMargin)

    // 通过查询的 user_info表中的数据形成的 DF,写入到新建的person1表中(不存在则先创建表,在插入数据).
    userDF.select($"user_name", $"user_age", $"consume_amount", $"user_city").write.mode(SaveMode.Overwrite).saveAsTable("ds_data.person1")
    println("显示建表语句 ==================== ")
    spark.sql("show create table ds_data.person1").show(20, false)
    //+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
    //|createtab_stmt                                                                                                                                                                                                                         |
    //+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
    //|CREATE TABLE `ds_data`.`person1` (  `user_name` STRING COMMENT '用户姓名',  `user_age` INT COMMENT '用户年龄',  `consume_amount` DECIMAL(20,2) COMMENT '用户消费金额',  `user_city` STRING COMMENT '城市名称')USING parquet|
    //+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
    userDF.select($"user_name", $"user_age", $"consume_amount", $"user_city").write.mode(SaveMode.Overwrite).saveAsTable("ds_data.person")

    /*
        TODO 测试 spark sql 两种插入数据方式的方法: saveAsHiveTable. 当表不存在则使用 `saveAsTable`的方式创建表. 如果表存在,则使用 `insertInto`方式插入数据.
          1、查询当前Hive中已经存在的一张表 ds_data.source,将查询的数据使用分区的方式插入到一张新表中. 分区字段为: user_birthday,相同的生日去到同一个分区中.
          2、调用封装好的方法: saveAsHiveTable,并传入指定参数. 最初当前 hive 库中不存在当前 `ds_data.user_info_par`分区表,掉用saveAsHiveTable,并指定创建的为一张内部表.执行结束后查看: ds_data.user_info_par表以及分区信息.
          3、当我执行完第一遍后,当前分区表就已经存在了,再将 DF 中的数据存入Hive表时,就会走 insertInto的方式.
     */
    println("source表中原始数据 ========== ")
    val userInfoDF: DataFrame = spark.sql("select * from ds_data.source")
    //source表中原始数据 ==========
    //+-------+---------+--------+---------+----------------+-------------+
    //|user_id|user_name|user_age|user_city|user_city_person|user_birthday|
    //+-------+---------+--------+---------+----------------+-------------+
    //|1001   |tiancy   |25      |bj       |5000            |2022-09-10   |
    //|1002   |zs       |25      |sh       |10010           |2022-05-13   |
    //|1003   |ls       |25      |xn       |200             |2022-05-13   |
    //|1004   |lucy     |25      |bj       |5000            |2022-05-10   |
    //|1005   |ashe     |25      |bj       |5000            |2022-09-10   |
    //|1006   |xx       |25      |rb       |6000            |2022-05-13   |
    //|1007   |oo       |25      |bj       |5000            |2022-05-13   |
    //|1008   |siri     |25      |bj       |5000            |2022-09-10   |
    //|1009   |clida    |25      |bj       |5000            |2022-06-10   |
    //|10010  |joj      |25      |sz       |3000            |2022-07-10   |
    //+-------+---------+--------+---------+----------------+-------------+

    /** 将当前 userInfoDF 中的数据保存到一张分区表中,分区字段为userInfoDF中的字段 :user_birthday,如果分区字段不想叫这个名字,可以先出来 userInfoDF,使用`withColumnRenamed`修改字段名称,再插入到分区表中 */
    val userInfoDtDF: DataFrame = userInfoDF.withColumnRenamed("user_birthday", "dt")
    //+-------+---------+--------+---------+----------------+----------+
    //|user_id|user_name|user_age|user_city|user_city_person|dt        |
    //+-------+---------+--------+---------+----------------+----------+
    //|1001   |tiancy   |25      |bj       |5000            |2022-09-10|
    //|1002   |zs       |25      |sh       |10010           |2022-05-13|
    //|1003   |ls       |25      |xn       |200             |2022-05-13|
    //|1004   |lucy     |25      |bj       |5000            |2022-05-10|
    //|1005   |ashe     |25      |bj       |5000            |2022-09-10|
    //|1006   |xx       |25      |rb       |6000            |2022-05-13|
    //|1007   |oo       |25      |bj       |5000            |2022-05-13|
    //|1008   |siri     |25      |bj       |5000            |2022-09-10|
    //|1009   |clida    |25      |bj       |5000            |2022-06-10|
    //|10010  |joj      |25      |sz       |3000            |2022-07-10|
    //+-------+---------+--------+---------+----------------+----------+


    saveAsHiveTable(spark, userInfoDtDF, "ds_data", "user_info_par", "", Array("dt"))

    // 查询写入的结果.



    spark.stop()
  }

  /**
   * saveAstable 和 insertInto 的真实使用案例:如果表不存在,则使用 `saveAstable`创建表,如果表存在,则使用`insertInto`插入数据
   *
   * @param spark         SparkSession 对象.
   * @param data          要写入的 dataFrame
   * @param dbName        库名
   * @param tableName     表名
   * @param tableSavePath 创建表的方式:内部表和外部表.默认为 "" 的情况都是内部表. 如果要创建外部表,则需要指定一个表数据的存储路径.
   * @param ptCols        分区字段.
   */
  def saveAsHiveTable(spark: SparkSession, data: DataFrame, dbName: String, tableName: String, tableSavePath: String = "", ptCols: Array[String]): Unit = {

    // saveAsTable 建表
    // insert into 执行动态分区插入
    // if ... else ....
    // 指定内表 外表
    // 分区字段
    // create database ds_spark  local 'xxxx'

    /** 获取当前操作的库名.表名,也就是待操作的具体表 */
    val table = s"$dbName.$tableName"
    /** 标识数据库中的表。如果 `database` 未定义，则使用当前数据库。 */
    val tableIdentifier: TableIdentifier = TableIdentifier(tableName, Some(dbName))
    // println(s"tableIdentifier => $tableIdentifier") // 如果存在,则展示 库名.表名

    /** 管理Hive的元数据信息以及sparkSession中的临时表以及视图 */
    val catalog: SessionCatalog = spark.sessionState.catalog

    if (catalog.tableExists(tableIdentifier)) {
      // 如果表存在，则使用 insertInto 插入,并指定spark sql 的动态分区参数.
      spark.conf.set("spark.sql.sources.partitionOverwriteMode", "DYNAMIC")
      // 分区字段放到末尾,具体的实现方式是: 将多个数组拼接承一个数组.
      val sortCols: Array[String] = Array.concat(
        // 获取当前df中所有的字段名称,并通过`filterNot`筛选出不包含 分区列 的字段.
        data.columns.filterNot(ptCols.contains(_)),
        ptCols
      )
      val resultDF: DataFrame = data.select(sortCols.head, sortCols.tail: _*)
      println(s"insertInto 模式写入Hive表中 ........ $tableIdentifier")
      resultDF.write.mode(SaveMode.Overwrite).insertInto(table)

    } else {
      val path = s"$tableSavePath/$tableName"
      // 如果表不存在，则使用`saveAsTable`的方式创建表.并且判断创建表的方式: 内部表和外部表.
      if (tableSavePath == "") {
        // 创建内部表.
        println(s"创建内部表 $tableName")
        data.write
          .mode(SaveMode.Overwrite)
          // array => string* 格式
          .partitionBy(ptCols: _*)
          .saveAsTable(table)
      } else {
        // 创建外部表.
        println(s"创建外部表  $path")
        data.write
          .mode(SaveMode.Overwrite)
          .options(Map("path" -> path))
          // array => string* 格式
          .partitionBy(ptCols: _*)
          .saveAsTable(table)
      }
    }
  }

}
