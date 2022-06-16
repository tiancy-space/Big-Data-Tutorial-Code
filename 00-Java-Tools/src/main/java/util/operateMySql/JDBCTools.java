package util.operateMySql;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/** 当前类是测试数据库连接池的用法,是一个测试样例*/
public class JDBCTools {
    // 1、数据源,即连接池
    private static DataSource dataSource;

    // 2、ThreadLocal对象
    private static ThreadLocal<Connection> threadLocal;

    static {
        try {
            //1、读取druip.properties文件
            Properties pro = new Properties();
            pro.load(JDBCTools.class.getClassLoader().getResourceAsStream("druid.properties"));

            //2、连接连接池
            dataSource = DruidDataSourceFactory.createDataSource(pro);

            //3、创建线程池
			/*
			`ThreadLocal`用于保存某个线程共享变量，原因是在Java中，每一个线程对象中都有一个ThreadLocalMap<ThreadLocal, Object>，
				其key就是一个ThreadLocal，而Object即为该线程的共享变量。而这个map是通过ThreadLocal的set和get方法操作的。
				对于同一个static ThreadLocal，不同线程只能从中get，set，remove自己的变量，而不会影响其他线程的变量。
			1、ThreadLocal.get: 获取ThreadLocal中当前线程共享变量的值。
			2、ThreadLocal.set: 设置ThreadLocal中当前线程共享变量的值。
			3、ThreadLocal.remove: 移除ThreadLocal中当前线程共享变量的值。
			 */
            threadLocal = new ThreadLocal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接的方法
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() {
        // 从当前线程中获取连接
        Connection connection = threadLocal.get();
        if (connection == null) {
            // 从连接池中获取一个连接
            try {
                connection = dataSource.getConnection();
                // 将连接与当前线程绑定
                threadLocal.set(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 释放连接的方法
     */
    public static void releaseConnection() {
        // 获取当前线程中的连接
        Connection connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.close();
                // 将已经关闭的连接从当前线程中移除
                threadLocal.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}