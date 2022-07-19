package spark

import org.apache.spark.sql.SparkSession

/**
 * @Description: 定义一个获取 SparkSession 类型的对象 `spark`. 具体的使用方式: 自定义一个类, extends SparkApp这个类,在这个类中就可以直接使用 `spark`这个对象了.
 * @Author: tiancy
 * @Create: 2022/7/18
 */

object Session {
  /** 定义成员变量:用来做本地测试 */
  private val SparkLocalUrl: String = "local[*]"

  /**
   * 定义一个获取 `SparkSession`对象的方法,指定master eg: createSpark("yarn")
   *
   * @param master
   * @return spark
   */
  def createSpark(master: String): SparkSession = {
    SparkSession
      .builder()
      .master(master)
      .enableHiveSupport()
      .getOrCreate()
  }

  /** 获取spark上下文对象,master(local[*]) */
  def createSparkLocal(): SparkSession = {
    SparkSession
      .builder()
      .master(SparkLocalUrl)
      .enableHiveSupport()
      .getOrCreate()
  }

}

trait SparkApp extends Serializable {
  protected implicit lazy val spark: SparkSession = {
    Session.createSparkLocal()
  }

  protected implicit lazy val sparkYarn: SparkSession = {
    Session.createSpark("yarn")
  }
}
