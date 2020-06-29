package com.winphysoft.basic.thread.fut;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureException {
    public static void main(String[] args) throws Exception{
        Future future = Executors.newFixedThreadPool(1).submit(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
        System.out.println(future.get());
    }
}
