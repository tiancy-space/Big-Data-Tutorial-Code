package member.controller

import java.util.concurrent.Executors

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import util.HiveUtil

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object Test {

  case class Student(id:Int,name:String,age:Int,schoolId:Int,dt:String)
  case class School(id:Int,name:String,dt:String)
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    val sparkConf = new SparkConf().setAppName("dwd_member_import").setMaster("local[*]")
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://mycluster")
    ssc.hadoopConfiguration.set("dfs.nameservices", "mycluster")
    HiveUtil.openDynamicPartition(sparkSession) //开启动态分区
    val array=new ArrayBuffer[Student]();
    val random=new Random();
    val executor = Executors.newFixedThreadPool(12);
    for(i<-0 until 50000000){ //插入五千万条数据
      executor.submit(new Runnable {
        override def run(): Unit = {
          array.append( Student(i,"name"+i,18,random.nextInt(1000000),"20200805"))
        }
      })
    }
    import sparkSession.implicits._
    val studentData=ssc.makeRDD(array).toDF()
    studentData.write.mode(SaveMode.Overwrite).insertInto("default.student")
  }

}
