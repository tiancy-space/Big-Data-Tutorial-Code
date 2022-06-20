package rdd.instance

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_Instance_File_Partition {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 创建RDD（数据模型）- 数据源为文件

        // textFile第二个参数表示最小分区数量，存在默认值
        // math.min(defaultParallelism, 2)

        // Spark是不读文件的，内部文件读取采用的是Hadoop

        /*
           totalSize = 7
           goalSize = totalSize / 3 = 2
           numPart = totalSize / goalSize = 3...1


         */
        val rdd = sc.textFile("data/word.txt", 2)
        rdd.saveAsTextFile("output")


        sc.stop()

    }
}
