package com.winphysoft.basic.vm.clazz.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by pony on 2018/8/15.
 */
public class DProxy implements DInterface{
    public static void main(String[] args) {
        DProxy dp = new DProxy();
        DInterface di = (DInterface) Proxy.newProxyInstance(dp.getClass().getClassLoader(), new Class[]{DInterface.class}, new DInvocationHandeler(dp));
        di.do1("1");
        di.do2("2");
    }

    @Override
    public void do1(String s) {
        System.out.println(s + "d1");
    }

    @Override
    public void do2(String s) {
        System.out.println(s + "d2");

    }
}
