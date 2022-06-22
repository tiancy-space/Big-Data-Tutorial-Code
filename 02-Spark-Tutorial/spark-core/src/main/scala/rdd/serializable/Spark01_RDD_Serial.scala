package rdd.serializable

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Serial {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName("RDD Instance")
        val sc = new SparkContext(conf)

        // TODO RDD的行动算子
        val rdd = sc.makeRDD(
            List(
                "Spark", "Scala", "Hadoop", "Hive"
            ), 2
        )

//        rdd.filter(
//            _.startsWith("S")
//        ).collect().foreach(println)

        val search = new Search("H")

        search.matchString(rdd).collect().foreach(println)

        sc.stop()

    }
    // 定义一个类,用来实现过滤功能.
    class Search( s : String ) {
        // 定义一个方法,用来判断当前rdd中是否以当前类的成员变量开头的.
        def matchString( rdd : RDD[String] ) = {
            val q : String = s
            rdd.filter(_.startsWith(q))
        }
    }

}