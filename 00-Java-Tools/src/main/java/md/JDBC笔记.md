#  第一章  JDBC

之前我们学习了JavaSE，编写了Java程序，数据保存在变量、数组、集合等中，无法持久化，后来学习了IO流可以将数据写入文件，但不方便管理数据以及维护数据的关系；

后来我们学习了数据库管理软件MySQL，可以方便的管理数据。

那么如何将它俩结合起来呢？即Java程序<==>MySQL，实现数据的存储和处理。

那么就可以使用JDBC技术。

## 一、JDBC概述

### 1.1 JDBC简介

JDBC（Java Database Connectivity）是一种用于执行SQL语句的Java API，可以为多种关系数据库提供统一访问，它由一组用Java语言编写的类和接口组成。JDBC提供了一种基准，据此可以构建更高级的工具和接口，使数据库开发人员能够编写数据库应用程序

JDBC技术主要包含两个部分：

- java.sql包和javax.sql包中的JDBC API

  为了项目代码的可移植性，可维护性，SUN公司从最初就制定了Java程序连接各种数据库的统一接口规范。这样的话，不管是连接哪一种DBMS软件，Java代码可以保持一致性。

- JDBC驱动程序，即各个数据库厂商提供的jar

  因为各个数据库厂商的DBMS软件各有不同，那么内部如何通过sql实现增、删、改、查等管理数据，只有这个数据库厂商自己更清楚，因此把接口规范的实现交给各个数据库厂商自己实现。

![jdbc](imgs\jdbc.png)



###  1.2 JDBC核心组件对象

- **DriverManager：** 此类管理数据库驱动程序列表。使用通信子协议将来自java应用程序的连接请求与适当的数据库驱动程序匹配。
- **Connection：**该界面具有用于联系数据库的所有方法。连接对象表示通信上下文，即，与数据库的所有通信仅通过连接对象。
- **Statement**：使用从此接口创建的对象将SQL语句提交到数据库。除了执行存储过程之外，一些派生接口还接受参数。
- **ResultSet：**在使用Statement对象执行SQL查询后，这些对象保存从数据库检索的数据。它作为一个迭代器，允许我们移动其数据。、

## 二、JDBC开发步骤

### 2.1 JDBC开发步骤

**0、准备工作：**

- 准备数据库与数据

  ```mysql
  -- 创建数据库testJDBC
  create database testJDBC;
  use testJDBC;
  -- 创建表users
  create table users(
    id int primary key auto_increment,
    name varchar(40),
    password varchar(40),
    email varchar(60),
    birthday date
  );
  -- 插入数据
  insert into users(name,password,email,birthday) 
  values ('zs','123456','zs@sina.com','1990-12-04'),
  	('lisi','123456','lisi@sina.com','1991-12-04'),
  	('wangwu','123456','wangwu@sina.com','1989-12-04');	
  ```

- 创建Java项目，并将数据库驱动jar包添加到项目

  ![image-20201228110412735](imgs\image-20201228110412735.png)

  ![image-20201228110811108](imgs\image-20201228110811108.png)

  ![image-20201228105734835](imgs\image-20201228105734835.png)

#### 1、注册驱动

通过驱动管理类`java.sql.DriverManager`来注册驱动

```java
DriverManager.registerDriver(new com.mysql.jdbc.Driver());
```

#### 2、创建Connection连接对象

通过驱动管理类`java.sql.DriverManager`来创建一个数据库的连接对象

```java
Connection conn = 		DriverManager.getConnection("jdbc:mysql://localhost:3306/testJDBC","root","1234");
```

#### 3、创建Statement对象

需要使用类型为`Statement`的对象来构建和提交SQL语句到数据库，并返回结果。

```java
//创建Statement对象
Statement st = conn.createStatement();
//或者创建PrepareStatement对象(推荐)
//ResultSet rs = conn.prepareStatement("select * from users");
```

#### 4、执行sql并返回结果

如果执行查询SQL操作，调用`executeQuery`方法，返回`ResultSet`结果集

```java
ResultSet rs = st.executeQuery("select * from users");
```

如果执行增、删、改SQL操作，调用`executeUpdate`方法，返回整数（受影响的行数）

```java
//int i = st.executeUpdate("insert into users(name,password) value('tom','123456')");
//int i = st.executeUpdate("update users set password='654321' where name='tom'");
//int i = st.executeUpdate("delete from users where name='tom'");
```

#### 5、处理结果

如果是查询操作，返回的是`ResultSet`对象，可以从中解析出数据

```java
while (rs.next()) {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    String password = rs.getString("password");
    String email = rs.getString("email");
    String birthday = rs.getString("birthday");
    System.out.println(id+"-"+name+"-"+password+"-"+email+"-"+birthday);
}
```

如果是增删改操作，返回的是受影响的行数

```java
//System.out.println(i>0?"操作成功":"操作失败");
```

#### 6、释放资源

```java
if(rs != null)
	rs.close();
if(st != null)
    st.close();
if(conn != null)
    conn.close();
```

**开发步骤流程图：**

![1561213163143](imgs/1561213163143.png)

### 2.2 示例代码：

```java
//1.注册驱动
DriverManager.registerDriver(driver);

//2.建立连接
Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testJDBC","root","abc");

//3.获得执行SQL语句的statement
Statement statement = conn.createStatement();

//4.执行sql语句并返回结果
ResultSet resultSet = statement.executeQuery("select * from users");

//5.如果有返回结果集，处理结果集
while(resultSet.next()){
  System.out.print(resultSet.getObject(1));
  System.out.print(resultSet.getObject(2));
  System.out.print(resultSet.getObject(3));
  System.out.print(resultSet.getObject(4));
  System.out.print(resultSet.getObject(5));
  System.out.println("--------------------");
}
//6.释放资源
resultSet.close();
statement.close();
conn.close();
```

示例代码：增、删、改

```java
public class TestJDBC {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//1、注册驱动
		//Class.forName("驱动类的全名称")
		Class.forName("com.mysql.jdbc.Driver");
		
		//2、获取连接，连接数据库
        //TCP/IP协议编程，需要服务器的IP地址和端口号
		//mysql的url格式：jdbc协议:子协议://主机名:端口号/要连接的数据库名
		String url = "jdbc:mysql://localhost:3306/test";//其中test是数据库名
		String user = "root";
		String password = "123456";
		Connection conn = DriverManager.getConnection(url, user, password);
	
		//3、执行sql
		//添加一个部门到数据库的t_department表中
		//(1)编写sql
		String sql = "insert into t_department values(null,'计算部2','计算钞票2')";

		//(2)获取Statement对象
		Statement st = conn.createStatement();
		//(3)执行sql
		int len = st.executeUpdate(sql);
		//(4)处理结果
		System.out.println(len>0?"成功":"失败");
		
		//4、关闭
		st.close();
		conn.close();
	}
}
```

示例代码：查询

```java
public class TestSelect {
	public static void main(String[] args) throws Exception{
		// 1、注册驱动
		Class.forName("com.mysql.jdbc.Driver");

		// 2、连接数据库
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");

		// 3、执行sql
		String sql = "SELECT * FROM t_department";
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(sql);//ResultSet看成InputStream
		while(rs.next()){//next()表示是否还有下一行
			Object did = rs.getObject(1);//获取第n列的值
			Object dname = rs.getObject(2);
			Object desc = rs.getObject(3);
			/*
			int did = rs.getInt("did");//也可以根据列名称，并且可以按照数据类型获取
			String dname = rs.getString("dname");
			String desc = rs.getString("description");
			 */
			
			System.out.println(did +"\t" + dname + "\t"+ desc);
		}

		// 4、关闭
		rs.close();
		st.close();
		conn.close();
	}
}
```

## 三、 JDBC相关API

### 3.1 JDBC核心组件API与与开发步骤详解

1. ### java.sql.DriverManager类

   驱动管理类DriverManager，主要用于注册驱动和创建数据库连接。

   | 返回值            | 方法名                                                   | 含义           |
   | ----------------- | -------------------------------------------------------- | -------------- |
   | void              | `registerDriver(java.sql.Driver driver)`                 | 注册驱动       |
   | static Connection | `getConnection(String url)`                              | 创建数据库连接 |
   | static Connection | `getConnection(String url,java.util.Properties info)`    | 创建数据库连接 |
   | static Connection | `getConnection(String url,String user, String password)` | 创建数据库连接 |

   - #### 注册驱动

     使用驱动管理类`java.sql.DriverManager`来注册驱动

     ```java
     //方式一：不推荐，因会导致驱动类被注册两次（看源码），并且代码强依赖数据库驱动jar
     DriverManager.registerDriver(new com.mysql.jdbc.Driver());
     ```

     ```java
     //方式二:创建了两个Driver对象（见源码），并且强依赖数据库驱动
     new com.mysql.jdbc.Driver();
     ```

     ```java
     //方式三:推荐，反射方式,接收字符串参数，降低了对驱动类的依赖
     Class.forName("com.mysql.jdbc.Driver");
     ```

     > 实际在JDK6之后`DriverManager`就已经可以实现自动注册驱动，如果手动注册了驱动，不再自动注册。
     >
     > 需要驱动包中此位置文件`META-INF/services/java.sql.Driver` 中包含内容：`com.mysql.jdbc.Driver`

   - #### 建立与数据库连接

     加载驱动程序后，可以使用重载方法`getConnection`创建Connection对象，每个Connection对象表示Java程序与数据库之间的一个物理连接。

     ```
     Connection conn = getConnection(String url，String user,String password);
     ```

     **其中不同数据库URL配置不同：**

     | RDBMS  | JDBC驱动程序名称                  | URL格式                                                     |
     | ------ | --------------------------------- | ----------------------------------------------------------- |
     | MySQL  | `com.mysql.jdbc.Driver`           | `jdbc：mysql：//hostname / databaseName`                    |
     | ORACLE | `oracle.jdbc.driver.OracleDriver` | `jdbc：oracle：thin：@ hostname：port Number：databaseName` |
     | DB2    | `com.ibm.db2.jdbc.net.DB2Driver`  | `jdbc：db2：hostname：port Number / databaseName`           |

     > URL格式说明：
     >
     > 协议:子协议://主机名:端口/数据库名？参数名1=参数值1&参数名2=参数值2
     > 其中,如果主机是本机或端口是默认3306端口时，可以缺省。如：jdbc:mysql:///databaseName
     >
     > 示例：jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=utf8（如果JDBC程序与服务器端的字符集不一致，会导致乱码，那么可以通过参数指定服务器端的字符集

     **三种创建连接方法示例：**

     ```java
     //方式一：
     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testJDBC?user=root&password=1234");
     
     //方式二：
     Properties info = new Properties();
     info.setProperty("user", "root");
     info.setProperty("password", "1234");
     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testJDBC",info);
     
     //方式三：（推荐方式）
     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testJDBC","root","1234");
     ```

     

2. ### java.sql.Connection接口

   Connection对象代表一个Java程序与数据库的物理连接，Connection接口的实现在数据库驱动中。所有与数据库交互都是基于连接对象的。

   | 返回值            | 方法名                            | 含义                             |
   | ----------------- | --------------------------------- | -------------------------------- |
   | Statement         | createStatement()                 | 创建操作sql语句的对象。          |
   | PreparedStatement | prepareStatement(String sql)      | 创建预处理sql语句的Statement对象 |
   | void              | setAutoCommit(boolean autoCommit) | 开启/关闭事务的自动提交          |
   | void              | commit()                          | 提交事务                         |
   | void              | rollback()                        | 事务回滚                         |
   | void              | close()                           | 关闭connection，释放资源         |

   

3. ### java.sql.Statement接口

   Statement接口的实现在数据库驱动中。用于执行静态SQL 语句并返回它所生成结果的对象。 

   | 返回值    | 方法名                    | 含义                                                         |
   | --------- | ------------------------- | ------------------------------------------------------------ |
   | ResultSet | executeQuery(String sql)  | 根据查询语句返回结果集。只能执行select语句。                 |
   | int       | executeUpdate(String sql) | 根据执行的DML（insert、update、delete）语句，返回受影响的行数。 |
   | boolean   | execute(String sql)       | 此方法可以执行任意sql语句。返回boolean值，表示是否返回ResultSet结果集。仅当执行select语句，且有返回结果时返回true, 其它语句都返回false; |
   | void      | close()                   | 关闭statement，释放资源                                      |

   

4. ### java.sql.ResultSet接口

   使用Statement对象执行SQL查询的结果会封装到一个ResultSet对象中，ResultSet提供以下API来解析其中的数据

   | 返回值  | 方法名                       | 含义                                                        |
   | ------- | ---------------------------- | ----------------------------------------------------------- |
   | Object  | getObject(int columnIndex)   | 根据序号取值，索引从1开始。                                 |
   | Object  | getObject(String ColomnName) | 根据列名取值。                                              |
   | int     | getInt(int colIndex)         | 以int形式获取ResultSet结果集当前行指定列号值                |
   | int     | getInt(String colLabel)      | 以int形式获取ResultSet结果集当前行指定列名值                |
   | float   | getFloat(int colIndex)       | 以float形式获取ResultSet结果集当前行指定列号值              |
   | float   | getFloat(String colLabel)    | 以float形式获取ResultSet结果集当前行指定列名值              |
   | String  | getString(int colIndex)      | 以String 形式获取ResultSet结果集当前行指定列号值            |
   | String  | getString(String colLabel)   | 以String形式获取ResultSet结果集当前行指定列名值             |
   | Date    | getDate(int columnIndex);    | 以Date形式获取ResultSet结果集当前行指定列号值               |
   | Date    | getDate(String columnName)   | 以Date形式获取ResultSet结果集当前行指定列名值               |
   | void    | close()                      | 关闭ResultSet 对象                                          |
   | boolean | next()                       | 将光标从当前位置向前移一行                                  |
   | boolean | previous()                   | 将光标移动到此 ResultSet 对象的上一行。                     |
   | boolean | absolute(int row)            | 参数是当前行的索引，从1开始根据行的索引定位移动的指定索引行 |
   | void    | afterLast()                  | 将光标移动到末尾，正好位于最后一行之后。                    |
   | void    | beforeFirst()                | 将光标移动到开头，正好位于第一行之前。                      |

   获取不同数据类型数据时，可参考如下java和数据库的常用数据类型对应关系：

   | 数据库中的类型 | java的数据类型 |
   | -------------- | -------------- |
   | tityint        | byte           |
   | smallint       | short          |
   | int            | int            |
   | bigint         | long           |
   | float          | float          |
   | double         | double         |
   | char\varchar   | String         |
   | date           | Date           |

   ```java
   ResultSet resultSet = statement.executeQuery("select * from users");//得到结果集
   //封装结果集
   while(resultSet.next()){
     int id = resultSet.getInt(1);
     String name = resultSet.getString(2);
     String password = resultSet.getString("password");
     Object email = resultSet.getObject("email");
     Date date = resultSet.getDate("birthday");
     
     System.out.println(id+"--"+name+"--"+password+"--"+email+"--"+date);
   }
   ```

   

5. ### 释放资源

   系统资源是有限的，不用的资源需要释放。

   ```java
   if(resultSet!=null){
     resultSet.close(); 
   }
   if (statement != null) {
     statement.close();
   }
   if (conn != null) {
     conn.close();
   }
   ```

   

### 3.2 PreparedStatement解决SQL注入问题

PrepareStatement接口是Statement的子接口，提供了更优秀的功能

| 返回值    | 方法名                                       | 含义                          |
| --------- | -------------------------------------------- | ----------------------------- |
| ResultSet | executeQuery()                               | 执行预处理sql语句的查询操作   |
| int       | executeUpdate()                              | 执行预处理sql语句的增删改操作 |
| void      | setInt(int parameterIndex, int x)            | 设置sql参数                   |
| void      | setFloat(int parameterIndex, float x)        | 设置sql参数                   |
| void      | setString(int parameterIndex, String x)      | 设置sql参数                   |
| void      | setDate(int parameterIndex, java.sql.Date x) | 设置sql参数                   |
| void      | setObject(int parameterIndex, Object x)      | 设置sql参数                   |
| ...       | ...                                          | ...                           |

#### 1、使用Statement的问题

**（1）SQL语句拼接**

```java
//提取用户名和密码变量，模拟登录操作
String name = "tom";
String password = "123456";
String sql =
    "select * from users where name='" + name + "' and password='" + password + "'";
System.out.println("sql="+sql);

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

**（2）SQL注入**

```java
String name = "tom";
String password="' or '1'='1";// 如果登录时键盘录入密码为' or '1'='1时，结果登录成功
String sql =
    "select * from users where name='" + name + "' and password='" + password + "'";
System.out.println("sql="+sql);

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

（3）处理blob等类型的数据

- 如果数据库表中字段定义是blob类型时，需要写入的是二进制数据，即需要通过字节流写入二进制数据，这时无法把数据直接拼接成sql字符串。

#### 2、PreparedStatement解决问题

**（1）避免sql拼接**

```java
String sql = "insert into users(name,password,email,birthday) values(?,?,?,?)";
PreparedStatement pst = conn.prepareStatement(sql);//这里要传带？的sql，然后mysql端就会对这个sql进行预编译

//设置？的具体值
/*pst.setString(1, name);
		pst.setString(2, password);
		pst.setString(3, email);
		pst.setDouble(4, birthday);*/

pst.setObject(1, name);
pst.setObject(2, password);
pst.setObject(3, email);
pst.setObject(4, birthday);

int len = pst.executeUpdate();//此处不能传sql
System.out.println(len);
```

**（2）不会有sql注入问题**

```java
//即使输入' or '1'= '1也没问题
 String sql = "select * from users where name=? and password=?";
String name = "tom";
String password = "123123";
PreparedStatement pstmt = conn.prepareStatement(sql);

pstmt.setString(1,name );
pstmt.setString(2,password );
ResultSet rs = pstmt.executeQuery();
```

（3）处理blob类型的数据（了解）

```java
//pic字段为blob类型
String sql = "insert into users(name,pic) value(?,?)";
PreparedStatement pstmt = conn.prepareStatement(sql);

pstmt.setString(1,"tom" );
pstmt.setBlob(2,new FileInputStream("D:/1.jpeg"));

int i = pstmt.executeUpdate();
System.out.println(i > 0 ? "成功" : "失败");
```

 * 注意两个问题：

   ①my.ini关于上传的字节流文件有大小限制，可以在my.ini中配置变量

   ​	max_allowed_packet=16M

   ②每一种blob有各自大小限制：

   tinyblob:255字节、blob:65k、mediumblob:16M、longblob:4G

### 3.3 JDBC事务处理

参考转账案例

```java
/*
 * mysql默认每一个连接是自动提交事务的。
 * 那么当我们在JDBC这段，如果有多条语句想要组成一个事务一起执行的话，那么在JDBC这边怎么设置手动提交事务呢？
 * (1)在执行之前，设置手动提交事务
 * Connection的对象.setAutoCommit(false)
 * (2)成功：
 * Connection的对象.commit();
 * 失败：
 * Connection的对象.rollback();
 * 
 * 补充说明：
 * 为了大家养成要的习惯，在关闭Connection的对象之前，把连接对象设置回自动提交
 * (3)Connection的对象.setAutoCommit(true)
 * 
 * 因为我们现在的连接是建立新的连接，那么如果没有还原为自动提交，没有影响。
 * 但是我们后面实际开发中，每次获取的连接，不一定是新的连接，而是从连接池中获取的旧的连接，而且你关闭也不是真关闭，
 * 而是还给连接池，供别人接着用。以防别人拿到后，以为是自动提交的，而没有commit，最终数据没有成功。
 */
public class TestTransaction {
	public static void main(String[] args) throws Exception{
		/*
		 * 一般涉及到事务处理的话，那么业务逻辑都会比较复杂。
		 * 例如：购物车结算时：
		 * （1）在订单表中添加一条记录
		 * （2）在订单明细表中添加多条订单明细的记录（表示该订单买了什么东西）
		 * （3）修改商品表的销量和库存量
		 * ...
		 * 那么我们今天为了大家关注事务的操作，而不会因为复杂的业务逻辑的影响导致我们的理解，那么我们这里故意
		 * 用两条修改语句来模拟组成一个简单的事务。
		 * update t_department set description = 'xx' where did = 2;
		 * update t_department set description = 'yy' where did = 3;
		 * 
		 * 我希望这两天语句要么一起成功，要么一起回滚
		 * 为了制造失败，我故意把第二条语句写错
		 * update t_department set description = 'yy' （少了where） did = 3;
		 */
		
		//1、注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		
		//2、获取连接
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
		
		//设置手动提交事务
		conn.setAutoCommit(false);
		
		//3、执行sql
		String sql1 = "update t_department set description = 'xx' where did = 2";
		String sql2 = "update t_department set description = 'yy' did = 3";//这是错的
		
		//使用prepareStatement的sql也可以不带?
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql1);
			int len = pst.executeUpdate();
			System.out.println("第一条：" + (len>0?"成功":"失败"));
			
			pst = conn.prepareStatement(sql2);
			len = pst.executeUpdate();
			System.out.println("第二条：" + (len>0?"成功":"失败"));
			
			//都成功了，就提交事务
			System.out.println("提交");
			conn.commit();
		} catch (Exception e) {
			System.out.println("回滚");
			//失败要回滚
			conn.rollback();
		}
		
		//4、关闭
		pst.close();
		conn.setAutoCommit(true);//还原为自动提交
		conn.close();
	}
}
```



### 3.4  获取自增长键值（了解）

```java
/*
 * 我们通过JDBC往数据库的表格中添加一条记录，其中有一个字段是自增的，那么在JDBC这边怎么在添加之后直接获取到这个自增的值
 * PreparedStatement是Statement的子接口。
 * Statement接口中有一些常量值：
 * （1）Statement.RETURN_GENERATED_KEYS
 * 
 * 要先添加后获取到自增的key值：
 * （1）PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
 * （2）添加sql执行完成后,通过PreparedStatement的对象调用getGeneratedKeys()方法来获取自增长键值，遍历结果集
 * 		ResultSet rs = pst.getGeneratedKeys();
 */
public class TestAutoIncrement {
	public static void main(String[] args) throws Exception{
		//1、注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		
		//2、获取连接
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
		
		//3、执行sql
		String sql = "insert into t_department values(null,?,?)";
		/*
		 * 这里在创建PreparedStatement对象时，传入第二个参数的作用，就是告知服务器端
		 * 当执行完sql后，把自增的key值返回来。
		 */
		PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
		//设置？的值
		pst.setObject(1, "测试部");
		pst.setObject(2, "测试项目数据");
		
		//执行sql
		int len = pst.executeUpdate();//返回影响的记录数
		if(len>0){
			//从pst中获取到服务器端返回的键值
			ResultSet rs = pst.getGeneratedKeys();
			//因为这里的key值可能多个，因为insert语句可以同时添加多行，所以用ResultSet封装
			//这里因为只添加一条，所以用if判断
			if(rs.next()){
				Object key = rs.getObject(1);
				System.out.println("自增的key值did =" + key);
			}
		}
			
		//4、关闭
		pst.close();
		conn.close();
	}
}
```

### 3.5 批处理操作（了解）

```java
/*
 * 批处理：
 * 	批量处理sql
 * 
 * 例如：
 * （1）订单明细表的多条记录的添加
 * （2）批量添加模拟数据
 * ...
 * 
 * 不用批处理，和用批处理有什么不同？
 * 批处理的效率很多
 * 
 * 如何进行批处理操作？
 * （1）在url中要加一个参数
 *     rewriteBatchedStatements=true,默认值false会发送多次sql请求
 *     那么我们的url就变成了  jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true
 *     这里的?，表示?后面是客户端给服务器端传的参数，多个参数直接使用&分割
 * （2）调用方法不同
 * pst.addBatch();
 * int[] all = pst.executeBatch();
 * 
 * 注意：如果批量添加时，insert使用values,不要使用value
 */
public class TestBatch {
	
	public static void main(String[] args) throws Exception{
		long start = System.currentTimeMillis();
		//例如：在部门表t_department中添加1000条模拟数据
		//1、注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		
		//2、获取连接
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true", "root", "123456");
		
		//3、执行sql
		String sql = "insert into t_department values(null,?,?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		
		//设置？的值
		for (int i = 1; i <=1000; i++) {
			pst.setObject(1, "模拟部门"+i);
			pst.setObject(2, "模拟部门的简介"+i);
			
			pst.addBatch();//添加到批处理一组操作中，攒一块处理
/*			if(i % 500 == 0){//有时候也攒一部分，执行一部分
				//2.执行
				pst.executeBatch();
				//3.清空
				pst.clearBatch();
			}*/
		}
		pst.executeBatch();
		
		//4、关闭
		pst.close();
		conn.close();
		
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start));//耗时：821
	}
}
```

# 第二章  数据库连接池

1、什么是数据库连池
连接对象的缓冲区。负责申请，分配管理，释放连接的操作。

2、为什么要使用数据库连接池

不使用数据库连接池，每次都通过DriverManager获取新连接，用完直接抛弃断开，连接的利用率太低，太浪费。
对于数据库服务器来说，压力太大了。我们数据库服务器和Java程序对连接数也无法控制，很容易导致数据库服务器崩溃。

我们就希望能管理连接。
我们可以建立一个连接池，这个池中可以容纳一定数量的连接对象，一开始，我们可以先替用户先创建好一些连接对象，
等用户要拿连接对象时，就直接从池中拿，不用新建了，这样也可以节省时间。然后用户用完后，放回去，别人可以接着用。
可以提高连接的使用率。当池中的现有的连接都用完了，那么连接池可以向服务器申请新的连接放到池中。
直到池中的连接达到“最大连接数”，就不能在申请新的连接了，如果没有拿到连接的用户只能等待。

3、市面上有很多现成的数据库连接池技术：

* JDBC 的数据库连接池使用 javax.sql.DataSource 来表示，DataSource 只是一个接口（通常被称为数据源），该接口通常由服务器(Weblogic, WebSphere, Tomcat)提供实现，也有一些开源组织提供实现：
  * **DBCP** 是Apache提供的数据库连接池，**速度相对c3p0较快**，但因自身存在BUG，Hibernate3已不再提供支持
  * **C3P0** 是一个开源组织提供的一个数据库连接池，**速度相对较慢，稳定性还可以**
  * **Proxool** 是sourceforge下的一个开源项目数据库连接池，有监控连接池状态的功能，**稳定性较c3p0差一点**
  * **BoneCP** 是一个开源组织提供的数据库连接池，速度快，优于DBCP和C3P0，但是已被**HikariCP**取代。
  * **Druid** 是阿里提供的数据库连接池，据说是集DBCP 、C3P0 、Proxool 优点于一身的数据库连接池

4、阿里的德鲁伊连接池技术

（1）加入jar包

例如：druid-1.1.10.jar

（2）代码步骤

第一步：建立一个数据库连接池

第二步：设置连接池的参数

第三步：获取连接

```java
public class TestPool {
	public static void main(String[] args) throws SQLException {
		//1、创建数据源（数据库连接池）对象
		DruidDataSource ds =new DruidDataSource();
		
		//2、设置参数
		//(1)设置基本参数
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/test");
		ds.setUsername("root");
		ds.setPassword("123456");
		
		//(2)设置连接数等参数
		ds.setInitialSize(5);//一开始提前申请好5个连接，不够了，重写申请
		ds.setMaxActive(10);//最多不超过10个，如果10都用完了，还没还回来，就会出现等待
		ds.setMaxWait(1000);//用户最多等1000毫秒，如果1000毫秒还没有人还回来，就异常了
		
		//3、获取连接
		for (int i = 1; i <=15; i++) {
			Connection conn = ds.getConnection();
			System.out.println("第"+i+"个：" + conn);
			
			//如果这里没有关闭，就相当于没有还
//			conn.close();#这里关闭，是还回池中
		}
	}
}
```

| **配置**                      | **缺省** | **说明**                                                     |
| ----------------------------- | -------- | ------------------------------------------------------------ |
| name                          |          | 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。 如果没有配置，将会生成一个名字，格式是：”DataSource-” + System.identityHashCode(this) |
| jdbcUrl                       |          | 连接数据库的url，不同数据库不一样。例如：<br />mysql： jdbc:mysql://10.20.153.104:3306/druid2 <br />oracle：jdbc:oracle:thin:@10.20.149.85:1521:ocnauto |
| username                      |          | 连接数据库的用户名                                           |
| password                      |          | 连接数据库的密码。如果你不希望密码直接写在配置文件中，可以使用ConfigFilter。详细看这里：<https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter> |
| driverClassName               |          | 根据url自动识别 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName(建议配置下) |
| initialSize                   | 0        | 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 |
| maxActive                     | 8        | 最大连接池数量                                               |
| maxIdle                       | 8        | 已经不再使用，配置了也没效果                                 |
| minIdle                       | 0        | 最小连接池数量                                               |
| maxWait                       | -1       | 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。 |
| poolPreparedStatements        | false    | 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。 |
| maxOpenPreparedStatements     | -1       | 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100 |
| validationQuery               |          | 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。 |
| testOnBorrow                  | true     | 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 |
| testOnReturn                  | false    | 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能 |
| testWhileIdle                 | false    | 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 |
| timeBetweenEvictionRunsMillis |          | 有两个含义： 1)Destroy线程会检测连接的间隔时间2)testWhileIdle的判断依据，详细看testWhileIdle属性的说明 |
| numTestsPerEvictionRun        |          | 不再使用，一个DruidDataSource只支持一个EvictionRun           |
| minEvictableIdleTimeMillis    |          |                                                              |
| connectionInitSqls            |          | 物理连接初始化的时候执行的sql                                |
| exceptionSorter               |          | 根据dbType自动识别 当数据库抛出一些不可恢复的异常时，抛弃连接 |
| filters                       |          | 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有： 监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall |
| proxyFilters                  |          | 类型是List，如果同时配置了filters和proxyFilters，是组合关系，并非替换关系 |

# 第三章  封装JDBCTools

配置文件：src/jdbc.properties

```properties
#key=value
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/test
username=root
password=1234
initialSize=5
maxActive=10
maxWait=1000
```

JDBCTools工具类：

```java
package com.atguigu.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/*
 * 获取连接或释放连接的工具类
 */
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
			threadLocal = new ThreadLocal<>();
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
	 * 
	 * @param connection
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
```

其中ThreadLocal的介绍如下：

JDK 1.2的版本中就提供java.lang.ThreadLocal，为解决多线程程序的并发问题提供了一种新的思路。使用这个工具类可以很简洁地编写出优美的多线程程序。通常用来在在多线程中管理共享数据库连接、Session等

ThreadLocal用于保存某个线程共享变量，原因是在Java中，每一个线程对象中都有一个ThreadLocalMap<ThreadLocal, Object>，其key就是一个ThreadLocal，而Object即为该线程的共享变量。而这个map是通过ThreadLocal的set和get方法操作的。对于同一个static ThreadLocal，不同线程只能从中get，set，remove自己的变量，而不会影响其他线程的变量。

1、ThreadLocal.get: 获取ThreadLocal中当前线程共享变量的值。

2、ThreadLocal.set: 设置ThreadLocal中当前线程共享变量的值。

3、ThreadLocal.remove: 移除ThreadLocal中当前线程共享变量的值。

# 第五章  封装BasicDAOImpl

```java
package com.atguigu.test08.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.atguigu.test06.threadlocal.JDBCTools2;

/*
 * 提供增、删、改、查所有表的通用方法
 */
public class BasicDAO1 {
	/*
	 * 通用的增、删、改的方法
	 */
	protected int update(String sql,Object... args) throws SQLException{
		//1、获取连接对象
		Connection conn = JDBCTools2.getConnection();
		//2、编写sql，用形参传入的方式代替
		
		//3、获取Statement或PreparedStatement对象
		PreparedStatement pst = conn.prepareStatement(sql);
		
		//设置？的值
		//因为不知道sql中是否有?，以及？设置为什么值，通过形参来传入
		//Object... args可变形参，可以传入0~n个值
		//如果没有传入，说明没有？需要设置
		//如果传入了n个值，那么说明sql中有n个？需要设置
		if(args!=null  && args.length>0){
			for (int i = 0; i < args.length; i++) {
				//数组的下标从0开始，pst的？的序号是从1开始，所以这里用i+1
				pst.setObject(i+1, args[i]);
			}
		}
		
		//4、执行sql
		int len = pst.executeUpdate();
		//5、关闭		
		pst.close();
		JDBCTools2.free();
		
		return len;
	}
	
	/*
	 * 通用查询多个Javabean对象的方法
	 */
	protected <T> ArrayList<T> getList(Class<T> clazz,String sql,Object... args) throws Exception{
		//1、获取连接
		Connection conn = JDBCTools2.getConnection();
		
		//2、编写sql，由形参传入
		
		//3、获取PreparedStatement对象
		PreparedStatement pst = conn.prepareStatement(sql);
		
		//4、设置？，由形参传入
		if(args!=null  && args.length>0){
			for (int i = 0; i < args.length; i++) {
				//数组的下标从0开始，pst的？的序号是从1开始，所以这里用i+1
				pst.setObject(i+1, args[i]);
			}
		}
		
		//5、执行sql
		ResultSet rs = pst.executeQuery();
		/*
		 * 如何把ResultSet结果集中的数据变成一个一个的Javabean对象，放到ArrayList对象，并且返回
		 */
		ArrayList<T> list = new ArrayList<>();
		/*
		 * 要从ResultSet结果集中获取一共有几行，决定要创建几个对象
		 * 要从ResultSet结果集中获取一共有几列，决定要为几个属性赋值
		 * ResultSet结果集对象中，有一个方法ResultSetMetaData getMetaData()获取结果集的元数据
		 * 元数据就是描述结果集中的数据的数据，例如：列数，列名称等
		 */
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();//获取列数
		
		while(rs.next()){//循环一次代表一行，就要创建一个Javabean对象
			//(1)创建一个Javabean对象
			T t  = clazz.newInstance();//这个方法有要求，要求Javabean这个类要有无参构造
			
			//(2)设置对象的属性值
			/*
			 * 反射操作属性的步骤：
			 * ①获取Class对象，现在有了
			 * ②获取属性对象Field
			 * 		Field f = clazz.getDeclaredField("属性名");
			 * ③创建Javabean对象，已经创建
			 * ④设置属性的可访问性  setAccessible(true)
			 * ⑤设置属性的值
			 */
			for (int i = 0; i < count; i++) {//一共要为count个属性赋值
//				Field f = clazz.getDeclaredField("属性名");
				String fieldName = metaData.getColumnLabel(i+1);//获取第几列的字段名
				Field f = clazz.getDeclaredField(fieldName);
				
				f.setAccessible(true);
				
				f.set(t, rs.getObject(i+1));//rs.getObject(i+1)获取第几列的值
			}
			
			//(3)把Javabean对象放到list中
			list.add(t);
		}
		
		//6、关闭
		rs.close();
		pst.close();
		JDBCTools2.free();
		
		return list;
	}

	protected <T> T getBean(Class<T> clazz,String sql,Object... args) throws Exception{
		return getList(clazz,sql,args).get(0);
	}
}
```

使用BasicDAOImpl实现Employee的增删改查

![1557457111280](imgs/1557457111280.png)

示例代码：EmployeeDAO.java

```java
public interface EmployeeDAO {
	void addEmployee(Employee emp);
	void updateEmployee(Employee emp);
	void deleteByEid(int eid);
	Employee getByEid(int eid);
	ArrayList<Employee> getAll();
}
```

示例代码：EmployeeDAOImpl.java

```java
package com.atguigu.test08.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.atguigu.bean.Employee;

public class EmployeeDAOImpl extends BasicDAO1 implements EmployeeDAO {

	@Override
	public void addEmployee(Employee emp) {
		//`ename`,`tel`,`gender`,`salary`,`commission_pct`,`birthday`,
		//`hiredate`,`job_id`,`email`,`mid`,`address`,`native_place`,`did`
		String sql = "insert into t_employee values(null,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			update(sql, emp.getEname(),emp.getTel(),emp.getGender(),emp.getSalary(),emp.getCommissionPct(),
					emp.getBirthday(),emp.getHiredate(),emp.getJobId(),emp.getEmail(),emp.getMid(),emp.getAddress(),
					emp.getNativePlace(),emp.getDid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateEmployee(Employee emp) {
		String sql = "update t_employee set `ename`=?,`tel`=?,`gender`=?,`salary`=?,`commission_pct`=?,`birthday`=?,`hiredate`=?,`job_id`=?,`email`=?,`mid`=?,`address`=?,`native_place`=?,`did`=? where eid=?";
		try {
			update(sql, emp.getEname(),emp.getTel(),emp.getGender(),emp.getSalary(),emp.getCommissionPct(),
					emp.getBirthday(),emp.getHiredate(),emp.getJobId(),emp.getEmail(),emp.getMid(),emp.getAddress(),
					emp.getNativePlace(),emp.getDid(),emp.getEid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteByEid(int eid) {
		String sql = "delete from t_employee where eid=?";
		try {
			update(sql, eid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Employee getByEid(int eid) {
		//这里sql中通过取别名的方式，来指定对应的Javabean的属性名
		String sql = "select `eid`,`ename`,`tel`,`gender`,`salary`,`commission_pct` commissionPct ,`birthday`,`hiredate`,`job_id` jobId,`email`,`mid`,`address`,`native_place` nativePlace,`did` from t_employee where eid=?";
		Employee emp = null;
		try {
			emp = getBean(Employee.class, sql, eid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
		return emp;
	}

	@Override
	public ArrayList<Employee> getAll() {
		//这里sql中通过取别名的方式，来指定对应的Javabean的属性名
		String sql = "select `eid`,`ename`,`tel`,`gender`,`salary`,`commission_pct` commissionPct ,`birthday`,`hiredate`,`job_id` jobId,`email`,`mid`,`address`,`native_place` nativePlace,`did` from t_employee";
		ArrayList<Employee>  list = new ArrayList<Employee>();
		try {
			list = getList(Employee.class, sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}
}
```

# 第六章 Apache的DBUtils

commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，它是对JDBC的简单封装，学习成本极低，并且使用dbutils能极大简化jdbc编码的工作量，同时也不会影响程序的性能。

其中QueryRunner类封装了SQL的执行，是线程安全的。

（1）可以实现增、删、改、查、批处理、

（2）考虑了事务处理需要共用Connection。

（3）该类最主要的就是简单化了SQL查询，它与ResultSetHandler组合在一起使用可以完成大部分的数据库操作，能够大大减少编码量。

**（1）更新**

public int update(Connection conn, String sql, Object... params) throws SQLException:用来执行一个更新（插入、更新或删除）操作。

......

**（2）插入**

public <T> T insert(Connection conn,String sql,ResultSetHandler<T> rsh, Object... params) throws SQLException：只支持INSERT语句，其中 rsh - The handler used to create the result object from the ResultSet of auto-generated keys.  返回值: An object generated by the handler.即自动生成的键值

....

**（3）批处理**

public int[] batch(Connection conn,String sql,Object[][] params)throws SQLException： INSERT, UPDATE, or DELETE语句

public <T> T insertBatch(Connection conn,String sql,ResultSetHandler<T> rsh,Object[][] params)throws SQLException：只支持INSERT语句

.....

**（4）使用QueryRunner类实现查询**

public Object query(Connection conn, String sql, ResultSetHandler rsh,Object... params) throws SQLException：执行一个查询操作，在这个查询中，对象数组中的每个元素值被用来作为查询语句的置换参数。该方法会自行处理 PreparedStatement 和 ResultSet 的创建和关闭。

....

ResultSetHandler接口用于处理 java.sql.ResultSet，将数据按要求转换为另一种形式。ResultSetHandler 接口提供了一个单独的方法：Object handle (java.sql.ResultSet  rs)该方法的返回值将作为QueryRunner类的query()方法的返回值。

该接口有如下实现类可以使用：

* BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
* BeanListHandler：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里。
* ScalarHandler：查询单个值对象
* MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
* MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
* ColumnListHandler：将结果集中某一列的数据存放到List中。
* KeyedHandler(name)：将结果集中的每一行数据都封装到一个Map里，再把这些map再存到一个map里，其key为指定的key。
* ArrayHandler：把结果集中的第一行数据转成对象数组。
* ArrayListHandler：把结果集中的每一行数据都转成一个数组，再存放到List中。

示例代码：BasicDAOImpl.java

```java
package com.atguigu.test09.dbutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.atguigu.test06.threadlocal.JDBCTools2;

public class BasicDAO2 {
	private QueryRunner qr = new QueryRunner();
	
	protected int update(String sql,Object... args) throws SQLException{
		Connection conn = JDBCTools2.getConnection();
		
		int len = qr.update(conn, sql, args);
		
		//QueryRunner可以帮你关闭连接
		return len;
	}
	
	protected <T> List<T> getList(Class<T> clazz,String sql,Object... args) throws Exception{
		Connection conn = JDBCTools2.getConnection();
		
		/*
		 * ResultSetHandler接口,用于处理 java.sql.ResultSet，将数据按要求转换为另一种形式。
		 * (1)BeanListHandler等形式
		 */
		List<T> list = qr.query(conn, sql, new BeanListHandler<>(clazz), args);
		
		return list;
	}
	
	protected <T> T getBean(Class<T> clazz,String sql,Object... args) throws Exception{
		Connection conn = JDBCTools2.getConnection();
		/*
		 * ResultSetHandler接口,用于处理 java.sql.ResultSet，将数据按要求转换为另一种形式。
		 * (2)BeanHandler等形式
		 */
		T t = qr.query(conn, sql, new BeanHandler<>(clazz), args);
		
		return t;
	}
	
	/*
	 * 通用的查询单个值的方法
	 * 例如：员工总数，最高工资，平均工资等
	 */
	protected Object getObject(String sql,Object... args) throws Exception{
		Connection conn = JDBCTools2.getConnection();
		
		/*
		 * ResultSetHandler接口,用于处理 java.sql.ResultSet，将数据按要求转换为另一种形式。
		 * (3)ScalarHandler：查询单个值对象等形式
		 */
		Object obj = qr.query(conn, sql, new ScalarHandler<>(), args);
		
		return obj;
	}
	
	/*
	 * 通用的查询多行多列的方法
	 * 例如：每个部门的平均工资
	 */
	protected List<Map<String, Object>> getMapList(String sql,Object... args) throws Exception{
		Connection conn = JDBCTools2.getConnection();
		/*
		 * ResultSetHandler接口,用于处理 java.sql.ResultSet，将数据按要求转换为另一种形式。
		 * (4)MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
		 */
		List<Map<String, Object>> list = qr.query(conn, sql, new MapListHandler(), args);
		
		return list;
	}
	
}
```

示例代码：

```java
public interface EmployeeDAO2 {
	void addEmployee(Employee emp);
	void updateEmployee(Employee emp);
	void deleteByEid(int eid);
	Employee getByEid(int eid);
	List<Employee> getAll();
	long empCount();//查询员工总数
	double avgSalary();//查询全公司的平均工资
	
	//key是部门编号，Double是平均工资
	Map<Integer,Double> avgSalaryPerDepartment();
}
```

示例代码：

```java
package com.atguigu.test09.dbutil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atguigu.bean.Employee;

public class EmployeeDAOImpl2 extends BasicDAO2 implements EmployeeDAO2 {

	@Override
	public void addEmployee(Employee emp) {
		//`ename`,`tel`,`gender`,`salary`,`commission_pct`,`birthday`,
		//`hiredate`,`job_id`,`email`,`mid`,`address`,`native_place`,`did`
		String sql = "insert into t_employee values(null,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			update(sql, emp.getEname(),emp.getTel(),emp.getGender(),emp.getSalary(),emp.getCommissionPct(),
					emp.getBirthday(),emp.getHiredate(),emp.getJobId(),emp.getEmail(),emp.getMid(),emp.getAddress(),
					emp.getNativePlace(),emp.getDid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateEmployee(Employee emp) {
		String sql = "update t_employee set `ename`=?,`tel`=?,`gender`=?,`salary`=?,`commission_pct`=?,`birthday`=?,`hiredate`=?,`job_id`=?,`email`=?,`mid`=?,`address`=?,`native_place`=?,`did`=? where eid=?";
		try {
			update(sql, emp.getEname(),emp.getTel(),emp.getGender(),emp.getSalary(),emp.getCommissionPct(),
					emp.getBirthday(),emp.getHiredate(),emp.getJobId(),emp.getEmail(),emp.getMid(),emp.getAddress(),
					emp.getNativePlace(),emp.getDid(),emp.getEid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteByEid(int eid) {
		String sql = "delete from t_employee where eid=?";
		try {
			update(sql, eid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Employee getByEid(int eid) {
		//这里sql中通过取别名的方式，来指定对应的Javabean的属性名
		String sql = "select `eid`,`ename`,`tel`,`gender`,`salary`,`commission_pct` commissionPct ,`birthday`,`hiredate`,`job_id` jobId,`email`,`mid`,`address`,`native_place` nativePlace,`did` from t_employee where eid=?";
		Employee emp = null;
		try {
			emp = getBean(Employee.class, sql, eid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
		return emp;
	}

	@Override
	public List<Employee> getAll() {
		//这里sql中通过取别名的方式，来指定对应的Javabean的属性名
		String sql = "select `eid`,`ename`,`tel`,`gender`,`salary`,`commission_pct` commissionPct ,`birthday`,`hiredate`,`job_id` jobId,`email`,`mid`,`address`,`native_place` nativePlace,`did` from t_employee";
		List<Employee>  list = new ArrayList<Employee>();
		try {
			list = getList(Employee.class, sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	@Override
	public long empCount() {
		String sql = "select count(1) from t_employee";
		Long count = 0L;
		try {
			Object obj = getObject(sql);
			count = (Long) obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return count;
	}

	@Override
	public double avgSalary() {
		String sql = "select avg(salary) from t_employee";
		Double avg = 0.0;
		try {
			avg = (Double) getObject(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return avg;
	}

	@Override
	public Map<Integer, Double> avgSalaryPerDepartment() {
		String sql = "select did,avg(salary) from t_employee group by did";
		
		Map<Integer, Double> map = new HashMap<>();
		try {
			List<Map<String, Object>> mapList = getMapList(sql);
			/*
			 * String：字段的名称，例如：did,avg(salary)
			 * Object：字段的值，例如1，19819.408666666666
			 *  {did=1, avg(salary)=19819.408666666666}
				{did=2, avg(salary)=11708.5}
				{did=3, avg(salary)=70223.0}
				{did=4, avg(salary)=12332.0}
				{did=5, avg(salary)=11065.5}
			 */
			
			for (Map<String, Object> map2 : mapList) {
				map.put((Integer)map2.get("did"),(Double)map2.get("avg(salary)"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return map;
	}

}
```

示例代码：

```java
package com.atguigu.test09.dbutil;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.atguigu.bean.Employee;

public class TestEmployeeDAOImp2 {
	@Test
	public void test1(){
		EmployeeDAOImpl2 ed2 = new EmployeeDAOImpl2();
		List<Employee> all = ed2.getAll();
		for (Employee employee : all) {
			System.out.println(employee);
		}
	}
	
	@Test
	public void test2(){
		EmployeeDAOImpl2 ed2 = new EmployeeDAOImpl2();
		long count = ed2.empCount();
		System.out.println(count);
	}
	
	@Test
	public void test3(){
		EmployeeDAOImpl2 ed2 = new EmployeeDAOImpl2();
		double avgSalary = ed2.avgSalary();
		System.out.println(avgSalary);
	}
	
	@Test
	public void test4(){
		EmployeeDAOImpl2 ed2 = new EmployeeDAOImpl2();
		Map<Integer, Double> map = ed2.avgSalaryPerDepartment();
		map.forEach((k,v) -> System.out.println(k+"->"+v));
	}
}
```



