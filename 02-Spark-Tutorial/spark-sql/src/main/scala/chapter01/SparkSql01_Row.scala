package chapter01

/**
 * @Description: 演示Spark SQL中的数据抽象: 从 RDD --> DataFrame --> DataSet之间的转化.
 * @Author: tiancy
 * @Create: 2022/6/28
 */

/*
    DataFrame = RDD[Row] + Schema；DataFrame 的前身是 SchemaRDD
    Row对象,这里主要用来给定RDD的类型,方便在执行前通过执行计划,发掘RDD的内部细节.它在编程语言中,也是一个对象的体现.很像是一个长度任意的元组.
 */
object SparkSql01_Row extends App {
  // 在使用 row 对象前,先导包

  import org.apache.spark.sql.Row

  // 直接通过当前Row的样例类中的apply方法.可以直接理解为当前表中的一行数据.
  val row: Row = Row(1, "tiancy", "male", 25, 184.6, 72.5)
  val id: Any = row(0)
  val name: Any = row(1)
  val gender: Any = row(2)
  val age: Any = row(3)
  val height: Any = row(4)
  val weight: Any = row(5)

  // id = 1 | name = tiancy | gender = male | age = 25 | height = 184.6 | weight = 72.5
  println(s"id = $id | name = $name | gender = $gender | age = $age | height = $height | weight = $weight")
}
