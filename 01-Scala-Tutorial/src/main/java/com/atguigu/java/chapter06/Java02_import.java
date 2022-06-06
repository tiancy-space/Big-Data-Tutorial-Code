package com.atguigu.java.chapter06;

import static com.atguigu.java.chapter06.Test02.* ;

public class Java02_import {

    public static void main(String[] args) {
        Test02.test();
        System.out.println(Test02.name);


        System.out.println(name);
        test();
    }

}


class Test02{

    public static  String  name  = "zhangsan" ;

    public static void test(){
        System.out.println("test....");
    }

}

