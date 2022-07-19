package metrics

import bean.TenGreatestMoviesByAverageRating
import org.apache.commons.dbutils.QueryRunner
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import util.JDBCUtil


/**
 * 需求1：查找电影评分个数超过5000,且平均评分较高的前十部电影名称及其对应的平均评分
 */
class BestFilmsByOverallRating extends Serializable {

  def run(moviesDataset: DataFrame, ratingsDataset: DataFrame, spark: SparkSession): Unit = {
    import spark.implicits._

    // 将moviesDataset注册成表
    moviesDataset.createOrReplaceTempView("movies")
    // 将ratingsDataset注册成表
    ratingsDataset.createOrReplaceTempView("ratings")

    // 查询SQL语句
    val ressql1: String =
      """
        |WITH ratings_filter_cnt AS (
        |SELECT
        |	    movieId,
        |	    count( * ) AS rating_cnt,
        |	    avg( rating ) AS avg_rating
        |FROM
        |	    ratings
        |GROUP BY
        |	    movieId
        |HAVING
        |	    count( * ) >= 5000
        |),
        |ratings_filter_score AS (
        |SELECT
        |     movieId, -- 电影id
        |     avg_rating -- 电影平均评分
        |FROM ratings_filter_cnt
        |ORDER BY avg_rating DESC -- 平均评分降序排序
        |LIMIT 10 -- 平均分较高的前十部电影
        |)
        |SELECT
        |	   m.movieId,
        |	   m.title,
        |	   round(r.avg_rating,2) AS avgRating
        |FROM
        |	  ratings_filter_score r
        |JOIN movies m ON m.movieId = r.movieId
        |ORDER By avgRating DESC
      """.stripMargin

    val resultDS: Dataset[TenGreatestMoviesByAverageRating] = spark.sql(ressql1).as[TenGreatestMoviesByAverageRating]
    //
    println("需求一SQL分析结果......")
    resultDS.show(10, false)
    resultDS.printSchema()
    println("查找电影评分个数超过5000,且平均评分较高的前十部电影名称及其对应的平均评分,开始插入数据......")
    // 写入MySQL
    resultDS.foreachPartition(
      (par: Iterator[TenGreatestMoviesByAverageRating]) => par.foreach(insert2Mysql)
    )
    println("需求一执行完成......")
  }

  /**
   * 获取连接，调用写入MySQL数据的方法
   *
   * @param res
   */
  private def insert2Mysql(res: TenGreatestMoviesByAverageRating): Unit = {
    lazy val conn: Option[QueryRunner] = JDBCUtil.getQueryRunner
    conn match {
      case Some(connection) => {
        // 如果当前获取的连接不为空,则调用下面封装好的更新方法. upsert() 方法.
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
  private def upsert(r: TenGreatestMoviesByAverageRating, conn: QueryRunner): Unit = {
    try {
      val sql: String =
        s"""
           |REPLACE INTO `ten_movies_averagerating`(
           |movieId,
           |title,
           |avgRating
           |)
           |VALUES
           |(?,?,?)
       """.stripMargin
      // 执行insert操作
      conn.update(sql, r.movieId, r.title, r.avgRating)
    } catch {
      case e: Exception => {
        e.printStackTrace()
        System.exit(-1)
      }
    }
  }

}
