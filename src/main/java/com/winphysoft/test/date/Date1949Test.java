package com.winphysoft.test.date;

import java.util.Calendar;
import java.util.Date;

public class Date1949Test {
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(0,0,0,0,0,0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(1949, 4, 1);
        System.out.println(new Date(c.getTimeInMillis()));
    }
}
