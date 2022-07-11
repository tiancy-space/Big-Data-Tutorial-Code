package chapter06_sparksql常用函数

import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * @Description: 开创函数练习
 * @Author: tiancy
 * @Create: 2022/7/8
 */

object SparkSql03_开窗函数练习 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("windows function")
      .master("local[*]")
      .getOrCreate()

    // TODO 1、查询员工的基本信息以及部门员工的最高工资. 基本信息 + 添加一行每个部门的最大薪资 ==> 开窗: 第一种写法: 按照部门分组,取分组内工资最大值. 第二种写法:按照部门分组,按照工资降序取第一条.
    import spark.implicits._
    val userInfoDF: DataFrame = spark.sparkContext.makeRDD(List(
      (1, "zs", 18, 1500, "A", true),
      (2, "ls", 20, 3000, "B", false),
      (3, "ww", 20, 2000, "B", false),
      (4, "zl", 30, 3500, "A", true),
      (5, "tq", 40, 2500, "A", false)
    )).toDF("id", "name", "age", "salary", "dept_id", "sex")
    userInfoDF.createOrReplaceTempView("user_info")

    val window1: WindowSpec = Window.partitionBy("dept_id")
    import org.apache.spark.sql.functions._
    userInfoDF.withColumn("deptMaxSalary", max("salary").over(window1)).show(10, false)
    //+---+----+---+------+-------+-----+-------------+
    //|id |name|age|salary|dept_id|sex  |deptMaxSalary|
    //+---+----+---+------+-------+-----+-------------+
    //|1  |zs  |18 |1500  |A      |true |3500         |
    //|4  |zl  |30 |3500  |A      |true |3500         |
    //|5  |tq  |40 |2500  |A      |false|3500         |
    //|2  |ls  |20 |3000  |B      |false|3000         |
    //|3  |ww  |20 |2000  |B      |false|3000         |
    //+---+----+---+------+-------+-----+-------------+

    // sql写法
    spark.sql(
      """
        |select
        |   id,name,salary,dept_id ,sex,
        |   max(salary) over(partition by dept_id) as deptMaxSalary
        |from user_info
        |""".stripMargin).show(10, false)


    // TODO 2、练习二 topN 统计某天每个用户访问次数前十的页面 : 先将当前数据进行 `day` + `user_id` + page_id 进行分组,求每个用户每天对同一个页面的访问次数(T1),再进行开窗操作.
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
    pageViewDF.createOrReplaceTempView("t_page")
    val window2: WindowSpec = Window.partitionBy("day", "user_id").orderBy(-$"count")
    // 先按照 day、user_id、page_id进行分组,求每个用户每天每个页面的总的点击次数. 再进行开窗: 用户 + day进行分组,总点击次数进行降序,取排名前10
    pageViewDF
      .groupBy("day", "user_id", "page_id")
      // 这里对聚合结果进行条数统计,如何取别名? ==> 默认列名是count
      .count()
      // .as("totalCtn") // 这里是对当前执行的结果表,取别名
      .withColumn("dayUserViewTop10", dense_rank().over(window2))
      .filter("dayUserViewTop10 <= 10")
      .show(100, false)

    // sql写法如下
    println("统计每个用户每天每个页面访问次数前10的信息 sql写法 ......")
    spark.sql(
      """
        |select *
        |from (
        |         select day,
        |                user_id,
        |                page_id,
        |                userClickDayCtn,
        |                rank() over (partition by day,user_id order by userClickDayCtn desc) as dayUserViewTop10
        |         from (
        |                  select day,
        |                         user_id,
        |                         page_id,
        |                         count(user_id) as userClickDayCtn
        |                  from t_page
        |                  group by day, user_id, page_id
        |              ) t1
        |     ) t2
        |where dayUserViewTop10 <= 10
        |""".stripMargin).show(100, false)

    spark.stop()
  }
}
