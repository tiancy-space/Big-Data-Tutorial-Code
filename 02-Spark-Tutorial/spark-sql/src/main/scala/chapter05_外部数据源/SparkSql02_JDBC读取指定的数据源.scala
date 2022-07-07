package chapter05_外部数据源

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description: 使用spark sql JDBC的方式,读取指定的数据源.
 * @Author: tiancy
 * @Create: 2022/7/6
 */
object SparkSql02_JDBC读取指定的数据源 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession
      .builder()
      .appName("spark sql operator Hive")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    import org.apache.spark.sql._
    import spark.implicits._

    /*
       TODO 通过 `jdbc` 方式读取mysql中的表.
         1、可以直接使用 spark.read.format("jdbc").options(Map...).load()
         2、也可以使用通用的读取数据源的通用方法:
    */
    // 读取配置文件中 `config.properties` mysql的配置项.
    val url: String = ConfigUtil("jdbc.url")
    val driver: String = ConfigUtil("jdbc.driver")
    val userName: String = ConfigUtil("jdbc.user")
    val password: String = ConfigUtil("jdbc.password")
    val mysqlTableName = "user_info"

    /*
        通过 options指定的jdbc配置项: key是固定写死的.
        还有其他的参数: query、customSchema、truncate等参数,具体的使用可以看 spark sql的官网.
        https://spark.apache.org/docs/3.3.0/sql-data-sources-jdbc.html#data-source-option
     */

    val jdbcMap = Map(
      "url" -> url,
      "driver" -> driver,
      "dbtable" -> mysqlTableName,
      "user" -> userName,
      "password" -> password
    )
    // 需要通过 options 指定: url、driver、user、password、表名
    val userInfoDF: DataFrame = spark.read.format("jdbc").options(jdbcMap).load()

    println("====== 使用 spark.read.format(jdbc).options(map).load() ========")
    userInfoDF.show(10, false)
    //====== 使用 spark.read.format(jdbc).options(map).load() ========
    //+-------+---------+-----------------+
    //|user_id|user_name|user_phone       |
    //+-------+---------+-----------------+
    //|1      |ashe     |iphne13 pro max  |
    //|2      |green    |huawei mata20 pro|
    //|3      |kangkang |iphone13 pro max |
    //+-------+---------+-----------------+

    // 通过jdbc读取MySQL的第二种方式: 直接使用 spark.read.jdbc(url,表名,prop(driver、用户名、密码))
    val props = new Properties()
    props.put("user", userName)
    props.put("password", password)
    props.put("driver", driver)

    spark.read.jdbc(url, mysqlTableName, props).show(10, false)


    /*
        TODO 2.1、spark sql 通过jdbc方式写入数据到MySQL中. spark write mysql 会进行自动类型推断并且自动建表.
     */
    // delimiter 和 sep 使用效果相同.
    val operationCVSMap = Map("header" -> "true", "sep" -> ",", "inferSchema" -> "true")
    // 数据格式 name;age;job | Jorge;30;Developer
    val df4: DataFrame = spark.read.options(operationCVSMap).csv("./02-Spark-Tutorial/data/people1.csv")
    // 将当前读取到的df通过jdbc的方式写入到 mysql中.
    df4.show()
    val jdbcWriteMap = Map(
      "url" -> url,
      "driver" -> driver,
      "dbtable" -> "employee_info",
      "user" -> userName,
      "password" -> password
    )

    df4.write.format("jdbc").options(jdbcWriteMap).mode(SaveMode.Append).save()

    /*
      TODO 2.2、spark sql 通过 jdbc方式操作 mysql 方式二 : 直接使用 df.write.jdbc(url,tableName,props)
        这种方式,可以不需要将表名写死在 options内的map中,可以直在调用 df.write.jdbc(url,tableName,prop)中指定. 因此使用起来更加灵活.
     */
    val prop = new Properties()
    prop.setProperty("driver", driver)
    prop.setProperty("user", userName)
    prop.setProperty("password", password)
    df4.write.mode(SaveMode.Append).jdbc(url, "employee2", prop)

    /*
        TODO 3、通过 spark sql jdbc 操作 mysql表,通过 option("query",sql语句) 来执行一条sql. 这里需要注意: 执行sql语句时, 使用 option指定的配置中, `query`和`dbtable`不可以同时出现.
     */
    spark.read.format("jdbc")
      .option("user", userName)
      .option("password", password)
      .option("driver", driver)
      .option("url", url)
      //      .option("dbtable", "user_info")
      .option("query", "select * from user_info where user_id = 3")
      .load().show()
    /*
        TODO 4、实现:自己建表操作,在创建表时指定索引、唯一键、主键等. spark sql 操作表时,不期望自动建表. 也就是关闭自动建表. 可以使用 option("truncate	",true) . 默认是关闭的. 这里开启这个功能.
          自己创建一张表,建表语句如下
          create table person
          (
            id int auto_increment,
            name varchar(30) default '' null,
            age int null,
            gender varchar(10) default 'male' not null,
            salary double null,
            `ModifiedTime` datetime DEFAULT NULL,
            constraint person_pk primary key (id)
          )
          comment 'spark sql 通过jdbc方式操作mysql中的表,这里演示关闭自动建表功能';
          可以尝试开启清空表的方式,来保持当前表结构. `show create table person;` 查看建表语句即可.
      */
    val people2DF: DataFrame = spark.read.format("csv").options(operationCVSMap).load("./02-Spark-Tutorial/data/people2.csv")
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    // 在添加新列或者修改列前,需要先导入当前包: `import org.apache.spark.sql.functions.lit`
    import org.apache.spark.sql.functions.lit
    val peopleResult: DataFrame = people2DF.withColumn("ModifiedTime", lit(sdf.format(System.currentTimeMillis())))
    peopleResult.show()

    // 没有表则创建,有表则清空表数据,不清空表结构.
    peopleResult.write
      .option("user", userName)
      .option("password", password)
      .option("driver", driver)
      .option("url", url)
      .option("dbtable", "person")
      // 通过当前参数,覆盖表时,仅仅清空表,不清空表结构.
      .option("truncate", value = true)
      .format("jdbc")
      .mode(SaveMode.Overwrite)
      .save()


    /*
        TODO 5、读取数据和写入数据时,自定义 schema,从而在操作表时,相同关联字段类型一致.
          eg: 读取MySQL中一张表,表中 id字段为int 通过phoenix读取HBase中的表,关联字段类型为 String类型. 类型不一致,关联可能会出问题.
          操作方式: 通过 option("customSchema",schema) ==> 指定的表中数据类型读取.
          往某个表中写时,指定字段类型 通过 option("createTableColumnTypes",字符串类型的 writeSchema) .具体使用如下
     */
    // 通过jdbc 读取MySQL中的表,原始样子,表中字段类型
    val personDF4: DataFrame = spark.read
      .option("user", userName)
      .option("password", password)
      .option("driver", driver)
      .option("url", url)
      .option("dbtable", "person")
      .format("jdbc")
      .load()

    personDF4.show(10, false)
    personDF4.printSchema()
    //root
    // |-- id: integer (nullable = true)
    // |-- name: string (nullable = true)
    // |-- age: integer (nullable = true)
    // |-- gender: string (nullable = true)
    // |-- salary: double (nullable = true)
    // |-- ModifiedTime: timestamp (nullable = true)

    // 定义一个 schema,用来修改当前读取到的表的字段类型. 这里将  id: integer (nullable = true) ==> string类型
    var customSchema: String = "id string,ModifiedTime string"
    val personDF5: DataFrame = spark.read
      .option("user", userName)
      .option("password", password)
      .option("driver", driver)
      .option("url", url)
      .option("dbtable", "person")
      .option("customSchema", customSchema)
      .format("jdbc")
      .load()
    personDF5.show()
    //+---+-----+----+------+--------+-------------------+
    //| id| name| age|gender|  salary|       ModifiedTime|
    //+---+-----+----+------+--------+-------------------+
    //|  1|Jorge|null|  male| 8888.65|2022-07-07 18:17:32|
    //|  2| null|  32|female| 9000.65|2022-07-07 18:17:32|

    personDF5.printSchema()
    //root
    //    |-- id: string (nullable = true)
    //    |-- name: string (nullable = true)
    //    |-- age: integer (nullable = true)
    //    |-- gender: string (nullable = true)
    //    |-- salary: double (nullable = true)
    //    |-- ModifiedTime: string (nullable = true)


    spark.stop()
  }

}
