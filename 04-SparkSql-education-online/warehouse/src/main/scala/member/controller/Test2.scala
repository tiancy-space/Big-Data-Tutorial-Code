package member.controller

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Test2 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("test").setMaster("local[*]")
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://mycluster")
    ssc.hadoopConfiguration.set("dfs.nameservices", "mycluster")
    val df1 = sparkSession.sql("select id,name,age,schoolid from default.student")
    val df2 = sparkSession.sql("select id,name from default.school")
    df1.join(df2, df1("schoolid") === df2("id"),"left").show(100000)
     while (true){

     }
  }
}
