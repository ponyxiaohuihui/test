package com.winphysoft.test.map;

import java.util.IdentityHashMap;

/**
 * Created by 小灰灰 on 2017/1/18.
 */
public class TestIdentityHashMap {
    public static void main(String[] args) {
        IdentityHashMap<Object, String> map = new IdentityHashMap();
        String key1 = new String("key");
        String key2 = new String("key");
        Object key3 = new Object();
        map.put(key1, "key1");
        map.put(key2, "key1");
        map.put(key3, "key1");
        System.out.println(map.size());
    }
}
