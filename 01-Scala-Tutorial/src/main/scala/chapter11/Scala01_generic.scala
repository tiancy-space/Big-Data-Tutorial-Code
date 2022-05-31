package chapter11

/**
  * Scala - 泛型
  */
object Scala01_generic {
  def main(args: Array[String]): Unit = {
    // 语法:
    // Java  ： <>
    // Scala :  []

    //1. 泛型不可变
    val parentTest: Test[Parent] = new Test[Parent]
    val userTest: Test[User] = new Test[User]
    val subUserTest: Test[SubUser] = new Test[SubUser]

    //2. 泛型协变 ：将子类型当成父类型来使用.
    val parentTest1: Test1[Parent] = new Test1[User]
    val userTest1: Test1[User] = new Test1[SubUser]
    val subUserTest1: Test1[SubUser] = new Test1[SubUser]

    //3. 泛型逆变 ：将父类型当成子类型来使用
    //val parentTest2: Test2[Parent] = new Test2[User]
    val userTest2: Test2[User] = new Test2[Parent]
    val subUserTest2: Test2[SubUser] = new Test2[User]


    //4. 泛型上下限
    val parent = new Parent
    val user = new User
    val subUser = new SubUser

    //上限
    //test[Parent](parent)
    test[User](user)
    test[User](subUser)
    test[SubUser](subUser)

    //下限
    test1[Parent](parent)
    test1[User](user)
    //test1[SubUser](subUser)



  }
  // 泛型上限   A <= User
  def  test[A <: User]( a : A ): Unit = {
    println(a)
  }
  //泛型下限   A >= User
  def  test1[A >: User]( a : A ): Unit = {
    println(a)
  }


}

class Test2[-T]{}

class Test1[+T]{}

class Test[T] {
}

class Parent {
}

class User extends Parent{
}

class SubUser extends User {
}
