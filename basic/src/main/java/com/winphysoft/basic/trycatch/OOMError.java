package com.winphysoft.basic.trycatch;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/12/24
 */
public class OOMError {
    public static void main(String[] args) throws Exception{
        ExecutorService service = Executors.newFixedThreadPool(10);
        int i = 0;
        while (i++ < 1000000){
            service.submit(()->{
                if (System.currentTimeMillis() > 0){
                    throw new OutOfMemoryError("rrrr");
                }
            });
        }
        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
