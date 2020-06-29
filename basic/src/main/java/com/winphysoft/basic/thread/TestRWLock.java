package com.winphysoft.basic.thread;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/4/1
 */
public class TestRWLock {
    static StampedLock lock = new StampedLock();
    public static void main(String[] args) {
        new Thread(()->{
            lock.readLock();
            System.out.println("i am read");
        }).start();
        new Thread(()->{
            System.out.println("i am write wait");
            lock.unlockRead(10);
            lock.writeLock();
            System.out.println("i am write");
        }).start();
    }
}
