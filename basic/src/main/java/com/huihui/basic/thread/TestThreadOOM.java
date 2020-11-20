package com.huihui.basic.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author pony
 * Created by pony on 2020/11/3
 */
public class TestThreadOOM {
    public static void main(String[] args) throws Exception{
        Thread thread = new Thread(() -> {
            List<int[]> list = new LinkedList<>();
            boolean print = false;
            boolean sleep = true;
            int count = 0;
            while (true) {
                try {
                    if (print) {
                        System.out.println("add");
                    }
                    count++;
                    if (sleep) {
                        Thread.sleep(1000);
                        if (count > 10){
                            sleep = false;
                        }
                    }
                    list.add(new int[1024]);
                } catch (Throwable t) {
                    t.printStackTrace();
                    print = true;
                }
            }
        });
        thread.start();
        thread.join();
    }
}
