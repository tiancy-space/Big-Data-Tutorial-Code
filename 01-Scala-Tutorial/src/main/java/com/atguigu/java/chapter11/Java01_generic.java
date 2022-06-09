package com.atguigu.java.chapter11;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    TODO java中的泛型定义、使用以及注意事项
 */
public class Java01_generic {
    public static void main(String[] args) {
        /*
            1. 泛型基本使用
            先定义一个集合,用于存放不同书记类型的数据.
         */
        ArrayList arrayList = new ArrayList();
        arrayList.add(123);
        arrayList.add("abc");
        arrayList.add(false);
        System.out.println(arrayList); // [123, abc, false]
        /*
            存放没有问题,但是在取数据的时候,会出现集合中存放的数据类型不同,到最最终取的时候,只能使用父类型 object来接收.
            如果后面的处理数据的步骤还很长,并且针对不同的数据类型还有不同的处理逻辑,则使用起来不是很流畅,导致处理过程冗余 ==> 引入泛型的目的: 强调数据类型.
         */
        Object o = arrayList.get(0);

        // 定义一个集合,并声明它的泛型,也就是当前定义的 ArrayList对象,只能存放 Integer类型的数据. 泛型声明的位置: 指定了当前类后面的位置,也就是所谓的泛型类.
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.add(123);
        //arrayList1.add("abc"); // 编译前类型检查不通过

        Integer integer = arrayList1.get(0);

        //2. 泛型问题 : 先定义一个没有泛型的ArrayList(),往里面放入不同数据类型的数据. 并将当前的大杂烩集合引用地址,给到一个带泛型的新的集合上.

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(123);
        arrayList2.add("abc");
        arrayList2.add(false);

        ArrayList<Integer> arrayList3 = arrayList2;

        Integer integer0 = arrayList3.get(0);
        /*
            下面的获取当前集合中的指定元素时,会报类型转化异常.
            Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
            产生原因: 使用泛型,仅仅在我们写代码时,明确指定了要处理的数据类型,而在程序编译执行时,会做泛型的擦除,只要语法编译通过,就不会报错.
            但是程序真正执行起来后,我们执行的结果,都用Integer来接收,就会报这个问题.
         */
        // Integer integer1 = arrayList3.get(1); 程序运行报错
        // Integer integer2 = arrayList3.get(2); 程序运行报错


        //3.泛型上下限问题:
        // ? super XXX
        // ? extends XXX
        ArrayList<String> arrayList4 = new ArrayList<>();
        /*
            泛型的上下限:
            public static void test1(List<String> list){}
            public static void test2(List<Object> list){}
         */
        test1(arrayList4);
        /*
            调用当前方法时,程序编译不通过: 原因 当前方法需要传入一个List类型的对象,并且当前list类型的对象里面需要的是Object类型的数据.
            这时调用当前的方法: test2(里面的参数类型为<String>),却编译不通过. 这里需要考虑到呀: String是Object类的子类,这样是不是用起来不太灵活呢.
            这里就需要引入一个新的概念: 需要考虑到 泛型的上下限问题.
            所谓的泛型上限和下限:
                就是在使用泛型声明 类或者方法操作的数据类型时,我们在调用时传入的数据类型可以是当前声明的泛型数据类型本身,也可以是传入的数据类型的父类.
                或者当前数据类型的子类作为当前泛型类型. 从而能够传入符合我们预期的泛型类型进行灵活使用.
            <? super xxx>
            <? extends xxx>
         */
        //test2(arrayList4);


        //4. 泛型不可变,还会存在一个概念: 泛型的协变、逆变、不可变可以参考:  https://juejin.cn/post/6952434934589947912
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
        /*
             上限  ? <= User ,也就是说定义的当前方法中参数类型为一个List<>,List内存放的数据类型只能是User类型,或者User类型的子类. 否则编译不通过.
             class User extends Parent {}
             class SubUser extends User {}
             class Emp {} ,当前的Emp类和User类没有关系.
             public void extendsTest(List<? extends User> list) {}
         */
        bbb.extendsTest(userList);
        // class SubUser extends User {} ,SubUser是User类型的子类.
        bbb.extendsTest(subUserList);
        //bbb.extendsTest(empList); // 与指定的类型无关,因此编译报错.

        //下限
        /*
            class Parent {} /当前类
            class User extends Parent {} // User类是Parent的子类.
            public void superTest(List<? super User> list) {} : 声明一个方法superTest, 当前方法参数类型为:List<? super User> : 声明的泛型下限
                也就是指明当前方法中的参数为List<>,List中可以存放当的数据类型只能是 User 或者 User的父类.
         */
        bbb.superTest(parentList);
        bbb.superTest(userList);
        //bbb.superTest(subUserList);
        //bbb.superTest(empList);


    }

    public static void test1(List<String> list) {

    }

    public static void test2(ArrayList<Object> list) {
    }


}

class BBB {
    //上限  ? <= User
    public void extendsTest(List<? extends User> list) {

    }

    //下限   ? >=User
    public void superTest(List<? super User> list) {
    }
}


class AAA<T> {

}


class Parent {

}

class User extends Parent { }

class SubUser extends User { }

class Emp {}
