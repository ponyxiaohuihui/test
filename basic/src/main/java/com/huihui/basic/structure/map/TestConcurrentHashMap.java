package com.huihui.basic.structure.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pony on 2017/12/13.
 */
public class TestConcurrentHashMap {
    public static void main(String[] args) {
        testGetNull();
    }

    public static void testGetNull() {
        Map m = new ConcurrentHashMap();
        m.put(1, 1);
        System.out.println(m.get(2));
    }
}
