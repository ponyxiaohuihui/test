package com.winphysoft.basic.file.buffer;

import sun.misc.Cleaner;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/10/28
 */
public class CleanerTest {
    public static void main(String[] args) {
        LinkedList<Buffer> ref = new LinkedList<>();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try{
                        synchronized (ref) {
                            try {
                                ref.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("pull one");
                        final Buffer buf = ref.poll();
                        new Thread() {
                            @Override
                            public void run() {
                                int idx = 0;
                                int sum = 0;
                                while (idx++ < 9999) {
                                    sum += buf.get(idx);
                                }
                                System.out.println("sum is " + sum);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }


                        }.start();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        while (true) {
            try {
                int idx = 0;
                Buffer buf = new Buffer(10000);
                while (idx++ < 9999) {
                    buf.put(idx, (byte) idx);
                }
                System.out.println(buf.get(0));
                ref.add(buf);
            } catch (OutOfMemoryError e) {
                synchronized (ref) {
                    ref.notify();
                    System.out.println("notify one");
                }
            }
        }

    }
}
class Buffer{
    byte[] bytes;
    Cleaner cleaner;
    public Buffer(int i) {
        bytes = new byte[i];
        cleaner = Cleaner.create(this, new Deallocator(bytes));
    }

    public void put(int idx , byte v) {
        bytes[idx] = v;
    }

    public byte get(int i) {
        return bytes[i];
    }

    private class Deallocator implements Runnable{
        byte[] bytes;

        public Deallocator(byte[] bytes) {

        }

        @Override
        public void run() {
            Arrays.fill(bytes, (byte)0);
            System.out.println("deallo");
        }
    }
}