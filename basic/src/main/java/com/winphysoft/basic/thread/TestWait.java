package com.winphysoft.basic.thread;

/**
 * Created by 小灰灰 on 2017/2/15.
 */
public class TestWait {
    public static void main(String[] args) throws InterruptedException {
        final Object ob = new Object();
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    synchronized (ob){
                        ob.notify();
                        Thread.sleep(20000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        synchronized (ob){
            ob.wait();
        }
        System.out.println("aaa");

    }

}
