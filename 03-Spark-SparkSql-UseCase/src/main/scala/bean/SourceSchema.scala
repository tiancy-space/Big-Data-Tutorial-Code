package bean

import org.apache.spark.sql.types.{DataTypes, StructType}

/**
 * 手动定义数据源每个字段的类型,也就是定义表结构.,表中的字段类型,即 schema
 */
class SourceSchema {

  private val movieSchema: StructType = new StructType()
    .add("movieId", DataTypes.StringType, false)
    .add("title", DataTypes.StringType, false)
    .add("genres", DataTypes.StringType, false)

  private val ratingSchema: StructType = new StructType()
    .add("userId", DataTypes.StringType, false)
    .add("movieId", DataTypes.StringType, false)
    .add("rating", DataTypes.StringType, false)
    .add("timestamp", DataTypes.StringType, false)

  def getMovieSchema: StructType = movieSchema

  def getRatingSchema: StructType = ratingSchema
}