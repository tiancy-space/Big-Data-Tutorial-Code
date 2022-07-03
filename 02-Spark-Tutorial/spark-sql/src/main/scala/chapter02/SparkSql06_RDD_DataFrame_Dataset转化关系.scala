package chapter02

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * @Description: spark中rdd、Df、Ds之间的转化.
 * @Author: tiancy
 * @Create: 2022/7/1
 */
object SparkSql06_RDD_DataFrame_Dataset转化关系 {
  /*
      TODO 注意: 在进行 rdd、DataFrame、DataSet之间相互转化郭成中时,需要想到一张图,以及 三者强调的部分: RDD强调数据本身,DataFrame强调结构(也就是Row类型 + 表结构 schema),DataSet则强调 类型,也就是需要有阳历类与之对应.
        RDD --- rdd.toDf("id","name","age") -->   DataFrame  ----df.as[Person]---> DataSet
        RDD -- rdd.map{case ... }.toDS --> DataSet
        DataSet --- ds.toDF()----> DataFrame ----df.RDD()---> RDD
        DataSet -- ds.rdd()--> RDD
   */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("operation csv").master("local[*]").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    // TODO 1、RDD ---转化---> DataFrame RDD中注重的的是数据本身.DataFrame注重的是结构,也就是需要给定一个 schema . DataSet注重的是 数据中的类型,也就是需要一个类来限制每一列.
    // 1) 创建一个RDD.
    val personRDD: RDD[Row] = sc.makeRDD(
      Seq((1, "green", "male"), (2, "ashe", "female"), (3, "武大郎", "male"), (4, "武松", "male"))
    ).map(tuple => Row(tuple._1, tuple._2, tuple._3))

    //2) RDD转为DadaFrame, 这里需要注意: DataFrame = RDD[Row]类型. 是带结构(schema) + row对象. DF 注重的是 结构(可以理解为schema)
    /** 两种转化方式: 1、直接给定 rdd 和 schema 对象,调用createDataFrame() . 2、toDF */
    import org.apache.spark.sql.types._
    val schema: StructType = StructType(List(
      StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("gender", StringType, true)
    ))
    val personDf: DataFrame = spark.createDataFrame(personRDD, schema)
    personDf.printSchema()
    personDf.show()

    println(" ========RDD 转化 DF另一种方式: 可以直接将 rdd.toDF(给定列名)=========================== ")
    /** 2、rdd 转化为DataFrame, 可以使用 rdd.toDF(给定一个列名) */
    val rdd: RDD[(Int, String, Int)] = sc.makeRDD(
      List(
        (1, "zhangsan", 30),
        (2, "lisi", 40),
        (3, "wangwu", 50),
      )
    )
    import spark.implicits._
    // RDD => DataFrame
    val df: DataFrame = rdd.toDF("id", "name", "age")
    df.printSchema()
    df.show()

    // TODO 2、 RDD --> DataSet : DataSet数据结构注重的是类型. 也就是需要给定一个样例类. map 模式匹配转化成对象. 再调用DS.
    val goodsRDD: RDD[(Int, String, Double)] = sc.makeRDD(List((1, "iphone12", 6999.5), (2, "iphone11", 5999.9), (3, "iphone 13 pro max", 8999.5)))
    val goodsDs: Dataset[Goods] = goodsRDD.map {
      // 注意使用模式匹配,不要忘了 case. 这里的写法: 通过 tuple() 来匹配阳历类.
      case (id, name, price) => Goods(id, name, price)
    }.toDS()
    goodsDs.printSchema()
    goodsDs.show()

    // TODO 3、DataFrame 转化为 DataSet
    val personDs: Dataset[Person] = personDf.as[Person]
    personDs.printSchema()
    personDs.show()


    // TODO 4、DataFrame 转化为 RDD
    val dfRdd: RDD[Row] = df.rdd
    dfRdd.collect().foreach(println)
    // TODO 5、DataSet 转化为 RDD
    val dsTtoRdd: RDD[Goods] = goodsDs.rdd
    dsTtoRdd.collect().foreach(println)

    // TODO 6、DataSet 转化为 DataFrame
    val dsToDf: DataFrame = personDs.toDF()
    dsToDf.printSchema()
    dsToDf.show()

    sc.stop()
    spark.stop()
  }


  // 注意阳历类放的位置,不能随便放,应该放在main方法外.
  case class User(id: Int, name: String, age: Int)

  case class Person(id: Int, name: String, gender: String)

  case class Goods(id: Int, name: String, price: Double)

}
