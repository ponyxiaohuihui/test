package com.winphysoft.basic.ref;


/**
 * Created by pony on 2018/5/7.
 */
public class EEETest {
    public static void main(String[] args) throws NoSuchFieldException {
        Class c = EEE.class;
        c.getField("A");
    }
}