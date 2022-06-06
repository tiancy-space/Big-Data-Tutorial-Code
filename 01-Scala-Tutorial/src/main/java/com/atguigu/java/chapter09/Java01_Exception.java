package com.atguigu.java.chapter09;

import com.atguigu.scala.chapter09.Scala01_exception;

import java.io.File;
import java.io.FileInputStream;

public class Java01_Exception {
    public static void main(String[] args) {
        //1. 处理异常的方式:  throw 、 throws 、 try...catch ...finally

        //new FileInputStream(new File("")) ;

        try {
            int a = 10;
            int b = 0;
            int c = a / b;

        } catch (ArithmeticException e){
            // catch时，需要将范围小的写到前面
            e.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();

        } finally {
            System.out.println("finally");
        }


        try {
            Scala01_exception.testException();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
