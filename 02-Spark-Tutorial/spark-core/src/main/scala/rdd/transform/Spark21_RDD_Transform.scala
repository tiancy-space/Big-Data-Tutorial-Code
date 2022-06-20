package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark21_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO Spark Join 会将相同的key连接在一起
        val rdd1 = sc.makeRDD(
            List(
                ("a", 1), ("b", 2), ("c", 3)
            )
        )
        val rdd2 = sc.makeRDD(
            List(
                ("a", 4), ("d", 5), ("c", 6)
            )
        )
        // 相同的key将value连接在一起
        // (a, (1, 4))
        // join 有可能有笛卡尔乘积效果，而且还有可能有shuffle，所以并不推荐使用
        rdd1.join(rdd2).collect().foreach(println)
        //rdd1.leftOuterJoin(rdd2).collect().foreach(println)
        //rdd1.rightOuterJoin(rdd2).collect().foreach(println)
        //rdd1.fullOuterJoin(rdd2).collect().foreach(println)
        rdd1.cogroup(rdd2).collect().foreach(println)

        sc.stop()

    }

}
