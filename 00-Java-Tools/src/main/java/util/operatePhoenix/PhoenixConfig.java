package util.operatePhoenix;

public class PhoenixConfig {
    // 定义表名
    public static final String HBASE_SCHEMA ="GMALL0609_REALTIME";
    // 声明HBase中在集群中的位置
    public static final String PHOENIX_SERVER ="jdbc:phoenix:hadoop202,hadoop203,hadoop204:2181";
    // 声明ClickHouseURL
    public static final String CLICKHOUSE_URL ="jdbc:clickhouse://hadoop202:8123/default";

}