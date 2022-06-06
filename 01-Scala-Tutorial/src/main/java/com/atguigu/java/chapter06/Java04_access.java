package com.atguigu.java.chapter06;

public class Java04_access {

    public static void main(String[] args) {
        //protected: 本类  本包  子类

        //clone方法的提供者   : java.lang.Object

        //clone方法的访问位置 :com.atguigu.java.chapter06.Java04_access


        JavaUser04 javaUser04 = new JavaUser04();

        //javaUser04.clone() ;
    }

}

//
class JavaUser04  /* extends Object */{

    //clone

    public void test() throws CloneNotSupportedException {
        this.clone() ;
    }

}