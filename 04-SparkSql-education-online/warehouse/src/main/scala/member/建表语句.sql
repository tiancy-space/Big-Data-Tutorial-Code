create external table `education_online_dwd`.`dwd_member`
(
    uid           int,
    ad_id         int,
    birthday      string,
    email         string,
    fullname      string,
    iconurl       string,
    lastlogin     string,
    mailaddr      string,
    memberlevel   string,
    password      string,
    paymoney      string,
    phone         string,
    qq            string,
    register      string,
    regupdatetime string,
    unitname      string,
    userip        string,
    zipcode       string
)
    partitioned by (
        dt string,
        dn string
        )
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `education_online_dwd`.`dwd_member_regtype`
(
    uid           int,
    appkey        string,
    appregurl     string,
    bdp_uuid      string,
    createtime    timestamp,
    isranreg      string,
    regsource     string,
    regsourcename string,
    websiteid     int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `education_online_dwd`.`dwd_base_ad`
(
    adid   int,
    adname string
)
    partitioned by (
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `education_online_dwd`.`dwd_base_website`
(
    siteid     int,
    sitename   string,
    siteurl    string,
    `delete`   int,
    createtime timestamp,
    creator    string
)
    partitioned by (
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');



create external table `education_online_dwd`.`dwd_pcentermempaymoney`
(
    uid      int,
    paymoney string,
    siteid   int,
    vip_id   int
)
    partitioned by (
        dt string,
        dn string
        )
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `education_online_dwd`.`dwd_vip_level`
(
    vip_id           int,
    vip_level        string,
    start_time       timestamp,
    end_time         timestamp,
    last_modify_time timestamp,
    max_free         string,
    min_free         string,
    next_level       string,
    operator         string
) partitioned by (
    dn string
    )
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `education_online_dws`.`dws_member`
(
    uid                  int,
    ad_id                int,
    fullname             string,
    iconurl              string,
    lastlogin            string,
    mailaddr             string,
    memberlevel          string,
    password             string,
    paymoney             string,
    phone                string,
    qq                   string,
    register             string,
    regupdatetime        string,
    unitname             string,
    userip               string,
    zipcode              string,
    appkey               string,
    appregurl            string,
    bdp_uuid             string,
    reg_createtime       timestamp,
    isranreg             string,
    regsource            string,
    regsourcename        string,
    adname               string,
    siteid               int,
    sitename             string,
    siteurl              string,
    site_delete          string,
    site_createtime      string,
    site_creator         string,
    vip_id               int,
    vip_level            string,
    vip_start_time       timestamp,
    vip_end_time         timestamp,
    vip_last_modify_time timestamp,
    vip_max_free         string,
    vip_min_free         string,
    vip_next_level       string,
    vip_operator         string
) partitioned by (
    dt string,
    dn string
    )
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `education_online_dws`.`dws_member_zipper`
(
    uid        int,
    paymoney   string,
    vip_level  string,
    start_time timestamp,
    end_time   timestamp
) partitioned by (
    dn string
    )
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `education_online_ads`.`ads_register_appregurlnum`
(
    appregurl string,
    num       int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';

create external table `education_online_ads`.`ads_register_sitenamenum`
(
    sitename string,
    num      int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';


create external table `education_online_ads`.`ads_register_regsourcenamenum`
(
    regsourcename string,
    num           int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';


create external table `education_online_ads`.`ads_register_adnamenum`
(
    adname string,
    num    int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';

create external table `education_online_ads`.`ads_register_memberlevelnum`
(
    memberlevel string,
    num         int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';

create external table `education_online_ads`.`ads_register_viplevelnum`
(
    vip_level string,
    num       int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';


create external table `education_online_ads`.`ads_register_top3memberpay`
(
    uid           int,
    memberlevel   string,
    register      string,
    appregurl     string,
    regsourcename string,
    adname        string,
    sitename      string,
    vip_level     string,
    paymoney      decimal(10, 4),
    rownum        int
) partitioned by (
    dt string,
    dn string
    ) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\t';
