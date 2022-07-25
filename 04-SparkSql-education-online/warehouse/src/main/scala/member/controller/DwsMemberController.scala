package member.controller

//import com.atguigu.member.bean.{DwsMember, DwsMember_Result}
import member.service.DwsMemberService
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import util.HiveUtil

object DwsMemberController {

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    val sparkConf = new SparkConf().setAppName("dws_member_import").setMaster("local[*]")
    //      .set("spark.sql.shuffle.partitions", "36")
    //      .set("spark.sql.autoBroadcastJoinThreshold", "-1")
    //      .set("spark.reducer.maxSizeInFilght", "96mb")
    //      .set("spark.shuffle.file.buffer", "64k")
    //      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    //      .registerKryoClasses(Array(classOf[DwsMember]))
    val sparkSession = SparkSession.builder().config(sparkConf)
      .enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://nameservice1")
    ssc.hadoopConfiguration.set("dfs.nameservices", "nameservice1")
    HiveUtil.openDynamicPartition(sparkSession) //开启动态分区
    HiveUtil.openCompression(sparkSession) //开启压缩
    DwsMemberService.importMember(sparkSession, "20190722") //根据用户信息聚合用户表数据
    //    DwsMemberService.importMemberUseApi(sparkSession, "20190722")
  }
}
