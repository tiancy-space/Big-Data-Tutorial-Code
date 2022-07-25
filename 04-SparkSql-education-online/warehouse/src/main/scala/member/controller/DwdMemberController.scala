package member.controller

import member.service.EtlDataService
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import util.HiveUtil

/**
 * 将ods层中的数据进行ETL数据清洗,清洗结果存储到 DWD层. 建表语句在`resources/建表语句.sql`中写好了.
 */
object DwdMemberController {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "tiancy")
    val sparkConf: SparkConf = new SparkConf().setAppName("dwd_member_import").setMaster("local[*]")
    val sparkSession: SparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc: SparkContext = sparkSession.sparkContext
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://hadoop202:8020")
    //ssc.hadoopConfiguration.set("dfs.nameservices", "nameserveces:")
    HiveUtil.openDynamicPartition(sparkSession) //开启动态分区
    HiveUtil.openCompression(sparkSession) //开启压缩
    //对用户原始数据进行数据清洗 存入bdl层表中. 具体解析的json字符串每个字段的含义,可以参考当前项目下
    EtlDataService.etlBaseAdLog(ssc, sparkSession) //导入基础广告表数据
    EtlDataService.etlBaseWebSiteLog(ssc, sparkSession) //导入基础网站表数据
    EtlDataService.etlMemberLog(ssc, sparkSession) //清洗用户数据
    EtlDataService.etlMemberRegtypeLog(ssc, sparkSession) //清洗用户注册数据
    EtlDataService.etlMemPayMoneyLog(ssc, sparkSession) //导入用户支付情况记录
    EtlDataService.etlMemVipLevelLog(ssc, sparkSession) //导入vip基础数据
  }
}
