package com.winphysoft.test.vm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 小灰灰 on 2017/5/19.
 */
public class ReleaseMemory {
    public static void main(String[] args) {
        final AtomicInteger n = new AtomicInteger();
        Thread[] s = new Thread[100];
        for (int i = 0; i < 100; i++){
            s[i] = new Thread(){
                @Override
                public void run() {
                    while(true)
                    System.out.println(n.incrementAndGet());
                }
            };
        }
        for (int i = 0; i < 100; i++){
            s[i].start();
        }
    }
}
