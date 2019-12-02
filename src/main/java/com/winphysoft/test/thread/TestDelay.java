package com.winphysoft.test.thread;

import com.winphysoft.test.exception.RunTimeTest;
import org.omg.SendingContext.RunTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/10/25
 */
public class TestDelay {
    private static volatile boolean flags[] = new boolean[2000];
    public static void main(String[] args) {
//        List<Integer> list = new ArrayList<>();
//        //一个线程每100毫秒模拟下fullgc
//        new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(100);
//                        for (int i = 0; i < 1000000; i++) {
//                            list.add(i);
//                        }
//                    } catch (Throwable e) {
//                    }
//                    Arrays.fill(flags, true);
//                }
//            }
//        }.start();
        //4个线程同时读

        for (int i = 0; i < 2000; i++) {
            int finalI = i;
            Thread t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (flags[finalI]){
                            System.out.println("i am " + finalI + " time is " + System.nanoTime() / 10000000);
                            flags[finalI] = false;
                        }
                    }
                }
            };

            t.start();
        }
        Arrays.fill(flags, true);
        Runtime.getRuntime().availableProcessors();
    }
}
