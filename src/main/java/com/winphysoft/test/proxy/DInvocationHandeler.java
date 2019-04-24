package com.winphysoft.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by pony on 2018/8/15.
 */
public class DInvocationHandeler implements InvocationHandler {
    private DInterface di;
    public DInvocationHandeler(DInterface di) {
        this.di = di;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args[0] = "B";
        System.out.println("ivoke");
        return method.invoke(di, args);
    }
}
