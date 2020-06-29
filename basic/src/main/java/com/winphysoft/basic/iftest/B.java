package com.winphysoft.basic.iftest;

/**
 * Created by 小灰灰 on 2016/10/13.
 */
public class B implements Interface {
    @Override
    public void test(Interface i) {

    }

    @Override
    public Integer getOb() {
        return null;
    }

    private void test(A a){
        System.out.println("ba");
    }

    public void test(B b){
        System.out.println("bb");
    }
}
