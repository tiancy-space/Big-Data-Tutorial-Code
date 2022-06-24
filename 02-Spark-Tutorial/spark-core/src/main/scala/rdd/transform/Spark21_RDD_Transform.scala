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
        rdd1.join(rdd2).collect().foreach(println) // (a,(1,4) 、(c,(3,6))
        println("=== join用方法 ===" * 4)
        /*
            TODO leftOuterJoin 用法: 类似于左外连接,显示左边的全部,右表和左边进行关联,没有关联到则右边数据为null
                (a,(1,Some(4)))
                (b,(2,None))
                (c,(3,Some(6)))
         */
        rdd1.leftOuterJoin(rdd2).collect().foreach(println)
        println("=== leftOuterJoin用法 ===" * 4)

        /*
            rightOuterJoin结果:
                (a,(Some(1),4))
                (c,(Some(3),6))
                (d,(None,5))
         */
        rdd1.rightOuterJoin(rdd2).collect().foreach(println)
        println("=== rightOuterJoin 用法 ===" * 4)

        /*
            (a,(Some(1),Some(4)))
            (b,(Some(2),None))
            (c,(Some(3),Some(6)))
            (d,(None,Some(5)))
         */
        rdd1.fullOuterJoin(rdd2).collect().foreach(println)
        println("=== fullOuterJoin 用法 ===" * 4)

        /*
            (a,(CompactBuffer(1),CompactBuffer(4)))
            (b,(CompactBuffer(2),CompactBuffer()))
            (c,(CompactBuffer(3),CompactBuffer(6)))
            (d,(CompactBuffer(),CompactBuffer(5)))
         */
        rdd1.cogroup(rdd2).collect().foreach(println)
        println("=== cogroup 用法 ===" * 4)

        sc.stop()

    }

}
