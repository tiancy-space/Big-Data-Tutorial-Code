package com.atguigu.scala.chapter11

/*
    TODO 泛型 + 隐式转换结合: 上下文界定使用案例
 */
object ScalaGeneric {
  def main(args: Array[String]): Unit = {
    def f[A: Test](a: A) = println(a)

    implicit val test: Test[User] = new Test[User]
    f(new User())
  }

  class Test[T] {
  }

  class Parent {
  }

  class User extends Parent {
  }

  class SubUser extends User {
  }

}