package com.winphysoft.basic.vm.unsafe;

import sun.misc.Unsafe;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * Created by pony on 2018/6/13.
 */
public class TestUnsafeArray {
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
        while (true){
            setLoop();
        }
    }

    private static void unsafeLoop() {
        byte[] bytes = new byte[100000000];
        long t = System.currentTimeMillis();
        for (int i = 0; i< 100000000; i++){
            unsafe.putByte(bytes, 16+i, (byte) i);
        }
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void setLoop() {
        byte[] bytes = new byte[100000000];
        long t = System.currentTimeMillis();
        for (int i = 0; i< 100000000; i++){
            bytes[i] =  (byte) i;
        }
        System.out.println(System.currentTimeMillis() - t);
    }

}
