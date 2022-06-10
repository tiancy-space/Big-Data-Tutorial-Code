package com.atguigu.java.chapter12_other;

/**
 * @Description:
 * @Author: tiancy
 * @Create: 2022/6/10
 */
public class Java01_泛型类用意 {

}

// 定义一个手机的部件类: 电池类,里面有一个放电的方法
class 电池 {
    public void 放电() {
        System.out.println("输出电能");
    }
}

// 定义一个手机的部件类: 屏幕类,里面有一个亮屏的方法
class 屏幕 {
    public void 亮屏() {
        System.out.println("显示内容");
    }
}

// 定义一个手机的部件类: CPU类,里面有一个运算的方法
class CPU {
    public void 运算() {
        System.out.println("浮点计算");
    }
}

// 开始组装一个具体的手机,当前手机类中的属性: 电池、屏幕、CPU. 都是一个具体的类. 如果想创建一个具体的手机,就需要各种具体的硬件搭配.
// 如果我想定义当前手机类为一个泛型类,则当前的泛型,描述的就是当前类中成员变量(属性)的类型. ==> 定义的泛型类型就需要类内的属性都是这个类型.
class 手机 {
    private 电池 dc;
    private 屏幕 pm;
    private CPU cpu;

    public 手机(电池 dc, 屏幕 pm, CPU cpu) {
        this.dc = dc;
        this.pm = pm;
        this.cpu = cpu;
    }

    public void 开机() {
        dc.放电();
        cpu.运算();
        pm.亮屏();
        System.out.println("开机成功");
    }

    public static void main(String[] args) {
        电池 dc = new 电池();
        屏幕 pm = new 屏幕();
        CPU cpu = new CPU();
        手机 sj = new 手机(dc, pm, cpu);
        sj.开机();
    }
}
