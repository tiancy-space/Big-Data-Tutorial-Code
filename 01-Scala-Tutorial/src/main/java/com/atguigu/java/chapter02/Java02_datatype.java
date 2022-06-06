package com.atguigu.java.chapter02;

public class Java02_datatype {
    public static void main(String[] args) {
        char c = 'A' + 1 ; // 常量的运算在编译期就可以进行运算.
        System.out.println(c);  // 'B'


        char cc = 'A' ;

        char ccc =(char)(cc + 1) ; //变量的运算在运行期进行运算
    }
}
