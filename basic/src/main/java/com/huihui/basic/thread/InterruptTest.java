package com.huihui.basic.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InterruptTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future ft = service.submit(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!Thread.currentThread().isInterrupted()){
                    double d = 0;
                    for (i = 0 ;i < 100000000; i++){
                        d += Math.random();
                    }
                    System.out.println(d);
                }
                System.out.println("end" + i);
            }
        });
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ft.cancel(true);
                service.shutdown();
            }
        }.start();
    }
}
