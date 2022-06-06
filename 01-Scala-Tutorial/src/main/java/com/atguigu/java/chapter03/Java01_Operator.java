package com.atguigu.java.chapter03;

public class Java01_Operator {
    public static void main(String[] args) {

        String a = new String("abc") ;
        String b = new String("abc") ;

        System.out.println(a == b );     // false  ==比较对象的地址值
        System.out.println(a.equals(b)); // true   equals默认比较对象的地址值. 一般会通过重写用来比较对象的内容.
    }
}
