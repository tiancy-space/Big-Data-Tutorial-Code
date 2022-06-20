package rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark18_RDD_Transform {

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

        // (a, 1), (a, 2),(a, 6)
        // (a, (1,1)), (a, 2) => (a, (3, 2))(a, 6) => (a, (9, 3))

//        val rdd1 = sc.makeRDD(
//            List(
//                ("a", (1,1)), ("a", (2,1)), ("b", (3,1)),
//                ("b", (4,1)), ("b", (5,1)), ("a", (6,1))
//            ), 2
//        )
        // 1. map + reduceByKey => 平均值
        // 2. 性能有点低
//        val rdd1: RDD[(String, (Int, Int))] = rdd.map {
//            case (w, c) => {
//                (w, (c, 1))
//            }
//        }
//        rdd1.reduceByKey(
//            (t1, t2) => {
//                (t1._1 + t2._1, t1._2 + t2._2)
//            }
//        ).collect().foreach(println)

        // TODO combineByKey算子可以传递三个参数
        //   1. 相同key的第一个value如何转换
        //   2. 分区内计算规则
        //   3. 分区间计算规则
//        rdd.combineByKey(
//             v => (v, 1),
//            (t:(Int, Int), v) => {
//                (t._1 + v, t._2 + 1)
//            },
//            (t1:(Int, Int), t2:(Int, Int)) => {
//                (t1._1 + t2._1, t1._2 + t2._2)
//            }
//        ).collect().foreach(println)

        rdd.combineByKey(
            v => (v, 1),
            (t:(Int, Int), v) => {
                (t._1 + v, t._2 + 1)
            },
            (t1:(Int, Int), t2:(Int, Int)) => {
                (t1._1 + t2._1, t1._2 + t2._2)
            }
        ).collect().foreach(println)


        sc.stop()

    }
}
