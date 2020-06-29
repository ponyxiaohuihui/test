package com.winphysoft.basic.vm.clazz.iftest;

/**
 * Created by 小灰灰 on 2016/10/14.
 */
public class IFUtils {
    public static void test(A a, B b){
        System.out.println("ab");
    }

    public static void test(Interface a, B b){
        System.out.println("ib");
    }

    public static void test(A a, Interface b){
        System.out.println("ai");
    }

    public static void test(Interface a, Interface b){
        System.out.println("ii");
    }

    public static void testa(Interface a, Interface b){
        test(a.getOb(), a,  b);
    }

    public static void test(String s, Interface a, Interface b){
        System.out.println("sss");

    }

    public static void test(Object s, Interface a, Interface b){
        System.out.println("ooo");
    }

    public static void main(String[] args) {
        A ia = new A();
        Interface ib = new B();
        testa(ia, ib);
    }
}
