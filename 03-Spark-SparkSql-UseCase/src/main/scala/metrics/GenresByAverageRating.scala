package metrics

import bean.TopGenresByAverageRating
import org.apache.commons.dbutils.QueryRunner
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import util.JDBCUtil

/**
 * 需求2：查找每个电影类别及其对应的平均评分
 */
class GenresByAverageRating extends Serializable {
  def run(moviesDataset: DataFrame, ratingsDataset: DataFrame, spark: SparkSession): Unit = {
    import spark.implicits._
    // 将moviesDataset注册成表
    moviesDataset.createOrReplaceTempView("movies")
    // 将ratingsDataset注册成表
    ratingsDataset.createOrReplaceTempView("ratings")

    val ressql2: String =
      """
        |WITH explode_movies AS (
        |    -- 1、先将当前电影表中 分类字段通过炸裂 + 侧写炸开,形成列转行操作.
        |    SELECT movieId,
        |           title,
        |           category
        |    FROM movies lateral VIEW explode ( split ( genres, "\\|" ) ) temp AS category
        |    )
        |SELECT m.category    AS genres,
        |       avg(r.rating) AS avgRating
        |FROM explode_movies m JOIN ratings r ON m.movieId = r.movieId
        |GROUP BY m.category
        |""".stripMargin
    val resultDS: Dataset[TopGenresByAverageRating] = spark.sql(ressql2).as[TopGenresByAverageRating]

    // 打印数据
    println("需求二分析结果 ...... ")
    resultDS.show(10, false)
    resultDS.printSchema()
    // 写入MySQL
    println("需求二开始写入数据......")
    resultDS.foreachPartition((par: Iterator[TopGenresByAverageRating]) => par.foreach(insert2Mysql))
    println("需求二写入数据完成......")


  }

  /**
   * 获取连接，调用写入MySQL数据的方法
   *
   * @param res
   */
  private def insert2Mysql(res: TopGenresByAverageRating): Unit = {
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
  private def upsert(r: TopGenresByAverageRating, conn: QueryRunner): Unit = {
    try {
      val sql: String =
        s"""
           |REPLACE INTO `genres_average_rating`(
           |genres,
           |avgRating
           |)
           |VALUES
           |(?,?)
       """.stripMargin
      // 执行insert操作
      conn.update(
        sql,
        r.genres,
        r.avgRating
      )
    } catch {
      case e: Exception => {
        e.printStackTrace()
        System.exit(-1)
      }
    }
  }


}
