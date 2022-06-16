package util.operateMySql;

import bean.Employee;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 使用的时候,需要导入 Druid依赖,JdbcTools3这个工具类以及当前类.可以搭配实体类一起使用. 测试通过.
 *  写一个操作数据库的通用类,用于对MySQL数据库的增删改查操作.
 */
public class MySQLUtil {
    /**
     * 根据给定的SQL查询MySQL中中的数据,返回一个List.
     *
     * @param clazz  : 查询的结果集封装承对应的实体类
     * @param sql    : 给定的查询SQL
     * @param params : SQL中的占位符,是一个可变参,可以不传
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> query(Class<T> clazz, String sql, Object... params) throws Exception {
        Connection conn = JdbcTools3.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        //设置参数
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        ResultSet rs = null;
        List<T> resList = new ArrayList<>();
        try {
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
            while (rs.next()) {
                //将结果集封装为一个对象
                T obj = clazz.newInstance();
                //通过元数据获取查询的所有列数,对所有列进行遍历
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    //获取列名
                    String columnName = metaData.getColumnName(i);
                    //获取当前列对应的值
                    Object columnValue = rs.getObject(i);
                    //给对象的属性赋值
                    BeanUtils.setProperty(obj, columnName, columnValue);
                }
                //将封装的对象放到List集合中
                resList.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从MySQL中查询出现异常~~");
        } finally {
            JdbcTools3.closeAll(null, ps, conn);
        }
        return resList;
    }

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

    public static void main(String[] args) throws Exception {
        // 测试 MySQLUtil工具类中的查询所有的方法
        List<JSONObject> jsonObjectList = MySQLUtil.query(JSONObject.class, "select * from users");
        System.out.println(jsonObjectList);
        // 根据条件查询
        List<JSONObject> zs = MySQLUtil.query(JSONObject.class, "select * from users where name = ?", "zs");
        if (!zs.isEmpty()) {
            System.out.println(zs.get(0).getString("password"));
        } else {
            System.out.println("空空如也......");
        }
        // 插入某行数据
        int update = MySQLUtil.update("INSERT INTO users (name, password, email, birthday) VALUES (?,?,?,?)", "tiancy", "123456", "tiancy_01@163.com", "1996-01-01");
        System.out.println("插入数据成功: " + update);
        // 删除某行数据
        int deleteById = MySQLUtil.update("delete from users where id = ? ", 3);
        System.out.println("deleteById = " + deleteById);
        // 根据指定条件修改某行数据: eg 修改用户6的名字为 ashe
        int updateById = MySQLUtil.update("UPDATE users t SET t.name = 'ashe' WHERE t.id = ?", 6);
        System.out.println("updateById = " + updateById);

        // 如果给定的实体类和表中查询的字段名称不一致问题,可以在查询表时,给表中字段取别名的方式,从而映射成给定的实体类对象.
        /*
            创建的实体类 Employee : id、userName、password. 对应的表名: users,表中的字段: id、name、password
         */
        List<Employee> query = MySQLUtil.query(Employee.class, "select id,name,password from users");
        System.out.println(query);
        /*
            查询结果: [Employee{id=1, userName='null', password='123321'}, Employee{id=2, userName='null', password='111111'}]
            可以看到: 当前实体类中定义的属性 userName 和 表users中的字段 name不一致,因此在给当前给定的`Employee`对象赋值时,找不到字段,因此结果都为 null.
         */
        // 改进方式: 直接在查询SQL上给当前的查询列起别名.
        List<Employee> list = MySQLUtil.query(Employee.class, "select id, name userName, password from users");
        System.out.println(list);
    }
}
