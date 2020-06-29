package com.winphysoft.basic.thread.atom;

import com.sun.tools.doclint.Checker;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/18
 */
public class AtomicBooleanPerformance {
    private static volatile boolean flag = true;
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        Thread[] threads = new Thread[6];
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        for (int i = 0; i < 6; i++) {
            threads[i] = new Thread(){
                int count = 1000000000;
                public void run(){
                    while (count-- > 0 && flag){
                        atomicBoolean.compareAndSet(true, false);
                    };
                    System.out.println(System.currentTimeMillis() - t);
                }
            };
        }
        for (int i = 0; i < 6; i++) {
            threads[i].start();
        }
    }

}
