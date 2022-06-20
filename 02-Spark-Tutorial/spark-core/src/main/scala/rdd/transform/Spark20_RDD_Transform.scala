package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark20_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 取出每个相同key的平均值
        val rdd = sc.makeRDD(
            List(
                (new User(), 1), (new User(), 6), (new User(), 3),
                (new User(), 4), (new User(), 5), (new User(), 2)
            ), 2
        )

        rdd.sortByKey(false).collect.foreach(println)

        sc.stop()

    }
    class User extends Ordered[User]{
        override def compare(that: User): Int = {
            0
        }
    }

}
