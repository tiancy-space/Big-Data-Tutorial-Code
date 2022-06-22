package rdd.action

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Spark行动算子功能演示:collect、foreach、reduce、aggregate、fold、first、take、takeOrdered、count、countByKey、countByValue、save
 */
object Spark01_RDD_Action_Collect {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子
        val rdd = sc.makeRDD(
            List(1,2,3,4), 2
        )

        // MR : 一个任务就是一个Job . Spark中一个行动算子就是一个Job.
        // 行动算子会触发RDD的计算逻辑的执行，不会产生新的RDD，而是可以获取结果
        // 每一个行动算子的执行，会触发新的Job的执行,将Executor端执行的结果,拉取到Driver端进行操作
        val ints: Array[Int] = rdd.collect()
        // 本地遍历当前结果
        for (elem <- ints) {
            println(elem)
        }
        sc.stop()
    }
}