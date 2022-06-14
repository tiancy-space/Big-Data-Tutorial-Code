
## 二 Scala基础
[TOC]
### 1、HelloWorld解释

<strong style="color:#ff0000;">**前置知识**</strong> 

> **Java是面向对象的语言. Scala是完全面向对象的语言.**
>
> 举几个Java中常见的例子,Java中的基本数据类型,没有与之对应的对象,或者说直接通过`1`这种方式直接调用是不可取的.而在`Scala`中,真正的做到了`完全面向对象`.
>
> 但是,`Scala`是能和`Java`一起玩的,这里就会有一个兼容问题,因此引入了`object`这种描述`类`的关键字.
>
> **object : 因为Scala是完全面向对象的语言，因此Java中的很多非面向对象的语法在Scala中不存在.**
>
>   *             **例如: static语法 、 基本数据类型  等.**
>   *             **Scala通过object来模拟Java的static语法**

**通过与Java中HollWorld来类比Scala**

![image-20210831173637433](https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210831173637433.png) 

**定义一个object类型的Scala类**

```scala
object Scala01_HelloWorld {
  def main(args: Array[String]): Unit = {
    println("Hello Scala")
  }
}
```

> Scala中有<strong style="color:#c00000;">类型推断</strong>,因此我们在写代码的时候,**方法名、变量名称**会比**类型本身**优先级高.可以看下面的形容
>
> **Scala :   args: Array[String] => 参数名 : 参数类型   =>  我喜欢上了小花,她是女人**
>
> **Java  :   String[] args       => 参数类型  参数名    =>  我喜欢上了一个女人,她叫小花**

**先来描述Scala中的main()方法的写法**

> ```scala
>  def main(args: Array[String]): Unit = {}
> ```
>
>  **def ： define的缩写。 表示定义，声明.**
>
> **main() : 方法的名称为main()方法,括号离定义的是方法的传入的参数名称以及参数类型** 
>
> **Unit : 表示 main( ) 的返回值类型,类似于Java中的`void`,但是也有区别,因为`Unit`本身也是一个对象**

官方对`Unit`的描述

> `Unit` 是 `scala.AnyVal`的子类型。 `Unit` 类型的值只有一个，`()`，并且它在底层运行时系统中不由任何对象表示。返回类型为 `Unit` 的方法类似于声明为 `void` 的 Java 方法

**对于`object 类名 {}`这种定义类的解释,可以看`伴生类和伴生对象`**

### 2、伴生类和伴生对象

> 从JVM的角度来讲,肯定还是需要支持静态语法的,但是在Scala中,在设计层面上,我是完全面向对象的,因此我在编译期间,还是需要有静态化的过程的,来支持`JVM`的规范.
>
> **Scala中采用了`object`修饰的类,来解决`static`问题.**



**2.1、在我们创建Scala类的时候,会发现如下选项**

![image-20210831113540656](https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210831113540656.png) 

**2.2、这里创建两个类,一个用`class`类型,一个用`Object`类型,**

<img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210902094011164.png" alt="image-20210902094011164" style="zoom:67%;" />

**2.3、编译,查看字节码文件**

> `Scala01_Object`这个类最终产生一个**两个.class**文件,而`Scala01_Class`这个类产生了一个字节码文件.
>
> - **Scala01_Object,产生的两个字节码文件名称为`Scala01_Object`、`Scala01_Object$`**
>
>   ```java
>   public final class Scala01_Object{
>   }
>   ```
>
>   ```java
>   public final class Scala01_Object${
>     //定义了一个静态成员变量 
>     public static  MODULE$;
>     //在静态代码块中,通过new()的方式调用了当前类的无参构造,从而Scala01_Object$ 这个类的对象.
>     static{
>       new (); 
>     }
>     //私有的构造器,通过私有构造器,将静态代码块中创建的Scala01_Object$对象赋值给最上面定义的静态成员变量 MODULE$
>     private Scala01_Object$(){
>       MODULE$ = this;
>     }
>   }
>   ```
>
> - **Scala01_Class**
>
>   ```java
>   
>   public final class Scala01_Object{
>   }
>   ```

**2.4、在IDEA中,修改这两个类的代码,添加main(),再编译并通过反编译查看结果**

> 文件数量上,和上面的一种分析结果相同
>
> 即 `Scala01_Class =====> Scala01_Class.class` 一个字节码文件.
>
> `Scala01_Object =====> Scala01_Object.class、Scala01_Object$.class`.再经过反编译后查看内容
>
> - `Scala01_Class `
>
>   ```java
>   public class Scala01_Class{
>     //直接就是一个普通的main()
>     public void main(String[] args){
>     }
>   }
>   ```
>
> - `Scala01_Object`
>
>   ```java
>   public final class Scala01_Object{
>     //被静态修饰的静态方法,在静态方法中通过 Scala01_Object$类.静态成员变量.普通main()
>     //很明显,就是直接调用Scala01_Object$这个类的对象来调用的普通方法.
>     public static void main(String[] paramArrayOfString){
>       Scala01_Object$.MODULE$.main(paramArrayOfString);
>     }
>   }
>   ```
>
>   ```java
>   public final class Scala01_Object${
>     public static  MODULE$;
>     static{
>       new ();
>     }
>     public void main(String[] args){
>     }
>   
>     private Scala01_Object$(){
>       MODULE$ = this;
>     }
>   }
>   ```

**2.5、在IDEA中,修改这两个类的代码,添加打印字符串的操作,再编译并通过反编译查看结果**

> - `Scala01_Class`
>
>   ```java
>   public class Scala01_Class{
>     public void main(String[] args){
>       Predef.MODULE$.println("hello scala_class");
>     }
>   }
>   ```
>
>   
>
> - `Scala01_Object `
>
>   ```java
>   public final class Scala01_Object{
>     public static void main(String[] paramArrayOfString){
>       //可以直接看成是通过调用伴生类`Scala01_Object$`的伴生对象的main()
>       Scala01_Object$.MODULE$.main(paramArrayOfString);
>     }
>   }
>   ```
>
>   ```java
>   public final class Scala01_Object${
>     public static  MODULE$;
>   
>     static{
>       new ();
>     }
>   
>     public void main(String[] args){
>       Predef$.MODULE$.println("hello scala_object");
>     }
>     private Scala01_Object$() { MODULE$ = this; }
>   }
>   ```
>
>   **从上面的具体例子可以看出,`创建 Object`这种的Scala类,编译后会产生两个字节码文件, 以上述类名`Scala01_Object`这个类名举例,最终会产生`Scala01_Object.class以及Scala01_Object$.class`**.

**通过上述的查看结果,可以发现,在Scala中,引入的`Object`这种类型的创建类的方式,是为了和`Java中的static`做适配.换一句话讲,在static main()中会去调用伴生类的伴生对象的普通main()来完成最终的打印效果的**

### 3、变量和数据类型

#### 3.1、注释

> 和Java中定义的完全一致,单行注释、多行注释、文档注释

#### 3.2、常量和变量

变量类比于我们语文上的`代词`,有表示指代 他、她、它作用.

常量：在程序执行的过程中，其值不会被改变的变量

变量, 指的就是在程序的执行过程中,  其值可以发生改变的量.

**在Scala中,定义格式如下**

<strong style="color:#ff0000;">回顾 </strong>：Java 变量和常量语法

```java
//定义变量规则: 变量类型 变量名称 = 初始值
int a = 10
//定义常量规则: final 常量类型 常量名称 = 初始值 
final int b = 20
```

Scala中定义变量以及常量的基本语法

```scala
//定义变量规则: var 变量名 [: 变量类型] = 初始值 
var i:Int = 10
//定义常量规则: val 常量名 [: 常量类型] = 初始值 
val j:Int = 20
```

<strong style="color:#00b050;">**注意：能用常量的地方不用变量**</strong> 

**关于变量和常量的几点补充**

1. 声明变量时，类型可以省略，编译器自动推导，即类型推导

   ```scala
   val sum1: Int = 20
   val sum = 20 // 可以省略变量或者常量的类型
   ```

2. 类型确定后，就不能修改，说明 Scala 是强数据类型语言。

   ```scala
   var name:String = "张三"
   name = 23 // 先定义变量的类型为String,再给name赋值一个Int类型则会报错
   ```

3. 变量声明时，必须要有初始值

   ```scala
   var name:String  // 定义变量,必须赋初值
   ```

4. 在声明/定义一个变量时，可以使用 var 或者 val 来修饰，var 修饰的变量可改变，val 修饰的变量不可改

   ```scala
   val sum = 20
   sum = 30 // 编译报错: 因为声明sum为val,是一个常量,不可修改他的值
   var sum1: Int = 20
   sum1 = 30 // 通过声明sum1为变量,则可以修改值
   ```

5. <strong style="color:#c00000;">var 修饰的对象引用可以改变，val 修饰的对象则不可改变，但对象的状态（值）却是可以改变的</strong>。 

   > **比如：自定义对象、数组、集合等等**

   ```scala
   object TestVar {
     def main(args: Array[String]): Unit = {
       var p = new Person()
       p = null
       val p2 = new Person()
       p2 = null // 这行会报错：val 修饰的对象则不可改变
       p2.name = "Hook" // 这行不会报错：因为对象的状态（值）却是可以改变的
     }
   }
   class Person{
     var name = "Ashe"
   }
   ```

#### 3.3、标识符

Scala 对各种变量、方法、函数等命名时使用的字符序列称为标识符。即：**凡是自己可以起名字的地方都叫标识符。**

> **必须由`大小写英文字母, 数字, 下划线_, 美元符$`, 这四部分任意组合组成,且不能以数字开头**
>
> **理论上想写什么就写什么,如果发现编辑器报错,可以直接换一个,但是不推荐,推荐规范.**

#### 3.4、字符串

**Scala 中的`String` 类型具有来自底层Java String 的方法.**

> **通过点击`String`进去看源码,发现是 object Predef{}这个类中定义,**
>
> **客观来讲就是取别名.当然了也可以理解为是定义scala的数据类型,这个类型的名字就是String,来引用Java中的String.**

```scala
val username: String = "tiancy"
//源码中的String
type String        = java.lang.String
```

**3.4.1、创建字符串对象**

```scala
val username: String = "Ashe"
```

**3.4.2、字符串拼接操作**

```scala
//第一种方式 : 直接通过 + 号拼接
println("hello "+"World")
//第二种方式 : 通过String类的concat()方法
println("hello ".concat("world"))
```

**3.4.3、传值字符串<strong style="color:#c00000;"> printf</strong>**

> **这里的传值字符串,就是给定一个占位符,再通过变量填充的过程**

```scala
//此方法需要两个参数,参数为带有格式的字符串,后面的参数是一个可变参.
printf("username = %s , age = %s ",username,age)
```

**3.4.4、插值字符串**

```scala
println(s"username = $username , age = $age")
```

**3.4.5、 多行字符串**

> 主要用于字符串格式化输出的场景,比如写SQL

```scala
// 按照格式输出
val sql =
      """
        |select
        |   id , name , salary , deptId
        |from emp
        |where
        |   id = 20
        |order by
        |   id desc
        |""".stripMargin
    println(sql)
```

> 还可以和插值字符串一起使用,如下通过占位符的形式对SQL进行赋值

```scala
	val salary = 9999.5  //定义一个变量,再传递给SQL
    var queryById =
      s"""
         |SELECT
         |	t1.did,
         |	t2.dname,
         |	SUM( salary ) AS totalSalary
         |FROM
         |	t_employee t1
         |	JOIN t_department t2 ON t1.did = t2.did
         |GROUP BY
         |	t1.did
         |HAVING
         |	totalSalary > $salary
         |ORDER BY
         |	totalSalary DESC
         |""".stripMargin
    println(queryById)
```

> **其中,`.stripMargin`是调用了一个函数,是通过调用 `stripMargin('|')`,指定了顶个符号`|`**

#### 3.5、输入输出

##### 输入

**1、从控制台输入**

> 使用的是Scala提供的类`StdInt`

```scala
val line = StdIn.readLine()
println(line)
```

**2、从文件中读取**

<strong style="color:#ff0000;">问题写法</strong> 

```scala
def main(args: Array[String]): Unit = {
    //通过文件读取文件,可以直接通过Java的输入流来做.
    val fis = new FileInputStream(new File("B:\\DevTools\\IdeaProjects\\0609_BigData\\Scala\\Input\\input.txt"))
    //使用字符缓冲流+转换流,并指定字符集,使用这种方式获取 StandardCharsets.UTF_8 字符集
    val reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))
    var line1 = ""
    // 在Java中,这种写法可以,判断的就是 : 我们定义的line1是否为空
    //而在Scala中,这种写法判断的是 : (line1 = reader.readLine()) 这个表达式是否为空,这里要切记,表达式也式有结果的,这样判断永远也不为空.
    while ((line1 = reader.readLine()) != null) {
      println(line1)
    }
  }
```

<strong style="color:#00b050;">正确写法</strong> 

```scala
def main(args: Array[String]): Unit = {
    val fis = new FileInputStream(new File("B:\\DevTools\\IdeaProjects\\Input\\input.txt"))
    val reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))
    var flag = true
    while (flag) {
      val result = reader.readLine()
      if (result != null) {
        println(result)
      } else {
        flag = false
      }
    }
  }
```

<strong style="color:#00b050;">读取数据时,推荐写法</strong> 

> 可以通过一个工具来读取.

```scala
def main(args: Array[String]): Unit = {
    //使用迭代器遍历
    val iter = Source.fromFile(new File("B:\\DevTools\\IdeaProjects\\input.txt")).getLines()
    while (iter.hasNext) {
      println(iter.next())
    }
  }
```

##### 输出

**可以直接使用打印流将数据输出,使用此流的场景就是比较灵活,通过`ctrl p`查看参数发现,可以是字节流、也可以是字符流,还可以直接开启自动刷新**

![image-20210831205942810](https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210831205942810.png) 

```scala
def main(args: Array[String]): Unit = {
    //1. 往控制台输出
    println("hello scala")

    //2. 往文件中输出
    // 需求: 从控制台读取输入的内容，将内容写到output目录下的output.txt文件中
    println("请输入: ")
    val line: String = StdIn.readLine()

    //字节流
    val fos =
      new FileOutputStream(new File("D:\\IdeaProjects\\BigData210609\\Scala0609\\output\\output.txt"))

    //包装成字符流
    //new BufferedWriter()
    val writer = new PrintWriter(new OutputStreamWriter(fos,StandardCharsets.UTF_8),true)

    writer.println(line)
    //writer.flush()

    writer.close()
    fos.close()

  }
```



#### 3.6、数据类型

在Scala中,数据类型主要分为两块,一块是`AnyVal`,另一块是`AnyRef`.

AnyVal主要用来描述Java中的基本数据类型.也就是值类型.

需要注意的是 : `Null`也是一个对象.

> **看数据类型的继承图发现,Null也是一个引用数据类型,且是所有Java类的子类,这里是将一个子类对象赋值给父类 String类对象.完全可以**

```scala
//在Scala中,这种写法不会报错,程序能正常执行.
var name : String = null
//下面的写法,虽然编写时不会报错,但是程序不能正常执行,理论上null是引用数据类型,不能用AnyVal类型来接收
var num:Int = null
println(num)
```



#### 3.8、类型转换

> 当Scala程序在进行运算或者赋值动作时,  范围小的数据类型值会自动转换为范围大的数据类型值, 然后再进行计算.例如: 1 + 1.1的运算结果就是一个Double类型的2.1.  而有些时候, 我们会涉及到一些类似于"四舍五入"的动作, 要把一个小数转换成整数再来计算.  这些内容就是Scala中的类型转换.
>

> Scala中的类型转换分为`值类型的类型转换`和`引用类型的类型转换`, 这里我们先重点介绍:`值类型的类型转换`.  
>
> 值类型的类型转换分为:
>
> * 自动类型转换
> * 强制类型转换

#####  自动类型转换

1. 解释

   范围小的数据类型值会自动转换为范围大的数据类型值, 这个动作就叫: 自动类型转换.

   `自动类型转换从小到大分别为:Byte, Short, Char -> Int -> Long -> Float -> Double `

2. 示例代码

   ```Scala
   val a:Int = 3
   val b:Double = 3 + 2.21	//因为是int类型和double类型的值进行计算, 所以最终结果为: Double类型
   val c:Byte = a + 1	//这样写会报错, 因为最终计算结果是Int类型的数据, 将其赋值Byte类型肯定不行.
   ```

##### 强制类型转换

1. 解释

   范围大的数据类型值通过一定的格式(强制转换函数)可以将其转换成范围小的数据类型值, 这个动作就叫: 强制类型转换.

   `注意: 使用强制类型转换的时候可能会造成精度缺失问题!`

2. 格式


```Scala
val/var 变量名:数据类型 = 具体的值.toXxx		//Xxx表示你要转换到的数据类型
```

3. 参考代码

```scala
val a:Double = 5.21
val b:Int = a.toInt
```

##### 类型和String类型之间的相互转换

**1. 值类型的数据转换成String类型**

**格式一**:

```Scala
val/var 变量名:String = 值类型数据 + ""
```

**格式二**: 

```scala
val/var 变量名:String = 值类型数据.toString
```

**示例**

将Int, Double, Boolean类型的数据转换成其对应的字符串形式.

**参考代码**:

```scala
val a1:Int = 10
val b1:Double = 2.1
val c1:Boolean = true

//方式一: 通过和空字符串拼接的形式实现
val a2:String = a1 + ""
val b2:String = b1 + ""
val c2:String = c1 + ""

//方式二: 通过toString函数实现
val a3:String = a1.toString
val b3:String = b1.toString
val c3:String = c1.toString
```

**2. String类型的数据转换成其对应的值类型**

**格式:**

```scala
val/var 变量名:值类型 = 字符串值.toXxx	//Xxx表示你要转换到的数据类型
```

> 注意:  
>
> * String类型的数据转成Char类型的数据, 方式有点特殊, 并不是调用toChar, 而是toCharArray
> * 这点目前先了解即可, 后续我们详细解释









