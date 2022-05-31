package chapter01

/**
  * 1.class修饰的类编译后只有一个字节码文件 => 类名.class
  *
  * 2.反编译class文件查看:
  *    public com.atguigu.scala.chapter01.Scala02_class()
  *
  *
  * 3.在类中添加main方法，反编译查看:
  *     public void main(String[] args) {}    普通的main方法
  *
  * 4.在main方法中添加语句,反编译查看：
  *     语句就出现到了普通的main方法中
  *     public void main(String[] args){
  *         Predef..MODULE$.println("Hello class");
  *     }
  *
  * 5. class修饰的类， 类中的方法需要通过new对象后，使用对象来调用.
  *
  */
class Scala02_class {
  def main(args: Array[String]): Unit = {
      println("Hello class")
  }

  def test():Unit = {
     println("test")
  }
}
