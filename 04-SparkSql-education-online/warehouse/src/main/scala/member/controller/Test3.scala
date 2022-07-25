package member.controller

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import util.HiveUtil

object Test3 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("dws_sellcourse_import")
      .set("spark.sql.autoBroadcastJoinThreshold", "-1") //把广播join先禁止了
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://mycluster")
    ssc.hadoopConfiguration.set("dfs.nameservices", "mycluster")
    HiveUtil.openDynamicPartition(sparkSession)
    HiveUtil.openCompression(sparkSession)
    val course = sparkSession.sql("select courseid,coursename,status,pointlistid,majorid,chapterid,chaptername,edusubjectid," +
      "edusubjectname,teacherid,teachername,coursemanager,money,dt,dn from dwd.dwd_sale_course")
    val shoppingCart = sparkSession.sql("select courseid,orderid,coursename,discount,sellmoney,createtime,dt,dn from dwd.dwd_course_shopping_cart")
      .drop("coursename")
      .withColumnRenamed("discount", "cart_discount")
      .withColumnRenamed("createtime", "cart_createtime")
    val result = course.join(shoppingCart, Seq("courseid", "dt", "dn"), "right")
//    result.foreachPartition(partition=>{
//      partition.foreach(item=>{
//        println(item.getAs[String]("coursename"))
//      })
//    })
  }
}
