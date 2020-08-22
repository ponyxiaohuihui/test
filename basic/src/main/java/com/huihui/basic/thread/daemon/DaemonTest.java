package com.huihui.basic.thread.daemon;

public class DaemonTest {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        Thread thread = new Thread("p"){
            public void run(){

                try {
                    Thread.sleep(2000);
                    System.out.println("thread");
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
        System.out.println("finish");
        Thread child = new Thread("child"){
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    System.out.println("child1");
                    Thread.sleep(1000);
                    System.out.println("child2");
                    Thread.sleep(100000);
                    System.out.println("child3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        child.setDaemon(true);
        child.start();
    }
}
