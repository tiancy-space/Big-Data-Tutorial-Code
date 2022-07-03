package chapter02

import org.apache.spark.sql.{Dataset, SparkSession}


/**
 * @Description: DataSet的使用. 首先要获取到当前数据源的ds对象. 通过当前ds对象注册临时表.再通过
 * @Author: tiancy
 * @Create: 2022/6/28
 */
object SparkSql04_DataSet {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("dataSet的使用")
      .getOrCreate()

    // 在将RDD和DS、DF进行转换操作时,需要使用到隐式转换,从而达到类型转化的目的.
    import spark.implicits._

    // 创建DataSet的第一种方式,直接使用 sparkSession.createDataSet(集合)的方式创建.
    val seq1: Seq[Person] = Seq(Person("zs", 12, 160), Person("ls", 22, 170), Person("ww", 33, 180))
    val ds1: Dataset[Person] = spark.createDataset(seq1)
    ds1.printSchema()
    ds1.show(10, false)

    // 第二种: 直接通过将集合转化成为 DS. 使用 toDS()方法.
    val dsPerson2: Dataset[Person] = Seq(
      Person("ashe", 18, 185),
      Person("green", 18, 200),
      Person("corry", 25, 195)
    ).toDS()
    dsPerson2.show()

    // 在使用ds时,可以直接调动以前RDD学过的算子,从而进行算子操作.

    val intDs: Dataset[Int] = Seq(1, 2, 3).toDS()
    val res: String = intDs.map(_ * 10).collect().mkString(",")
    println(res) // 10,20,30

    // 读取一个json文件,生成DataSet类型的对象.
    val jsonDataSet: Dataset[TweetsSchema] = spark.read.json("./02-Spark-Tutorial/data/tweets.txt").as[TweetsSchema]
    jsonDataSet.show()


    // DataSet与RDD进行交互.
    // 使用反射推断.
    val studentDS: Dataset[Student] = spark.sparkContext
      .textFile("./02-Spark-Tutorial/data/student.txt")
      .map(line => line.split(" "))
      .map(row => Student(row(0).toInt, row(1), row(2).toInt, row(3), row(4), row(5).toDouble))
      .toDS()

    // 使用spark sql查询表.
    studentDS.createOrReplaceTempView("student")

    spark.sql(
      """
        |select * from student
        |""".stripMargin).show()

    spark.sql(
      """
        |select classId,studentName,sum(score) as totalScore
        |from student
        |group by classId,studentName
        |""".stripMargin).show()

    // 也可以使用 DS的属性值来查找.如果是 DF,则可以通过索引位置查查找.
    studentDS.map(row => "name:" + row.studentName).show()


    spark.stop()
  }

  // 在使用DataSet时,需要强调它是一个强类型,也就是需要通过样例类.
  case class Person(name: String, age: Int, height: Int)

  // 在解析Json类型数据的时候,使用样例类时,都使用String类型. 避免数据格式问题.
  case class TweetsSchema(id: String, user: String, text: String, place: String, country: String)

  case class Student(classId: Int, studentName: String, age: Int, sex: String, subjectName: String, score: Double)

}
