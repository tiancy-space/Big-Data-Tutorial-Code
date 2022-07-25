package member.controller

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Test4 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("test").setMaster("local[*]")
    //      .set("spark.sql.optimizer.dynamicPartitionPruning.enabled","false") //关闭dpp
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    //    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://mycluster")
    //    ssc.hadoopConfiguration.set("dfs.nameservices", "mycluster")
    //    val result = sparkSession.sql("select a.id,a.name,a.age,a.schoolid,b.name from default.student a inner join default.school b " +
    //      " on a.schoolid=b.id and a.dt=b.dt and b.id<300000")
    //    //    result.foreachPartition(partition=>partition.foreach(item=>println(item.getAs[String]("name"))))
    //    result.foreach(item => println(item.getAs[String]("name")))
    //    result.foreachPartition(partition => {
    //      //  partition.foreach(println(_))
    //    })
    //    while (true) {
    //
    //    }
  }
}
