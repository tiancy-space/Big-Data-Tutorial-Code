package util

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.apache.commons.dbutils.QueryRunner

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/7/19
 */

object JDBCUtil {
  val dataSource = new ComboPooledDataSource()
  val url: String = ReadConfigPropUtil("jdbc.url")
  val driver: String = ReadConfigPropUtil("jdbc.driver")
  val user: String = ReadConfigPropUtil("jdbc.user")
  val password: String = ReadConfigPropUtil("jdbc.password")
  dataSource.setJdbcUrl(url)
  dataSource.setDriverClass(driver)
  dataSource.setUser(user)
  dataSource.setPassword(password)

  def getQueryRunner: Option[QueryRunner] = {
    try {
      Some(new QueryRunner(dataSource))
    } catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }
  }

}