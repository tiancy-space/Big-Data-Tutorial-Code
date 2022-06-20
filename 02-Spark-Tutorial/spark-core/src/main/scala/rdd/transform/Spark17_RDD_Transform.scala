package rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark17_RDD_Transform {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO 取出每个分区内相同key的最大值然后分区间相加
        // 分区内和分区间进行区分
        // reduceByKey：分区内和分区间的计算规则相同
        val rdd = sc.makeRDD(
            List(
                ("a", 1), ("a", 2), ("b", 3),
                ("b", 4), ("b", 5), ("a", 6)
            ), 2
        )
        /*
          ("a", 1), ("a", 2), ("b", 3)
              => (a, 2), (b, 3)
                    => (a, 8) (b, 8)
              => (b, 5), (a, 6)
          ("b", 4), ("b", 5), ("a", 6)

         */
        // TODO aggregateByKey算子存在函数柯里化
        //  aggregateByKey算子也可以实现WordCount (4 / 10)
        // 存在多个参数列表
        // 第一个参数列表中只有一个参数，表示为 z, zeroValue(初始值)
        //      表示为计算初始值
        // 第二个参数列表中有二个参数
        //      第一个参数表示为相同key的value在分区内计算规则
        //      第二个参数表示为相同key的value在分区间计算规则
//        rdd.aggregateByKey(5)(
//            (x, y) => math.max(x, y),
//            (x, y) => x + y
//        ).collect().foreach(println)

        rdd.aggregateByKey(0)(
            (x, y) => x + y,
            (x, y) => x + y
        ).collect().foreach(println)
        rdd.aggregateByKey(0)(
            _+_,
            _+_
        ).collect().foreach(println)
        // TODO 如果aggregateByKey算子中分区内和分区间的计算规则相同的话，可以简化为另外一个算子
        //  foldByKey算子也可以实现WordCount (5 / 10)
        rdd.foldByKey(0)(_+_).collect().foreach(println)


        sc.stop()

    }
}
