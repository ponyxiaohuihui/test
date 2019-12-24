package com.winphysoft.test.type.date;

import java.sql.Date;

public class SqlDateTest {
    public static void main(String[] args) {
        Date sqlDate = new Date(System.currentTimeMillis());
        Date ofDate = Date.valueOf(sqlDate.toString());
        System.out.println(ofDate);
    }
}
