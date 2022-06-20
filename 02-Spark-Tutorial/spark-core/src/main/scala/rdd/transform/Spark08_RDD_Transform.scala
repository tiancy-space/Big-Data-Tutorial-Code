package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark08_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO filter
        val rdd = sc.makeRDD(
            1 to 10
        )

        // TODO
        // sample算子用于从数据集中抽取（采样）数据
        // sample算子的第一个参数表示为抽取方式有2种：
        //  1. 抽取后放回：true
        //  2. 抽取后不放回 : false
        // sample算子的第二个参数
        //  1. 抽取后放回的场合，表示预计重复的次数
        //  2. 抽取后不放回的场合，表示每一条数据被抽取的概率(抛硬币)
        //rdd.sample(false, 0.5).collect().foreach(println)
        //rdd.sample(true, 2).collect().foreach(println)
        // sample算子的第三个参数：随机数种子（随机数不随机）
        // 随机数依靠随机算法实现
        rdd.sample(false, 0.5, 2).collect().foreach(println)


        sc.stop()

    }
}
