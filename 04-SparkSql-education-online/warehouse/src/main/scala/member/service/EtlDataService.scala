package member.service

import com.alibaba.fastjson.JSONObject
import org.apache.spark.SparkContext
import org.apache.spark.sql.{SaveMode, SparkSession}
import util.ParseJsonData

object EtlDataService {
  /**
   * etl用户注册信息
   *
   * @param ssc
   * @param sparkSession
   */
  def etlMemberRegtypeLog(ssc: SparkContext, sparkSession: SparkSession) = {
    import sparkSession.implicits._ //隐式转换

    /*
      指定Hive中定义的hadoop上数仓存放的位置: 这里放在 `/user/tiancy/ods`目录下.
      将采集到的原始文件上传到自己定义的数仓位置上.具体上传命令如下:
      hadoop dfs -put 原始文件在Linux上的位置 dfs上的目录.
      hadoop dfs -put /user/tiancy/ods/memberRegtype.log /user/tiancy/ods
    */
    ssc.textFile("/user/tiancy/ods/memberRegtype.log") // 读取Hive上指定位置上的文件.
      .filter((item: String) => {
        val obj: JSONObject = ParseJsonData.getJsonData(item)
        // 通过判断当前解析获取的obj对象是否为`JSONObject`,如果不是,则过滤掉.
        obj.isInstanceOf[JSONObject]
      }).mapPartitions((partitoin: Iterator[String]) => {
      partitoin.map((item: String) => {
        val jsonObject: JSONObject = ParseJsonData.getJsonData(item)
        val appkey: String = jsonObject.getString("appkey")
        val appregurl: String = jsonObject.getString("appregurl")
        val bdp_uuid: String = jsonObject.getString("bdp_uuid")
        val createtime: String = jsonObject.getString("createtime")
        val isranreg: String = jsonObject.getString("isranreg")
        val regsource: String = jsonObject.getString("regsource")
        val regsourceName: String = regsource match {
          case "1" => "PC"
          case "2" => "Mobile"
          case "3" => "App"
          case "4" => "WeChat"
          case _ => "other"
        }
        val uid: Int = jsonObject.getIntValue("uid")
        val websiteid: Int = jsonObject.getIntValue("websiteid")
        val dt: String = jsonObject.getString("dt")
        val dn: String = jsonObject.getString("dn")
        (uid, appkey, appregurl, bdp_uuid, createtime, isranreg, regsource, regsourceName, websiteid, dt, dn)
      })
    }).toDF().coalesce(1).write.mode(SaveMode.Append).insertInto("education_online_dwd.dwd_member_regtype")
  }

  /**
   * etl用户表数据
   *
   * @param ssc
   * @param sparkSession
   */
  def etlMemberLog(ssc: SparkContext, sparkSession: SparkSession) = {
    import sparkSession.implicits._ //隐式转换
    ssc.textFile("/user/tiancy/ods/member.log").filter(item => {
      val obj = ParseJsonData.getJsonData(item)
      obj.isInstanceOf[JSONObject]
    }).mapPartitions(partition => {
      /*
        TODO 注意以下两个细节:
         - 1、使用`mapPartitions`这个算子,和`map`算子相比效率更高,是按照每个分区进行操作,强调的是分区数量,而不强调数据条数. 虽然效率更高,但是如果当前分区中的数据量过大,可能存在OOM.
         - 2、注意操作的是迭代器,需要注意,每个迭代器对象只能操作一次,如果操作多次,则后面的操作,当前迭代器对象会是一个`null`.调用算子会出现空指针. 具体案例可以看下面代码: 先通过当前迭代器对象`partition` 调用foreach
         循环遍历结束后,在使用当前的迭代器对象`partition`调用 map算子,就会出现上述的情况: 空指针.
         - 如果想在当前`mapPartitions`算子内,对迭代器对象操作多次,则需要将迭起器对象转化为List[partition.toList],再进行后续操作.
       */
      // partition.foreach(println) // 当前行打开,后面的 `partition.map .. ` 会报空指针.
      partition.map(item => {
        val jsonObject = ParseJsonData.getJsonData(item)
        val ad_id = jsonObject.getIntValue("ad_id")
        val birthday = jsonObject.getString("birthday")
        val email = jsonObject.getString("email")
        val fullname = jsonObject.getString("fullname").substring(0, 1) + "xx"
        val iconurl = jsonObject.getString("iconurl")
        val lastlogin = jsonObject.getString("lastlogin")
        val mailaddr = jsonObject.getString("mailaddr")
        val memberlevel = jsonObject.getString("memberlevel")
        val password = "******"
        val paymoney = jsonObject.getString("paymoney")
        val phone = jsonObject.getString("phone")
        val newphone = phone.substring(0, 3) + "*****" + phone.substring(7, 11)
        val qq = jsonObject.getString("qq")
        val register = jsonObject.getString("register")
        val regupdatetime = jsonObject.getString("regupdatetime")
        val uid = jsonObject.getIntValue("uid")
        val unitname = jsonObject.getString("unitname")
        val userip = jsonObject.getString("userip")
        val zipcode = jsonObject.getString("zipcode")
        val dt = jsonObject.getString("dt")
        val dn = jsonObject.getString("dn")
        // 如果表字段中字段个数小于等于22个,可以使用 tuple元组.
        // import scala.Tuple22,底层也是一个 case class.
        (uid, ad_id, birthday, email, fullname, iconurl, lastlogin, mailaddr, memberlevel, password, paymoney, newphone, qq,
          register, regupdatetime, unitname, userip, zipcode, dt, dn)
      })
    }).toDF().coalesce(2).write.mode(SaveMode.Append).insertInto("education_online_dwd.dwd_member")
    /*
       为什么要使用 `coalesce`来缩减分区呢 ?
        在Spark中,对数据源的读取 分区个数是不能手动控制的,底层走的是Hadoop.不能通过参数指定. 如果不指定`coalesce`缩减分区个数,则默认是一个分区对应一个文件.主要为了避免hadoop上的小文件问题.
        coalesce 底层呢,是一个不带shuffle的算子,也就是不走shuffle,是将当前节点上的`Executor`中的多个任务中的分区进行本地合并.
        也就是说: 使用spark时,读取数据源时的分区个数是没办法控制的,只有在计算时产生shuffle后,才能通过参数指定.
       Spark 走shuffle算子的分区个数,默认是200个.
     */
  }

  /**
   * 导入广告表基础数据
   *
   * @param ssc
   * @param sparkSession
   */
  def etlBaseAdLog(ssc: SparkContext, sparkSession: SparkSession) = {
    import sparkSession.implicits._ //隐式转换
    val result = ssc.textFile("/user/tiancy/ods/baseadlog.log").filter(item => {
      val obj = ParseJsonData.getJsonData(item)
      obj.isInstanceOf[JSONObject]
    }).mapPartitions(partition => {
      partition.map(item => {
        val jsonObject = ParseJsonData.getJsonData(item)
        val adid = jsonObject.getIntValue("adid")
        val adname = jsonObject.getString("adname")
        val dn = jsonObject.getString("dn")
        (adid, adname, dn)
      })
    }).toDF().coalesce(1).write.mode(SaveMode.Overwrite).insertInto("education_online_dwd.dwd_base_ad")
  }

  /**
   * 导入网站表基础数据
   *
   * @param ssc
   * @param sparkSession
   */
  def etlBaseWebSiteLog(ssc: SparkContext, sparkSession: SparkSession) = {
    import sparkSession.implicits._ //隐式转换
    ssc.textFile("/user/tiancy/ods/baswewebsite.log").filter(item => {
      val obj = ParseJsonData.getJsonData(item)
      obj.isInstanceOf[JSONObject]
    }).mapPartitions(partition => {
      partition.map(item => {
        val jsonObject = ParseJsonData.getJsonData(item)
        val siteid = jsonObject.getIntValue("siteid")
        val sitename = jsonObject.getString("sitename")
        val siteurl = jsonObject.getString("siteurl")
        val delete = jsonObject.getIntValue("delete")
        val createtime = jsonObject.getString("createtime")
        val creator = jsonObject.getString("creator")
        val dn = jsonObject.getString("dn")
        (siteid, sitename, siteurl, delete, createtime, creator, dn)
      })
    }).toDF().coalesce(1).write.mode(SaveMode.Overwrite).insertInto("education_online_dwd.dwd_base_website")
  }


  /**
   * 导入用户付款信息
   *
   * @param ssc
   * @param sparkSession
   */
  def etlMemPayMoneyLog(ssc: SparkContext, sparkSession: SparkSession) = {
    import sparkSession.implicits._ //隐式转换
    ssc.textFile("/user/tiancy/ods/pcentermempaymoney.log").filter(item => {
      val obj = ParseJsonData.getJsonData(item)
      obj.isInstanceOf[JSONObject]
    }).mapPartitions(partition => {
      partition.map(item => {
        val jSONObject = ParseJsonData.getJsonData(item)
        val paymoney = jSONObject.getString("paymoney")
        val uid = jSONObject.getIntValue("uid")
        val vip_id = jSONObject.getIntValue("vip_id")
        val site_id = jSONObject.getIntValue("siteid")
        val dt = jSONObject.getString("dt")
        val dn = jSONObject.getString("dn")
        (uid, paymoney, site_id, vip_id, dt, dn)
      })
    }).toDF().coalesce(1).write.mode(SaveMode.Append).insertInto("education_online_dwd.dwd_pcentermempaymoney")
  }

  /**
   * 导入用户vip基础数据
   *
   * @param ssc
   * @param sparkSession
   */
  def etlMemVipLevelLog(ssc: SparkContext, sparkSession: SparkSession) = {
    import sparkSession.implicits._ //隐式转换
    ssc.textFile("/user/tiancy/ods/pcenterMemViplevel.log").filter(item => {
      val obj = ParseJsonData.getJsonData(item)
      obj.isInstanceOf[JSONObject]
    }).mapPartitions(partition => {
      partition.map(item => {
        val jSONObject = ParseJsonData.getJsonData(item)
        val discountval = jSONObject.getString("discountval")
        val end_time = jSONObject.getString("end_time")
        val last_modify_time = jSONObject.getString("last_modify_time")
        val max_free = jSONObject.getString("max_free")
        val min_free = jSONObject.getString("min_free")
        val next_level = jSONObject.getString("next_level")
        val operator = jSONObject.getString("operator")
        val start_time = jSONObject.getString("start_time")
        val vip_id = jSONObject.getIntValue("vip_id")
        val vip_level = jSONObject.getString("vip_level")
        val dn = jSONObject.getString("dn")
        (vip_id, vip_level, start_time, end_time, last_modify_time, max_free, min_free, next_level, operator, dn)
      })
    }).toDF().coalesce(1).write.mode(SaveMode.Overwrite).insertInto("education_online_dwd.dwd_vip_level")
  }
}
