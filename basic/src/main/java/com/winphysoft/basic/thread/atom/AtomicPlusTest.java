package com.winphysoft.basic.thread.atom;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicPlusTest {
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Thread[] threads = new Thread[6];
        for (int i = 0; i < 6; i++) {
            threads[i] = new Thread(){
                int idx = 0;
                int[] is = new int[100000000];
                public void run(){
                    while ((is[idx++] = atomicInteger.incrementAndGet()) < 100000000){

                    };
                    System.out.println(System.currentTimeMillis() - t);
                }
            };
        }
        for (int i = 0; i < 6; i++) {
            threads[i].start();
        }
        Thread.sleep(2000);
        System.out.println("end");
    }
}
