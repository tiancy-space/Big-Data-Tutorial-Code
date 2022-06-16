package util.operateMySql;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *  description:JDBC工具类3.0 使用 ThreadLocal,将当前来连接池对象和当前线程进行绑定,解决事务问题.
 *  ThreadLocal类,可以设置或获取一个跟线程绑定的对象
 *  Thread类中有 Map<ThreadLocal,Object>
 *  ThreadLocal提供的set方法：
 *  可以把当前ThreadLocal对象和另一个对象（Connection对象）分别作为 key和value存储到当前线程对象中
 *  get方法可以从当前线程中，根据ThreadLocal对象(key)获取出对应的value
 * 简单理解：
 *  ThreadLocal类对象可以把一个任意的Object对象绑定到当前线程，
 *  而且只能一个对象，如果线程没变，还可以从当前线程中获取出来存入的这个对象
 */
public class JdbcTools3 {
    //连接池
    private static DataSource dataSource;
    //定义ThreadLocal对象,在当前线程上绑定一个连接对象.
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal();

    static {
        //创建Properties
        Properties prop = new Properties();
        try {
            //读取配置文件中的数据到Properties集合中
            prop.load(JdbcTools3.class.getClassLoader().getResourceAsStream("db.properties"));
            //根据properties参数来创建德鲁伊连接池
            dataSource = DruidDataSourceFactory.createDataSource(prop);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回一个Connection连接对象(只要Connection对象没有释放，那么每次获取到的都是同一个)
    public static Connection getConnection() throws SQLException {
        //从当前线程中获取连接对象
        Connection conn = threadLocal.get();
        if (conn == null) {
            conn = dataSource.getConnection();//从连接池获取
            threadLocal.set(conn);//把这个连接对象绑定到当前线程
        }
        return conn;
    }
    // 获取数据源.
    public static DataSource getDataSource(){
        return dataSource;
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
            //从当前线程中移除连接对象
            threadLocal.remove();
        }
    }
}
