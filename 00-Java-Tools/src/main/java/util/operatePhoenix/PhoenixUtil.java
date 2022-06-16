package util.operatePhoenix;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoenixUtil {

    private static Connection conn;

    private static void initConnection() throws Exception {
        //注册驱动
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        //创建连接
        conn = DriverManager.getConnection(PhoenixConfig.PHOENIX_SERVER);
        //设置操作的schema
        conn.setSchema(PhoenixConfig.HBASE_SCHEMA);
    }
    /**
     * 从Phoenix表中查询数据
     * @param sql  查询语句
     * @param clz   将结果集封装的对象类型
     * @return 查询结果
     */
    public static <T> List<T> queryList(String sql, Class<T> clz){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> resList = new ArrayList<>();
        try {
            if(conn == null){
                initConnection();
            }
            //获取数据库操作对象
            ps = conn.prepareStatement(sql);
            //执行SQL语句
            //    +-----+----------+
            //    | ID  | TM_NAME  |
            //    +-----+----------+
            //    | 13  | bb       |
            //    | 14  | cc       |
            rs = ps.executeQuery();
            //根据查询结果集  获取元数据信息
            ResultSetMetaData metaData = rs.getMetaData();
            //处理结果集
            while(rs.next()){
                //将结果集封装为一个对象
                T obj = clz.newInstance();
                //通过元数据获取查询的所有列数,对所有列进行遍历
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    //获取列名
                    String columnName = metaData.getColumnName(i);
                    //获取当前列对应的值
                    Object columnValue = rs.getObject(i);
                    //给对象的属性赋值
                    BeanUtils.setProperty(obj,columnName,columnValue);
                }
                //将封装的对象放到List集合中
                resList.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从Phoenix表中查询数据发生异常~~");
        } finally {
            //释放资源
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resList;
    }

    // 暂时还没用,没有测试. 原理和操作MySQL一致.
    public static void main(String[] args) {
        List<JSONObject> jsonObjList = queryList("select * from dim_base_trademark", JSONObject.class);
        System.out.println(jsonObjList);
    }

}
