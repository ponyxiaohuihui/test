package com.winphysoft.test.thread;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/10/27
 */
public class TestWaitLock {
    public static void main(String[] args) {
        final TestWaitLock lock = new TestWaitLock();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                lock.close();
            }
        };
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                lock.print();
            }
        }.start();
    }

    synchronized void close()  {
        System.out.println("start wait");
        Object ob = new Object();
        synchronized (this){
            try {
                this.wait(10000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("finish wait");
    }
    synchronized void print(){
        System.out.println("print");
    }
}
