package demand_analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 学生成绩统计
 * @Author: tiancy
 * @Create: 2022/6/27
 */
object Spark04_StudentMark {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("student").setMaster("local[*]")
    val sc = new SparkContext(conf)
    /*
      数据格式：班级 姓名 年龄 性别 科目 分数
              12 张三 25 男 chinese 50
     */
    val lineRDD: RDD[String] = sc.textFile("./02-Spark-Tutorial/data/student.txt")


    var studentRDDMap: RDD[Student] = lineRDD.map(
      line => {
        val fieldsArray: Array[String] = line.split(" ")
        Student(
          fieldsArray(0).toInt,
          fieldsArray(1),
          fieldsArray(2).toInt,
          fieldsArray(3),
          fieldsArray(4),
          fieldsArray(5).toDouble,
        )
      }
    )

    val studentRDD: RDD[Student] = studentRDDMap.cache()

    /*
      TODO 1、人数统计
     */
    // 一共多少人参加考试 : 核心处理数据的思路: 先进行聚合,减少数据量.
    val totalStudent: Long = studentRDD.map(
      stu => (stu.studentName, 1)
    ).reduceByKey(_ + _).count()
    println(s"一共 $totalStudent 个人参加考试")

    val groupByAgeCount: RDD[((String, Int), Int)] = studentRDD.map(
      stu => ((stu.studentName, stu.age), 1)
    )
    //    一共有多少个等于20岁的人参加考试？
    val eq20Ctn: Long = groupByAgeCount.filter(_._1._2 == 20).reduceByKey(_ + _).count()
    println(s"一共 $eq20Ctn 个 等于20岁的同学参加考试")
    //    一共有多少个小于20岁的人参加考试？
    val lt20Ctn: Long = groupByAgeCount.filter(_._1._2 < 20).reduceByKey(_ + _).count()
    println(s"一共 $lt20Ctn 个 小于20岁的同学参加考试")
    //    一共有多少个大于20岁的人参加考试？
    val mt20Ctn: Long = groupByAgeCount.filter(_._1._2 > 20).reduceByKey(_ + _).count()
    println(s"一共 $eq20Ctn 个 大于20岁的同学参加考试")

    //    一共有多个男生参加考试？
    val manCtn: Long = studentRDD.map(stu => ((stu.studentName, stu.sex), 1)).filter(_._1._2 == "男").reduceByKey(_ + _).count()
    println(s"一共 $manCtn 个男同学参加考试")
    //    一共有多少个女生参加考试？
    val woManCtn: Long = studentRDD.map(stu => ((stu.studentName, stu.sex), 1)).filter(_._1._2 == "女").reduceByKey(_ + _).count()
    println(s"一共 $woManCtn 个男同学参加考试")

    //    12班有多少人参加考试？ 可以尝试使用一下groupBy.
    val className12Ctn: Long = studentRDD.map(stu => ((stu.classId, stu.studentName), 1)).groupByKey().filter(_._1._1 == 12).count()
    println(s" 12班有 $className12Ctn 人参加考试")


    /*
        TODO 2.平均成绩,下面的这三个功能,可以写在一起,实现方式: 可以使用 map + reduceByKey. 也可以使用 combineByKey
     */

    //    语文科目的平均成绩是多少？
    //    数学科目的平均成绩是多少？
    //    英语科目的平均成绩是多少？
    // 使用 map + reduceByKey实现平均值.
    val subjectNameRDD: RDD[(String, (Double, Int))] = studentRDD.map(stu => {
      (stu.subjectName, (stu.score, 1))
    })
    println("各科的平均成绩 +++++++++++++++++++++++++++++++++++++++++++")
    subjectNameRDD.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    ).collect().foreach(println)

    // 使用 combineByKey实现科目平均值.
    println("======== combineByKey ========")
    studentRDD.map(stu => (stu.subjectName, stu.score)).combineByKey(
      // 相同key的第一个元素如何转换
      t => (t, 1), // 成绩 ==> (成绩,1)
      // 分区内 相同key的 第一个和第二个元素操作逻辑
      (t1: (Double, Int), v: Double) => (t1._1 + v, t1._2 + 1), // (分数,1) + 分数 ==> 分数相加, 统计元素值的 1 加 1
      // 分区间相同key的 两个元素的操作逻辑
      (t1: (Double, Int), t2: (Double, Int)) => (t1._1 + t2._1, t1._2 + t2._2)
    ).collect().foreach(println)

    // 单个人平均成绩是多少？ ==> 将数据格式转化成: (名字,(成绩,1)) + reduceByKey操作.
    println("单人平均成绩 =======")
    studentRDD.map(stu => (stu.studentName, (stu.score, 1))).reduceByKey(
      (t1, t2) => (t1._1 + t2._1, t1._2 + t2._2)
    ).collect().foreach(println)

    //12班平均成绩是多少？ : 平均成绩: 总分数 / 总人数. 也可以等于 每个人的每科分数总和 / 总条数. (12班,(分数,1))
    println("12班学成的平均成绩 =================================================================================")
    studentRDD.map(stu => (stu.classId, (stu.score, 1))).filter(_._1 == 12).reduceByKey(
      (t1, t2) => (t1._1 + t2._1, t1._2 + t2._2)
    ).collect().foreach(println)

    /*
        TODO 总成绩相关.
     */
    // 12班男生平均总成绩是多少？ 求男生平均总成绩. 各科总成绩 / 总人数.
    /** 12班男生每个男生平均总成绩 */
    println("12班男生每个男生的总成绩 ******** ********* ********")
    val studentScoresRDD: RDD[(String, Iterable[Double])] = studentRDD.filter(stu => {
      stu.sex == "男" && stu.classId == 12
    }).map(stu => (stu.studentName, stu.score)).groupByKey()
    studentScoresRDD.mapValues(
      iter => iter.toList.sum
    ).map(t => (1, (t._2, 1))).reduceByKey(
      (t1, t2) => (t1._1 + t2._1, t1._2 + t2._2)
    ).map(t => t._2._1 / t._2._2).collect().foreach(println)

    //    12班女生平均总成绩是多少？
    //    同理求13班相关成绩

    /*
        TODO 求最高分
     */
    //    全校语文成绩最高分是多少？ 简单的思路: 根据科目语文的进行分组,迭代器转化为List,使用 max .
    studentRDD.map(
      stu => (stu.subjectName, stu.score)
    ).filter(_._1 == "chinese").groupByKey().mapValues(iter => iter.toList.max).collect().foreach(println)

    //    12班语文成绩最低分是多少？
    //    13班数学最高成绩是多少？

    /*
        TODO 5、求总评成绩 + 过滤组合
          总成绩大于150分的12班的女生有几个？
          总成绩大于150分，且数学大于等于70，且年龄大于等于19岁的学生的平均成绩是多少？
     */
    //  总成绩大于150分的12班的女生有几个？
    studentRDD.filter(
      stu => {
        stu.classId == 12 && stu.sex == "女"
      }
    ).map(stu => (stu.studentName, stu.score)).groupByKey().mapValues(iter => iter.toList.sum).filter(_._2 > 150.0).collect().foreach(println)

    // 总成绩大于150分，且数学大于等于70，且年龄大于等于19岁的学生的平均成绩是多少？

    val value: RDD[((String, Int), Iterable[Student])] = studentRDD.groupBy(x => (x.studentName, x.age)).filter(x => x._1._2 >= 19) // 先按照 (姓名和年龄)作为key进行分组.
    val qu11 = value
      .map(x => (x._1, x._2.map(s => s.score).toList)) // 分组后结构转换: (姓名,年龄),(分数,分数,分数)
      .filter(x => x._2.sum > 150 && x._2(1) >= 70) // 分数求和
      .mapValues(x => x.sum / x.size).collect().mkString(", ")
    println("qu11: " + qu11)

    sc.stop()
  }

  case class Student(classId: Int, studentName: String, age: Int, sex: String, subjectName: String, score: Double)

}
