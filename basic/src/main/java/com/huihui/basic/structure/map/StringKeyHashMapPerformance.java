package com.huihui.basic.structure.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StringKeyHashMapPerformance {
    public static void main(String[] args) {
        List<String> uuidList = new ArrayList<>(10000000);
        for (int i = 0; i < 10000000; i++){
            uuidList.add(i, UUID.randomUUID().toString());
        }

        Map map = new HashMap();
        Object ob = new Object();
        long t = System.currentTimeMillis();
        for (String id : uuidList){
            map.put(id, ob);
        }
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        int i = 0;
        for (String id : uuidList){
            if (map.containsKey(id)){
                i++;
            }
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(i);
    }
}
