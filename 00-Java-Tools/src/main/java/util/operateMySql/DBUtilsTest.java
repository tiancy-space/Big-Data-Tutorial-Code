package util.operateMySql;

import bean.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 测试  Apache 的 提供的DBUtils工具类,当前类提供了操作数据库的增删改查方法.
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，它是对JDBC的简单封装，学习成本极低，并且使用dbutils能极大简化jdbc编码的工作量，同时也不会影响程序的性能。
 * @Author: tiancy
 * @Create: 2022/6/16
 */

/*
    首先导入commons-dbutils依赖.
    （1）可以实现增、删、改、查、批处理、
    （2）考虑了事务处理需要共用Connection。
    （3）该类最主要的就是简单化了SQL查询，它与ResultSetHandler组合在一起使用可以完成大部分的数据库操作，能够大大减少编码量。
 */
public class DBUtilsTest {
    // 使用commons-dbutils提供的jdbc操作,需要先创建一个QueryRunner这样的对象. 需要给定一个数据源
    private static final QueryRunner qr = new QueryRunner(JdbcTools3.getDataSource());

    public static void main(String[] args) throws SQLException {
        // 增
        int insertSQL = qr.update("insert into users(name,password) value(?,?)", "ashe111", "111155");
        // 删
        qr.update("delete from users where id = ?", 17);
        // 改
        qr.update(" UPDATE users t SET t.name = ?, t.email = ?, t.birthday = ? WHERE t.id = ? ", "ashe", "ashe1111111_01@163.com", "2022-06-16", 18);

        // 查询

        /*
            简单查询: 传入的参数: SQL语句、一个 BeanHandler 对象,用于指定当前查询的结果集进行封装. 以及SQL中的占位符.
         */
        User user = qr.query("select * from users where id = ? ", new BeanHandler<User>(User.class), 1);
        System.out.println(user); // User{id=1, name='zs', password='123321', email='zs@sina.com', birthday=1990-12-04}

        /*
            查询多条数据,返回一个结果集 :  new BeanListHandler<User>(User.class)
            * BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
            * BeanListHandler：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里。
            * ScalarHandler：查询单个值对象
            * MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
            * MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
            * ColumnListHandler：将结果集中某一列的数据存放到List中。
            * KeyedHandler(name)：将结果集中的每一行数据都封装到一个Map里，再把这些map再存到一个map里，其key为指定的key。
            * ArrayHandler：把结果集中的第一行数据转成对象数组。
            * ArrayListHandler：把结果集中的每一行数据都转成一个数组，再存放到List中。
         */
        List<User> userList = qr.query("select * from users ", new BeanListHandler<User>(User.class));
        // [User{id=1, name='zs', password='123321', email='zs@sina.com', birthday=1990-12-04}, User{id=2, name='lisi', password='111111', email='lisi@sina.com', birthday=1991-12-04}]
        System.out.println(userList);

        // 查询单个值 ScalarHandler
        String name = qr.query("select name from users where id = ? ", new ScalarHandler<String>(), 15);
        System.out.println("name = " + name);

        // 查询一行,将当前行数据封装成一个map .
        Map<String, Object> map = qr.query("select * from users where id = ?", new MapHandler(), 1);
        System.out.println("map = " + map.get("name")); // 结果是一个map结构.

        Map<String, Object> resMap = qr.query("select * from users", new MapHandler());
        // 如果查询数据有多条,返回查询到的第一行数据. {id=1, name=zs, password=123321, email=zs@sina.com, birthday=1990-12-04}
        System.out.println(resMap);

        // ColumnListHandler(给定表中的列名)
        List<Object> email = qr.query("select * from users", new ColumnListHandler<>("email"));
        // [zs@sina.com, lisi@sina.com, tiancy_01@163.com, tiancy_01@163.com, tiancy_01@163.com]
        System.out.println(email);

    }
}
