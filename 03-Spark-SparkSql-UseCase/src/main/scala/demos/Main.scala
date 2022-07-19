package demos

import bean.SourceSchema
import metrics.{BestFilmsByOverallRating, GenresByAverageRating, MostRatedFilms}
import org.apache.spark.sql.DataFrame
import spark.{SparkApp, SparkIO}

// 最终整体执行业务的主类,也是本次需求的入口.
object Main extends SparkApp {
  // 文件路径
  private val CHILD_CSV_FILE_PATH = "03-Spark-SparkSql-UseCase/ml-25m"


  def main(args: Array[String]): Unit = {

    // schema信息
    val schemaLoader = new SourceSchema
    // 读取Movie数据集
    val movieDF: DataFrame = SparkIO.readCsv(spark, CHILD_CSV_FILE_PATH, "movies.csv", schemaLoader.getMovieSchema)
    // 读取Rating数据集
    val ratingDF: DataFrame = SparkIO.readCsv(spark, CHILD_CSV_FILE_PATH, "ratings.csv", schemaLoader.getRatingSchema)
    ratingDF.cache()
    movieDF.cache()
    movieDF.printSchema()
    ratingDF.printSchema()

    // 需求1：查找电影评分个数超过5000,且平均评分较高的前十部电影名称及其对应的平均评分
    val bestFilmsByOverallRating = new BestFilmsByOverallRating
    bestFilmsByOverallRating.run(movieDF, ratingDF, spark)

    // 需求2：查找每个电影类别及其对应的平均评分
    val genresByAverageRating = new GenresByAverageRating
    genresByAverageRating.run(movieDF, ratingDF, spark)

    // 需求3：查找被评分次数较多的前十部电影
    val mostRatedFilms = new MostRatedFilms
    mostRatedFilms.run(movieDF, ratingDF, spark)

    ratingDF.unpersist()
    movieDF.unpersist()

    spark.close()
  }
}
