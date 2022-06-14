package com.atguigu.scala.chapter11

/*
    TODO 泛型逆变[-T]： 将父类型当成子类型来使用. 父类用子类来接收,右边new过程是父类,接收变量是子类型
 */
object Scala04_ScalaGeneric_逆变 {
    def main(args: Array[String]): Unit = {

        val test1 : Test[User] = new Test[User] // OK
        val test2 : Test[User] = new Test[Parent] // OK
        // val test3 : Test[User] = new Test[SubUser]  // Error

    }
    class Test[-T] {
    }
    class Parent {
    }
    class User extends Parent{
    }
    class SubUser extends User {
    }
}