package chapter08_sparksql原理刨析

/**
 * @Description: spark SQL 原理剖析: spark catalyst排第一、join排第二、剩下的是其他. 这章节很重要[反复去看,好好整理]. 没有代码,基本都是理论. 可以回去看自己整理的笔记和文档
 *               文档：文档_5_Spark SQL.docx 链接：http://note.youdao.com/noteshare?id=5a095d9786f0eac86fcaab319529f476&sub=88208A13654941F891E9C3B8D81F3B36
 *               整理的笔记: 7_2、Spark-SQL_强化版.md
 *
 * @Author: tiancy
 * @Create: 2022/7/13
 */
object SparkSql01_架构设计 {
  /*
    TODO spark sql 原理刨析: 1、spark sql 架构设计
      spark sql原理上,是加分项,也是自身优势所在的地方.



   */
  def main(args: Array[String]): Unit = {
    // 查看当前spark sql中的 分析器: Analyzer的方式. : 提供一个逻辑查询计划分析器，它使用 [[SessionCatalog]] 中的信息将 [[UnresolvedAttribute]] 和 [[UnresolvedRelation]] 转换为完全化类型的对象.
    import org.apache.spark.sql.catalyst.analysis.Analyzer
  }
}

