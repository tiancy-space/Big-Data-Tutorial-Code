package util.operateMySql;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

/**
 * description:JDBC工具类2.0 使用了德鲁伊连接池技术,未解决同一个连接的事务问题.
 */

public class JdbcTools2 {
    //连接池
    private static DataSource dataSource;

    static {
        //创建Properties
        Properties prop = new Properties();
        try {
            //读取配置文件中的数据到Properties集合中
            prop.load(JdbcTools2.class.getClassLoader().getResourceAsStream("db.properties"));

           /* //从集合中根据key获取value值
            className = prop.getProperty("driverClassName");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            String initialSize = prop.getProperty("initialSize");
            String maxActive = prop.getProperty("maxActive");
            String maxWait = prop.getProperty("maxWait");

            //创建连接池对象
            dataSource = new DruidDataSource();
            //连接池设置参数
            dataSource.setDriverClassName(className);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setInitialSize(Integer.valueOf(initialSize));
            dataSource.setMaxActive(Integer.valueOf(maxActive));
            dataSource.setMaxWait(Long.valueOf(maxWait));
            */

            /*
              TODO : 直接使用 Druid连接池创建 `dataSource` 对象.
                根据 `properties` 参数来创建德鲁伊连接池,使用时注意: 使用 `DruidDataSourceFactory` 工厂创建的连接池对象时,需要传入一个配置文件.
                并且读取到的配置项的key在 整个`createDataSource` 方法中有特殊指定,不能随便写,要对应.
                配置项中的key必须是这种: driverClassName 、url 、username 、password . 具体的 Druid的配置项,可以看文档声明: 使用Druid连接池配置项.md
             */
            dataSource = DruidDataSourceFactory.createDataSource(prop);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回一个Connection连接对象
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    //释放资源
    public static void closeAll(ResultSet rs, Statement st, Connection conn) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (st != null) {
            st.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        // 1、创建连接
        Connection conn = JdbcTools2.getConnection();
        // 通过占位符 写SQL,修改数据库中的数据.
        PreparedStatement pst = conn.prepareStatement("update users set password=? where name=?");
        // 给占位符赋值.
        pst.setObject(1, "123321");
        pst.setObject(2, "zs");
        int i = pst.executeUpdate();
        System.out.println("i = " + i);
        // 释放资源: 三个参数: 结果集 、PreparedStatement对象 、连接对象.
        JdbcTools2.closeAll(null, pst, conn);
    }
}