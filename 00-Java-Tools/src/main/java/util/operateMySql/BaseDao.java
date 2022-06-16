package util.operateMySql;

import bean.User;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个通用类,用来操作MySQL中SQL语句指定的表,实现增删改查操作.
 */
public class BaseDao {

    /**
     * 通用 增、删、改方法
     *
     * @param sql    sql语句
     * @param params sql语句中对应的每个参数
     * @return
     * @throws SQLException
     */
    public static int update(String sql, Object... params) throws SQLException {
        Connection conn = JdbcTools3.getConnection();
        //insert into users(name,password,email,birthday) value(?,?,?,?)
        PreparedStatement pst = conn.prepareStatement(sql);
        //循环设置参数,主要给SQL中的占位符进行赋值操作.
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
        //执行sql
        int i = pst.executeUpdate();
        //释放资源
        JdbcTools3.closeAll(null, pst, conn);
        return i;
    }
    //查询方法  query(User.class ,"sql")

    /**
     * 定义一个查询方法,给定一个要返回的实体类类型,一段查询SQL,以及SQL中的占位符
     *
     * @param clazz
     * @param sql
     * @param params
     * @param <T>
     * @return
     * @throws Exception
     */
    // 定义一个泛型方法
    public static  <T> List<T> query(Class<T> clazz, String sql, Object... params) throws Exception {
        Connection conn = JdbcTools3.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        //设置参数
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
        //执行查询
        ResultSet rs = pst.executeQuery();
        //获取表的结构信息,主要获取查询结果中的表字段.
        ResultSetMetaData metaData = pst.getMetaData();
        //获取表中字段数量，列的数量
        int columnCount = metaData.getColumnCount();
        //创建集合，用于封装所有的实体类
        List<T> list = new ArrayList<>();
        T t = null;
        while (rs.next()) {
            t = clazz.newInstance();//保证实体类中有空参构造器
            for (int i = 0; i < columnCount; i++) {//封装每一个实体类对象
                //获取列名
                String columnLabel = metaData.getColumnLabel(i + 1);
                //获取实体类中的属性（要保证实体类中的属性名与表中的字段名一致）
                Field field = clazz.getDeclaredField(columnLabel);
                //开启私有访问权限
                field.setAccessible(true);
                //把查询到的字段值，设置实体类对应的属性
                field.set(t, rs.getObject(1 + i));
            }
            //把封装的每个实体类对象添加到集合
            list.add(t);
        }
        JdbcTools3.closeAll(rs, pst, conn);
        return list;
    }

    public static void main(String[] args) throws Exception {
        // 查询所有,返回一个集合
        String querySql = "select * from users";
        List<User> list = BaseDao.query(User.class, querySql);
        System.out.println(list);
        // 根据条件查找
        List<User> query = BaseDao.query(User.class, "select * from users where id = ?", 1);
        System.out.println(query);
        System.out.println("=======================================================");
        List<User> query2 = BaseDao.query(User.class, "select * from users where id = ?", 4);
        if (query2.size() > 0) {
            System.out.println(query2.get(0));
        } else {
            System.out.println("空空如也");
        }
    }
}
