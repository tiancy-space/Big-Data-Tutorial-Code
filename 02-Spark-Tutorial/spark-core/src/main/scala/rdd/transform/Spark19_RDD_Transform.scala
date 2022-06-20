package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark19_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 取出每个相同key的平均值
        val rdd = sc.makeRDD(
            List(
                ("a", 1), ("a", 2), ("b", 3),
                ("b", 4), ("b", 5), ("a", 6)
            ), 2
        )
        //rdd.groupByKey()
        rdd.reduceByKey(_+_).reduceByKey(_+_)
        //rdd.aggregateByKey(0)(_+_, _+_)
        //rdd.foldByKey(0)(_+_)
        //rdd.combineByKey(v=>v, (t:Int, v)=>t+v, (t1:Int, t2:Int)=>(t1+t2))

        sc.stop()

    }
}
