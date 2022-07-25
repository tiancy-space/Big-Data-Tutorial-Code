## 1、ODS层采集到的数据经过数据清洗到DWD层. 

### 1.1 ODS原始数据格式

`ods/baseadlog.log` 基础广告日志文件中的数据
```json
{
    "adid": "0",
    "adname": "注册弹窗广告0",
    "dn": "webA"
}
```

| 字段名称 |                   |
| -------- | ----------------- |
| adid     | 基础广告表 广告id |
| adname   | 广告详情名称      |
| dn       | 网站分区          |

`/ods/baswewebsite.log` 基础网站文件中的数据

```json
{
    "createtime": "2000-01-01",
    "creator": "admin",
    "delete": "0",
    "dn": "webA",
    "siteid": "0",
    "sitename": "百度",
    "siteurl": "wwww.baidu.com/webA"
}
```

| 字段名称   | 字段代表含义 |
| ---------- | ------------ |
| createtime |              |
| creator    |              |
| delete     |              |
| dn         | 网站分区     |
| siteid     | 网站id       |
| sitename   | 网站名字     |
| siteurl    | 网站地址     |

`/ods/member.log` 用户数据

```json
{
    "ad_id": "9", // 广告id
    "birthday": "1997-11-16", // 出生日期
    "dn": "webA", // 网站分区
    "dt": "20190722", //日期分区
    "email": "test@126.com",
    "fullname": "王0", // 用户姓名
    "iconurl": "-",
    "lastlogin": "-",
    "mailaddr": "-",
    "memberlevel": "1", //用户等级
    "password": "123456", // 用户密码
    "paymoney": "-",
    "phone": "13711235451", // 手机号
    "qq": "10000",
    "register": "2015-04-05",
    "regupdatetime": "-",
    "uid": "0",
    "unitname": "-",
    "userip": "222.42.116.199", // 注册日期
    "zipcode": "-"
}
```






`ods/memberRegtype.log` 用户注册信息数据

```json
{
    "appkey": "-",
    "appregurl": "http:www.webA.com/product/register/index.html",
    "bdp_uuid": "-",
    "createtime": "2018-03-12",
    "dn": "webA",
    "domain": "-",
    "dt": "20190722",
    "isranreg": "-",
    "regsource": "1",
    "uid": "16",
    "websiteid": "3"
}
```



`/ods/pcentermempaymoney.log` 用户支付数据

```json
{
	"dn": "webA",  //网站分区
	"paymoney": "162.54", //支付金额
	"siteid": "1",  //网站id对应 对应basewebsitelog 下的siteid网站
	"dt":"20190722",  //日期分区
	"uid": "4376695",  //用户id
	"vip_id": "0" //对应pcentermemviplevellog vip_id
}
```




`/ods/pcenterMemViplevel.log`  用户vip用户等级

```json
{
	"discountval": "-",
	"dn": "webA",  //网站分区
	"end_time": "2019-01-01",   //vip结束时间
	"last_modify_time": "2019-01-01",
	"max_free": "-",
	"min_free": "-",
	"next_level": "-",
	"operator": "update",
	"start_time": "2015-02-07",  //vip开始时间
	"vip_id": "2",  //vip id
	"vip_level": "银卡"  //vip级别名称
}
```



### 1.2、ODS数据清洗到DWD层

具体实现可以参照 代码`member`包下的`member.controller.DwdMemberController`这个类. 

![image-20220725161945574](https://tiancy-images.oss-cn-beijing.aliyuncs.com/img/202207251619800.png) 



