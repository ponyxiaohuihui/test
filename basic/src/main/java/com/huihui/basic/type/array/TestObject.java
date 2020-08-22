package com.huihui.basic.type.array;

public class TestObject {
    public static void main(String[] args) {
        int[] ints = new int[1];
        String[] s = new String[1];
        Object[] ob = new Object[1];
        s.getClass().isAssignableFrom(ob.getClass());
        System.out.println(ints.length);
        System.out.println(ints.getClass());
        ints.hashCode();
        System.out.println(ints instanceof Object);
    }
}
