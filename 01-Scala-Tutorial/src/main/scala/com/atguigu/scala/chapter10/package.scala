package com.atguigu.scala

package object chapter10 {
  //包对象中
  implicit  class MySQLExt05(mySQL05: MySQL05){
    def select(): Unit ={
      println("select..... 包对象中 ...")
    }
  }

}
