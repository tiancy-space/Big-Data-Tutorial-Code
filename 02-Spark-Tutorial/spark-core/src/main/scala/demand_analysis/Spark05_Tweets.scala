package demand_analysis

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/27
 */
object Spark05_Tweets {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("tweets").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val lineRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/tweets.txt")


    /** json在解析前,要先去除空行... */
    val tweetsRDD: RDD[TweetsSchema] = lineRDD.filter(line => line.trim.nonEmpty).map(
      line => {
        val noObject: JSONObject = JSON.parseObject(line)
        TweetsSchema(
          noObject.getString("id"),
          noObject.getString("user"),
          noObject.getString("text"),
          noObject.getString("place"),
          noObject.getString("country")
        )
      }
    )

    // println(tweetsRDD.collect().mkString(", "))

    // 1.统计总条数 id log 唯一id，代码框架 以及 消费，重复json数据，uuid

    val qu1 = tweetsRDD.groupBy(s => s.id).distinct().count()

    println("统计总条数=>" + qu1)

    // 2.统计每个用户的tweets 条数

    val qu2 = tweetsRDD.map(s => s.user).distinct().count()

    println("统计每个用户的tweets=>" + qu2)

    //3.统计tweets中被@的分别是那些人；
    val regex = """@[\w]+""".r
    val qu3 = tweetsRDD.map(_.text)
      .flatMap(s => regex.findAllIn(s))
    println("统计tweets中被@的分别是那些人" + qu3.collect().mkString(", "))

    // 4. 统计tweets中被@最多10个人
    val qu4 = qu3.map(x => (x, 1))
      .reduceByKey(_ + _)
      .sortByKey(false)
      .take(10)

    println(qu4.mkString(", "))


    sc.stop()
  }

  /**
   * tweets 数据统计,样例类.
   *
   * @param id
   * @param user
   * @param text
   * @param place
   * @param country
   */
  // 在解析Json类型数据的时候,使用样例类时,都使用String类型. 避免数据格式问题.
  case class TweetsSchema(
                           id: String,
                           user: String,
                           text: String,
                           place: String,
                           country: String
                         )

}
