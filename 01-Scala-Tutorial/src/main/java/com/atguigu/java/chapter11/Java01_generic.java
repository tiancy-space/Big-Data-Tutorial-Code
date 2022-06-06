package com.atguigu.java.chapter11;


import java.util.ArrayList;
import java.util.List;

/**
 * 泛型
 */
public class Java01_generic {
    public static void main(String[] args) {
        //1. 泛型基本使用
        ArrayList arrayList = new ArrayList();
        arrayList.add(123);
        arrayList.add("abc");
        arrayList.add(false);

        Object o = arrayList.get(0);

        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.add(123);
        //arrayList1.add("abc");

        Integer integer = arrayList1.get(0);

        //2. 泛型问题

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(123);
        arrayList2.add("abc");
        arrayList2.add(false);

        ArrayList<Integer> arrayList3 = arrayList2 ;

        Integer integer1 = arrayList3.get(0);


        //3.泛型上下限问题:
        // ? super XXX
        // ? extends XXX
        ArrayList<String> arrayList4 = new ArrayList<>();

        test1(arrayList4);
        //test2(arrayList4);


        //4. 泛型不可变
        AAA<Parent> parentAAA = new AAA<Parent>();
        AAA<User> userAAA = new AAA<User>();
        AAA<SubUser> subUserAAA = new AAA<SubUser>();
        AAA<Emp> empAAA = new AAA<Emp>();

        //5. 泛型上限 下限

        BBB bbb = new BBB();

        List<Parent> parentList = new ArrayList<Parent>();
        List<User> userList = new ArrayList<User>();
        List<SubUser> subUserList = new ArrayList<SubUser>();
        List<Emp> empList = new ArrayList<Emp>();

        //上限
        //bbb.extendsTest(parentList);
        bbb.extendsTest(userList);
        bbb.extendsTest(subUserList);
        //bbb.extendsTest(empList);

        //下限
        bbb.superTest(parentList);
        bbb.superTest(userList);
       //bbb.superTest(subUserList);
        //bbb.superTest(empList);


    }

    public static void  test1(List<String> list){

    }

    public static void test2(ArrayList<Object> list){}


}
class BBB{
    //上限  ? <= User
    public void extendsTest(List<? extends User> list) {

    }
    //下限   ? >=User
    public void superTest(List<? super User> list){

    }
}



class AAA<T>{

}


class Parent{

}
class User extends Parent{

}

class SubUser extends User{

}
class Emp {

}
