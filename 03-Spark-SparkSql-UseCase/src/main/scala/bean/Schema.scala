package bean

/**
 * movie表映射成实体类
 *
 * @param movieId 电影的id
 * @param title   电影的标题
 * @param genres  电影类别
 */
case class Movies(movieId: String, title: String, genres: String)

/**
 * 影评表映射成的实体类.
 *
 * @param userId    用户的id
 * @param movieId   电影的id
 * @param rating    用户评分
 * @param timestamp 时间戳
 */
case class Ratings(userId: String, movieId: String, rating: String, timestamp: String)

/**
 * 需求1MySQL结果表对应的实体类: TenGreatestMoviesByAverageRating
 *
 * @param movieId   电影的id
 * @param title     电影的标题
 * @param avgRating 电影平均评分
 */
case class TenGreatestMoviesByAverageRating(movieId: String, title: String, avgRating: String)

/**
 * 需求2MySQL结果表,TopGenresByAverageRating
 *
 * @param genres    电影类别
 * @param avgRating 平均评分
 */
case class TopGenresByAverageRating(genres: String, avgRating: String)

/**
 * 需求3MySQL结果表,TenMostRatedFilms
 *
 * @param movieId   电影的id
 * @param title     电影的标题
 * @param ratingCnt 电影被评分的次数
 */
case class TenMostRatedFilms(movieId: String, title: String, ratingCnt: String)