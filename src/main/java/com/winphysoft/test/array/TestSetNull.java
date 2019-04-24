package com.winphysoft.test.array;

/**
 * Created by pony on 2018/10/10.
 */
public class TestSetNull {
    private Object ob = new Object();

    public void setNull(Object ob){
        ob = null;
    }

    public static void main(String[] args) {
        TestSetNull test = new TestSetNull();
        test.setNull(test.ob);
        System.out.println(test.ob);
    }
}
