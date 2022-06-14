## 一 Scala入门、环境搭建



#### <strong style="color:#00b0f0;">1. Scala简介</strong> 

##### 1.1 概述

> **Scala**(斯嘎拉)这个名字来源于"Scalable Language(可伸缩的语言)", 它是一门基于JVM的多范式编程语言, 通俗的说: **Scala是一种运行在JVM上的函数式的面向对象语言**. 

> 之所以这样命名, 是因为它的设计目标是: 随着用户的需求一起成长. Scala可被广泛应用于各种编程任务, 从编写小型的脚本到构建巨型系统, 它都能胜任. 

> 正因如此, Scala得以提供一些出众的特性, 例如: **它集成了面向对象编程和面向函数式编程的各种特性, 以及更高层的并发模型**. 

> 总而言之,  Scala融汇了许多前所未有的特性, 而同时又运行于JVM之上, 随着开发者对Scala的兴趣日增, 以及越来越多的工具支持, 无疑Scala语言将成为你手上一门必不可少的工具. 
>

​	`基于JVM解释:Scala的运行环境和Java类似, 也是依赖JVM的.`

​	`多范式解释: Scala支持多种编程风格`

##### 1.2 Scala之父

Scala之父是: <strong style="color:#ff0000;">Martin·Odersky(马丁·奥德斯基</strong>),  他是EPFL（瑞士领先的技术大学）编程研究组的教授. 也是Typesafe公司(现已更名为: Lightbend公司)的联合创始人. **他在整个职业生涯中一直不断追求着一个目标：让写程序这样一个基础工作变得高效、简单、且令人愉悦**. 他曾经就职于IBM研究院、耶鲁大学、卡尔斯鲁厄大学以及南澳大利亚大学. 在此之前，他在瑞士苏黎世联邦理工学院追随Pascal语言创始人Niklaus Wirth(1984年图灵奖获得者)学习，并于1989年获得博士学位. 

##### 1.3 语言特点

**Scala是一门以Java虚拟机（JVM）为运行环境并将面向对象和函数式编程的最佳特性结合在一起的**

**静态类型编程语言（静态语言需要提前编译的如：Java、c、c++等，动态语言如：js）。** 

1. Scala是一门多范式的编程语言，Scala支持面向对象和函数式编程。（多范式，就是多种编程方

   法的意思。有面向过程、面向对象、泛型、函数式四种程序设计方法。） 

2. Scala源代码（.scala）会被编译成Java字节码（.class），然后运行于JVM之上，并可以调用现有

   的Java类库，实现两种语言的无缝对接。 

3. Scala单作为一门语言来看，非常的简洁高效**。**

4. Scala在设计时，马丁·奥德斯基是参考了Java的设计思想，可以说Scala是源于Java，同时马丁·奥

   德斯基也加入了自己的思想，将函数式编程语言的特点融合到JAVA中

总体来说,Scala语言有以下特点

 * Scala是兼容的

   ```兼容Java，可以访问庞大的Java类库，例如：操作mysql、redis、freemarker、activemq等等```

 * Scala是精简的

   ```Scala表达能力强，一行代码抵得上多行Java代码，开发速度快```

 * Scala是高级的

   ```Scala可以让你的程序保持短小, 清晰, 看起来更简洁, 更优雅```

 * Scala是静态类型的

   ```Scala拥有非常先进的静态类型系统, 支持: 类型推断和模式匹配等```

 * Scala可以开发大数据应用程序

   ```例如: Spark程序、Flink程序等等...```

---

#### <strong style="color:#00b0f0;">2.Scala程序和Java程序对比</strong>  

##### 2.1 程序的执行流程对比

**Java程序编译执行流程**

<img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517115533358.png" alt="image-20210517115533358" style="zoom:67%;" />   

**Scala程序编译执行流程**

![image-20210831162627911](https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210831162627911.png)  

##### 2.2 Scala和Java及JVM关系图

<img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517120259636.png" alt="image-20210517120259636" style="zoom: 67%;" />    

---

#### <strong style="color:#00b0f0;">3.Scala 环境搭建</strong> 

##### 3.1 概述

scala程序运行需要依赖于Java类库，那么必须要有**Java运行环境**，scala才能正确执行. 所以要编译运行scala程序,因此需要一下环境: 

- JDK（JDK包含JVM）
- Scala编译器（Scala SDK）   

接下来，需要依次安装以下内容：

- 安装JDK
- 安装Scala SDK
- 在IDEA中安装Scala插件

##### 3.2 安装JDK

安装JDK 1.8 64位版本，并配置好环境变量, 此过程略.

##### 3.3 安装Scala SDK

Scala SDK是scala语言的编译器，要开发scala程序，必须要先安装Scala SDK

本次安装的版本是: 2.12.11

**步骤**

1. 下载Scala SDK.[Scala官网](https://scala-lang.org/)

   下载历史版本的Scala环境:[All previous releases](https://scala-lang.org/download/all.html),可以找见 [Scala 2.12.11版本](https://www.scala-lang.org/download/2.12.11.html),如下图所示

   <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517121450481.png" alt="image-20210517121450481" style="zoom:67%;" />    

2. 安装Scala SDK.

   这里的安装方式有多种

   - 一种直接下载`.msi`文件

     `2.1 双击scala-2.11.12.msi，将scala安装在指定目录, 傻瓜式安装, 下一步下一步即可.`
     `2.2 安装路径要合法, 不要出现中文, 空格等特殊符号.`

   - 另一种下载安装包`scala-2.12.11.zip`,下载压缩包版本的需要手动配置环境变量

     `2.1 解压scala-2.12.11.zip到指定的安装目录,这里以 C:\workSpace\Tools\scala-2.12.11 为例`

     `2.2 配置Scala的环境变量`

     我的电脑 右键 --- 属性 --- 高级 --- 环境变量 --- 系统变量 --- 点击新建 

     ```JAVA
     变量名:SCALA_HOME
     变量值:C:\workSpace\Tools\scala-2.12.11 
     ```

     <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517122433099.png" alt="image-20210517122433099" style="zoom:67%;" />  

     在系统变量中 --- 双击 Path --- 新建 ---输入 `%SCALA_HOME%\bin`  --- 点击确定

     > %SCALA_HOME%\bin

     <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517130736292.png" alt="image-20210517130736292" styl e="zoom:67%;" style="zoom:67%;" />     

3. 测试是否安装成功

   `打开控制台，输入: scala -version`

##### 3.4 安装IDEA scala插件

IDEA默认是不支持scala程序开发的，所以需要在IDEA中安装scala插件, 让它来支持scala语言。

**步骤**

###### 3.4.1 下载指定版本IDEA scala插件.

​	`1. 下载的Scala插件必须和你安装的IDEA版本一致`

​	`2. 官方下载地址: http://plugins.jetbrains.com/plugin/1347-scala`

###### **3.4.2 IDEA配置scala插件**

1. **选择配置 > 选择插件**

   <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517131540972.png" alt="image-20210517131540972" style="zoom:67%;" />  

2. **点击小齿轮 > 选择从本地安装插件**

   <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517131605187.png" alt="image-20210517131605187" style="zoom:67%;" />    

3. **找到下载的插件位置，点击OK**

   <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517131645114.png" alt="image-20210517131645114" style="zoom:67%;" />    

###### 3.4.3 重新启动IDEA

<img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517131724512.png" alt="image-20210517131724512" style="zoom: 50%;" />    

---

#### <strong style="color:#00b0f0;">4. Scala解释器</strong> 

##### 4.1 概述

scala解释器像Linux命令一样，执行一条代码，马上就可以让我们看到执行结果，用来测试比较方便。

我们接下来学习：

- 启动scala解释器
- 在scala解释器中执行scala代码
- 退出scala解释器

##### 4.2 启动scala解释器

要启动scala解释器，只需要以下几步：

- 按住`windows键 + r`
- 输入`scala`即可

<img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517132044972.png" alt="image-20210517132044972" style="zoom:67%;" />  

##### 4.3 执行scala代码

在scala的命令提示窗口中输入`println("hello, world")`，回车执行.

##### 4.4 退出解释器

- **方式一:  点击右上角的"×"**

- **方式二: 输入`:quit`退出 **

---

#### <strong style="color:#00b0f0;">5.**创建** IDEA 项目工程</strong> 

##### 1.创建Maven项目

- 打开 IDEA-> 点击`Create New Project` 

- 创建一个 Maven 工程，并点击 next

- GroupId **** ->ArtifactId 输入 scala->点击 next->点击 Finish

  <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517133433197.png" alt="image-20210517133433197" style="zoom:67%;" />  

- 指定项目工作目录空间

- 默认下，Maven 不支持 Scala 的开发，需要引入 Scala 框架。

  选中自己新建的项目,鼠标右键,点击`Add Framework Support...`

  <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517133849324.png" alt="image-20210517133849324" style="zoom:67%;" />  

  选中`Scala`,点击ok

  <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517134028158.png" alt="image-20210517134028158" style="zoom:50%;" />  

  **注意**：如果是第一次引入框架，Use libary 看不到，需要选择你的 Scala 安装目录，然

  后工具就会自动识别，就会显示 user libary。 

- 创建项目的源文件目录

  右键点击 main 目录->New->点击 Diretory -> 写个名字（比如 scala）。

  右键点击 scala 目录->Mark Directory as->选择 Sources root，观察文件夹颜色发生变化

  <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517134147724.png" alt="image-20210517134147724" style="zoom:67%;" />  

  

##### 2.通过IDEA关联Scala源码

在使用 Scala 过程中，为了搞清楚 Scala 底层的机制，需要查看源码，下面看看如何关

联和查看 Scala 的源码包。

- 查看源码:例如查看 Array 源码。按住 ctrl 键->点击 Array->右上角出现 Attach Soures…

  <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517134356768.png" alt="image-20210517134356768" style="zoom:67%;" />  

  <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517134410991.png" alt="image-20210517134410991" style="zoom:67%;" />  

- 关联源码

  1. 将我们的源码包 scala-sources-2.12.11.tar.gz 拷贝到 D:\Tools\scala-2.12.11\lib 文件夹下，并解压为 scala-sources-2.12.11 文件夹 

  2. 点击 Attach Sources…->选择 D:\Tools\scala-2.12.11\lib\scala-sources-2.12.11，这个文件夹，就可以看到源码了

     <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517134505363.png" alt="image-20210517134505363" style="zoom:67%;" />  

#### 6.**官方编程指南** 

1. 在线查看：https://www.scala-lang.org/

2. 离线查看：解压 scala-docs-2.11.8.zip，可以获得 Scala 的 API 操作。

   <img src="https://tiancy-images.oss-cn-beijing.aliyuncs.com/asSets/image-20210517134713881.png" alt="image-20210517134713881" style="zoom:67%;" />  

