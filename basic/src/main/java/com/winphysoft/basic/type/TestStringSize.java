package com.winphysoft.basic.type;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by pony on 2018/10/30.
 */
public class TestStringSize {
    public static void main(String[] args) {
        Object[] strings = new Object[1000000];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 1000; i++) {
            sb.append(UUID.randomUUID().toString());
        }
        Arrays.fill(strings, new String(sb));
        long t = System.currentTimeMillis();
        long size = 0l;
        for (Object ob : strings) {
            if (ob instanceof String) {
                size += ((String) ob).length() * 4;
            }
        }
        System.out.println(size);
        System.out.println(System.currentTimeMillis() - t);
    }

    @Override
    public String toString() {

        return "com.winphysoft.test.type.TestStringSize{}";
    }
}
