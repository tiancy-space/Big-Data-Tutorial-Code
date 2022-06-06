package com.atguigu.scala.chapter01

/**
  * 1. object修饰的类(伴生对象), 编译后会生成两个字节码文件:
  *    类名.class
  *    类名$.class
  *
  * 2. 反编译后查看:
  *     类名.class
  *
  *     类名$.class
  *        1) 当前类型的static变量 => MODULE$
  *           public static com.atguigu.scala.chapter01.Scala03_object$ MODULE$;
  *
  *        2) 静态代码块
  *           static{
  *               new ();  创建当前类型的对象
  *           }
  *
  *        3) 私有构造器
  *          private Scala03_object$(){
  *               MODULE$ = this;   // 将当前对象赋值给当前类型的变量 MODULE$
  *          }
  * 3. 添加main方法后,反编译查看:
  *     类名.class
  *         public static void main(String[] paramArrayOfString){     // static的main方法
  *                Scala03_object$.MODULE$.main(paramArrayOfString);
  *         }
  *
  *     类名$.class
  *         public void main(String[] args){}   // 普通的main方法
  *
  * 4. 在main方法中添加语句,反编译后查看:
  *     类名.class
  *         public static void main(String[] paramArrayOfString){     // static的main方法
  *                Scala03_object$.MODULE$.main(paramArrayOfString);
  *         }
  *
  *     类名$.class
  *         public void main(String[] args){
  *              Predef..MODULE$.println("Hello object");
  *         }
  *
  *  5. object修饰的类执行过程:
  *     类名.class => static main => 类名$.class => MODULE$  => 类名$.class => 普通的main
  */
object Scala03_object {
  def main(args: Array[String]): Unit = {
    println("Hello object")

    test()
  }

  def test():Unit = {
    println("test")
  }
}
