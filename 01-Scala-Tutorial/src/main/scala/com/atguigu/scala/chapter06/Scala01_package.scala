package com
package atguigu
package scala
package chapter06 {

  // 定义一个子包,当前包在 `chapter06`的内部,可以直接访问父包中的类和对象以及方法.
  package subChapter06 {

    object Sub_Scala01_package {
      def main(args: Array[String]): Unit = {
        // 调用父包中的方法.
        Scala01_package.test()  // Scala01_package ... test....
        // 调用 package对象中共有的部分,这里调用的是变量和函数.
        println(password) // 000000
        testPackage() // testPackage....
      }
    }
  }

  /**
   * Scala - 面向对象编程 - 包
   */
  object Scala01_package {
    def main(args: Array[String]): Unit = {
      /*
         Java的包:
             1. 管理类
             2. 区分类
                   java.util.Date
                   java.sql.Date

             3. 包权限

         Scala的包:
             1. Scala的包和类的物理路径没有直接关系

             2. package可以声明多次， 且可以嵌套声明

             3. 子包中的类可以直接访问父包中的内容。

             4. 可以声明包对象，将通用的内容放到包对象中。 当前包下的所有类都可以直接访问包对象的内容 .
       */

      println(password)
      testPackage()
    }

    def test(): Unit = {
      println("Scala01_package ... test....")
    }
  }

}
