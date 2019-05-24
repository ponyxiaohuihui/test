package com.winphysoft.test.date;

import java.util.Date;

/**
 * Created by pony on 2017/11/14.
 */
public class DateTest {
    public static void main(String[] args) {
        long t = new java.util.Date().getTime();
        Date d = new Date(t);
        System.out.println(t);
        System.out.println(d.getTime());
        System.out.println((long)1<<32);
    }
}
