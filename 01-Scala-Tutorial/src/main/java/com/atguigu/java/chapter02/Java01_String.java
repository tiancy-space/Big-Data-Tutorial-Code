package com.atguigu.java.chapter02;

import java.lang.reflect.Field;

/**
 * 字符串不可变
 *
 * 字符串底层用来维护数据的字符数组的 value变量是final修饰的,不能重新被赋值, 因此字符串对象不可变.
 */
public class Java01_String {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String str = " a b " ;
        System.out.println("trim前: !" +   str + "!"); // ! a b !
        String str1 = str.trim();
        System.out.println("trim后: !" +   str + "!"); // ! a b !
        System.out.println("trim后: !" +   str1 + "!");// !a b!

        // 反射

        String str2 = " a b " ;
        System.out.println("操作前: !"  + str2 + "!");
        Class<? extends String> str2Class = str2.getClass();
        Field value = str2Class.getDeclaredField("value");
        value.setAccessible(true);
        char [] cs =(char[])value.get(str2);
        cs[2] = ':' ;
        System.out.println("操作后: !"  + str2 + "!");

    }
}
