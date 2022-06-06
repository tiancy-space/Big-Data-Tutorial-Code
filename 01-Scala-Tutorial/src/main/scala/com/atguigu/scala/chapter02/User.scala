package com.atguigu.scala.chapter02

/**
  * 序列化 ： 将内存中的对象转换成字节序列
  * 反序列化: 将字节序列转换成内存中的对象
  *
  * Scala的序列化还是使用Java中的Serializable接口
  */
class User extends  Serializable {

  var username: String = "zhangsan"
}
