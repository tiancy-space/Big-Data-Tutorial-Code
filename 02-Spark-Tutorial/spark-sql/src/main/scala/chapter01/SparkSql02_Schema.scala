package chapter01

/**
 * @Description: 演示 DataFrame = RDD[Row] + Schema. 演示表表结构对象.
 * @Author: tiancy
 * @Create: 2022/6/28
 */
object SparkSql02_Schema {
  def main(args: Array[String]): Unit = {

    // 所谓 schema,就是给数据集中添加表头的过程,在声明表头时,需要的字段描述: 字段名称、字段类型、字段的描述、是否可以存储空值.在编码中体现的还是类与对象.

    // 1、先导入一个描述schema的类型的包.
    import org.apache.spark.sql.types._

    // 2、创建 schema的几种方式,也就是声明一个表对象过程,指定表中的字段名称、字段类型、是否可以为空以及元数据的一些属性信息.
    // 这里创建 Schema 表对象的是 StructType这个类.通过调用方法,往当前创建的对象中添加字段的属性.

    // 第一种创建方式: 直接通过创建对象,调用方法即可.
    val schema1: StructType = new StructType()
      .add(new StructField("id", IntegerType, false))
      .add(new StructField("name", StringType, true))
      .add(new StructField("gender", StringType, true))
      .add(new StructField("height", DoubleType, false))
      .add(new StructField("weight", DoubleType, false))

    // 第二种方式: 可以直接通过apply方法: 可以传一个List、Seq、Array这样的对象,对象中是添加字段的名称、字段类型、字段是否为空以及元数据信息.
    val schema2: StructType = StructType.apply(List(
      new StructField("id", IntegerType, false),
      new StructField("name", StringType, true),
      new StructField("gender", StringType, true),
      new StructField("height", DoubleType, false),
      new StructField("weight", DoubleType, false)
    ))

    // 第三种方式,通过Scala中的语法,来往集合中添加字段对象.
    val schema3 = StructType(
      StructField("id", IntegerType, false) ::
        StructField("name", StringType, true) ::
        StructField("gender", StringType, true) ::
        StructField("height", DoubleType, false) ::
        StructField("weight", DoubleType, false) :: Nil
    )

    println(schema1.fields.mkString(","))
  }
}
