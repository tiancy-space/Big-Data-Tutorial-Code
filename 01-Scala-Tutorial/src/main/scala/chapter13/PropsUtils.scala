package chapter13

import java.util.ResourceBundle

/**
  * 配置解析
  */
object PropsUtils {

  private val bundle: ResourceBundle = ResourceBundle.getBundle("core")

  def getValue(key: String ) : String = {
      bundle.getString(key)
  }


  def main(args: Array[String]): Unit = {
    println(getValue("executor.nums"))

    println(getValue(Config.EXECUROT_NUMS))
  }
}
