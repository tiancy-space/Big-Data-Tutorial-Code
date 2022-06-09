package com.atguigu.scala.chapter11

// 泛型协变[+T]:就是将子类型当成父类型来使用
object Scala03_ScalaGeneric_协变 {
    def main(args: Array[String]): Unit = {
        /*
            TODO 演示泛型协变展示效果:
                通过创建声明为协变泛型的类 Test类, 右边cj和左边变量接收,都使用了 User本身. Test和User类之间没有关系. 泛型的正常用法,左右一致,泛型不可变.
                val test2 : Test[User] = new Test[Parent] // Error
                上述的表述中: 右边创建Test类时,指定了为Parent类型,左边变量接收时指定为 User类型, Parent类型是User类型的父类. 而协变本身的概念: 可以将子类型当成夫类型来使用,这里的用法却是:父类型当子类型来用.
         */
        val test1 : Test[User] = new Test[User] // OK
        // 错误原因: 声明的当前类是一个协变泛型类型,也就是 子类当成父类来用 ==> 我们以前理解的多态 . 而这里却是 父类当子类来用,因此编译报错.
        // val test2 : Test[User] = new Test[Parent] // Error
        val test3 : Test[User] = new Test[SubUser]  // OK

    }
    // 定义一个泛型类,指定当前类的泛型是T类型,并且指明了当前类可以被看成父类来使用.
    class Test[+T] {
    }
    // 父类
    class Parent {
    }
    // User类是Parent类的子类
    class User extends Parent{
    }
    // SubUser类是User的子类. 上述的三个类存在继承关系: Parent(爷爷) --> User(父亲) --> SubUser(儿子)
    class SubUser extends User {
    }
}