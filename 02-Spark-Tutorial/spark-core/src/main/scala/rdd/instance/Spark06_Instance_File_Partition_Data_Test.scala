package rdd.instance

import org.apache.spark.{SparkConf, SparkContext}

object Spark06_Instance_File_Partition_Data_Test {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 创建RDD（数据模型）- 数据源为文件
        /*
           totalsize = 19
           goalsize = 19 / 3 = 6
           num = 19 / 6 = 3...1 => 3 + 1

           Hello@@   => 0123456
           Spark@@   => 78910111213
           World     => 1415161718


           [0, 6]   => 【Hello】
           [6, 12]  => 【Spark】
           [12, 18] => 【World】
           [18, 19] => 【】


         */


        val rdd = sc.textFile("data/word.txt", 3)
        rdd.saveAsTextFile("output")


        sc.stop()

    }
}
