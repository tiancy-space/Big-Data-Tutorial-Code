package metrics

import bean.TenMostRatedFilms
import org.apache.commons.dbutils.QueryRunner
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import util.JDBCUtil

/**
 * 需求3：查找被评分次数较多的前十部电影.
 */
class MostRatedFilms extends Serializable {
  def run(moviesDataset: DataFrame, ratingsDataset: DataFrame, spark: SparkSession): Unit = {

    import spark.implicits._

    // 将moviesDataset注册成表
    moviesDataset.createOrReplaceTempView("movies")
    // 将ratingsDataset注册成表
    ratingsDataset.createOrReplaceTempView("ratings")

    val ressql3: String =
      """
        |WITH rating_group AS (
        |    SELECT
        |       movieId,
        |       count( * ) AS ratingCnt
        |    FROM ratings
        |    GROUP BY movieId
        |),
        |rating_filter AS (
        |    SELECT
        |       movieId,
        |       ratingCnt
        |    FROM rating_group
        |    ORDER BY ratingCnt DESC
        |    LIMIT 10
        |)
        |SELECT
        |    m.movieId,
        |    m.title,
        |    r.ratingCnt
        |FROM
        |    rating_filter r
        |JOIN movies m ON r.movieId = m.movieId
        |
  """.stripMargin

    val resultDS: Dataset[TenMostRatedFilms] = spark.sql(ressql3).as[TenMostRatedFilms]
    // 打印数据
    println("需求三分析结果......")
    resultDS.show(10, false)
    resultDS.printSchema()
    // 写入MySQL
    println("需求三准备写入数据......")
    resultDS.foreachPartition((par: Iterator[TenMostRatedFilms]) => par.foreach(insert2Mysql))
    println("需求三写入数据完成......")

  }

  /**
   * 获取连接，调用写入MySQL数据的方法
   *
   * @param res
   */
  private def insert2Mysql(res: TenMostRatedFilms): Unit = {
    lazy val conn = JDBCUtil.getQueryRunner
    conn match {
      case Some(connection) => {
        upsert(res, connection)
      }
      case None => {
        println("Mysql连接失败")
        System.exit(-1)
      }
    }
  }

  /**
   * 封装将结果写入MySQL的方法
   * 执行写入操作
   *
   * @param r
   * @param conn
   */
  private def upsert(r: TenMostRatedFilms, conn: QueryRunner): Unit = {
    try {
      val sql =
        s"""
           |REPLACE INTO `ten_most_rated_films`(
           |movieId,
           |title,
           |ratingCnt
           |)
           |VALUES
           |(?,?,?)
       """.stripMargin
      // 执行insert操作
      conn.update(
        sql,
        r.movieId,
        r.title,
        r.ratingCnt
      )
    } catch {
      case e: Exception => {
        e.printStackTrace()
        System.exit(-1)
      }
    }
  }

}
