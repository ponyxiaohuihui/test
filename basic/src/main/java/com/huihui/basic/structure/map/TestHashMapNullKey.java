package com.huihui.basic.structure.map;

import java.util.HashMap;
import java.util.Map;

public class TestHashMapNullKey {
    public static void main(String[] args) {
        Map map = new HashMap<>();
        map.put(null, "1");
        System.out.println(map.get(null));
    }
}
