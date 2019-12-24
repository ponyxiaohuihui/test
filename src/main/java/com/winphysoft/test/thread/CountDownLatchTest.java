package com.winphysoft.test.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by pony on 2018/5/30.
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(0);
        latch.await();
        System.out.println("hahaha");
    }
}
