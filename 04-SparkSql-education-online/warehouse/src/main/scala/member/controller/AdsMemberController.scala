package member.controller

import member.service.AdsMemberService
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import util.HiveUtil

object AdsMemberController {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    val sparkConf = new SparkConf().setAppName("ads_member_controller").setMaster("local[*]")
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://nameservice1")
    ssc.hadoopConfiguration.set("dfs.nameservices", "nameservice1")
    HiveUtil.openDynamicPartition(sparkSession) //开启动态分区
    AdsMemberService.queryDetailApi(sparkSession, "20190722")
    //    AdsMemberService.queryDetailSql(sparkSession, "20190722")
  }
}
