package com.winphysoft.basic.vm.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeCrash {

    private static Unsafe unsafe;
    {
        try {
            unsafe = getUnsafe();
        } catch (Throwable e) {
        }
    }

    private int size;

    private long p;

    private int offset;

    public static UnsafeCrash newInstance(int size) {
        return new UnsafeCrash(size);
    }

    /**
     * 2 * 1024 * 1024
     *
     * @param size
     * @throws Throwable
     */
    public UnsafeCrash(int size) {
        int index = 0;
        while (true){
            long p = unsafe.allocateMemory(4032000l);
            unsafe.freeMemory(p);
            System.out.println(p);
            if (p < 100){
                System.out.println(p);
            }
        }
//        p = unsafe.allocateMemory(size);
//        this.size = size;
//        offset = 0;
    }

    public static Unsafe getUnsafe() throws Throwable {
        Class<?> unsafeClass = Unsafe.class;
        for (Field f : unsafeClass.getDeclaredFields()) {
            if ("theUnsafe".equals(f.getName())) {
                f.setAccessible(true);
                return (Unsafe) f.get(null);
            }
        }
        throw new IllegalAccessException("no declared field: theUnsafe");
    }

    public synchronized byte[] get() {
        int nbytes = offset;
        byte[] bytes = new byte[nbytes];
        for (int i = 0; i < nbytes; i++) {
            bytes[i] = unsafe.getByte(p + i);
        }
        return bytes;
    }

    public synchronized void put(byte[] bytes) {
        int retainBytes = size - offset;
        int nbytes = retainBytes > bytes.length ? bytes.length : retainBytes;
        for (int i = 0; i < nbytes; i++) {
            unsafe.putByte(p + offset++, bytes[i]);
        }
    }

    public synchronized boolean available() {
        return p != -1;
    }

    public synchronized void deallocate() {
        unsafe.freeMemory(p);
        p = -1;
        offset = -1;
    }

    public static void main(String[] args) {
        UnsafeCrash block = UnsafeCrash.newInstance(2 * 1024 * 1024);

        System.out.println(block.available());

        block.put("greeting".getBytes());
        byte[] bytes = block.get();
        String s = new String(bytes);
        System.out.println(s);

        block.deallocate();
        System.out.println(block.available());

        block.put("helloworld".getBytes());
    }
}
