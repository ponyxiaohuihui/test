package com.winphysoft.test.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {
    public static void main(String[] args) {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(String.format("%s", "logs"));
            }
        }, 2, 2000, TimeUnit.MILLISECONDS);
    }
}
