package rdd.instance

import org.apache.spark.{SparkConf, SparkContext}

object Spark06_Instance_File_Partition_Data {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 创建RDD（数据模型）- 数据源为文件
        // 7 => 1@@, 2@@, 3

        /*
           TODO Hadoop读取数据和数据分区没有关系
           1. hadoop读取数据是按行读取的
           2. hadoop读取数据是按偏移量读取的，左右包含 [0, 5]
           3. hadoop的相同偏移量不能重复读取

           1@@  => 012
           2@@  => 345
           3    => 6

           [0, 3] => 【1，2】
           [3, 6] => 【3】
           [6, 7] => 【】



         */

        val rdd = sc.textFile("data/word.txt", 2)
        rdd.saveAsTextFile("output")


        sc.stop()

    }
}
