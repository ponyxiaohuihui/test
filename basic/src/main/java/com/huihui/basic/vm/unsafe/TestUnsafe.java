package com.huihui.basic.vm.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by pony on 2018/6/13.
 */
public class TestUnsafe {
    private static Unsafe unsafe;
    static {
        try {
            Field f =Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws Exception{
        byte[] bytes = new byte[30];
        UA a = new UA("abbc", 10);
        for (int i = 0; i< 10; i++){
            bytes[i] = unsafe.getByte(a, 12 + i);
        }
        System.out.println(bytes);
        UA b = (UA) unsafe.allocateInstance(UA.class);
        for (int i = 0; i< 10; i++){
            unsafe.putByte(b, 12 + i, bytes[i]);
        }
        System.out.println(b);
    }

    private static class UA{
        String name;
        int age;

        public UA(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}
