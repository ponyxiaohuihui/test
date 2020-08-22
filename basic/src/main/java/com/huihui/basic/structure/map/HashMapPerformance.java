package com.huihui.basic.structure.map;

import java.util.HashMap;

public class HashMapPerformance {
    public static void main(String[] args) {
        String key1 = "1";
        String key2 = "2";
        HashMap<String, Plus> map = new HashMap<String, Plus>();
        map.put(key1, new Plus());
        map.put(key2, new Plus());
        int[] ints = new int[]{1,2,3,4};
        long t = System.currentTimeMillis();
        for (int i = 0; i < 100000000;i++){
            map.put(key1, map.get(key1).plus(ints[i % 2]));
            map.put(key2, map.get(key2).plus(ints[i % 2]));
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(map);

    }

    static class Plus{
        int i = 0;
        Plus plus(int num){
            i += num;
            return this;
        }

        @Override
        public String toString() {
            return "Plus{" +
                    "i=" + i +
                    '}';
        }
    }
}
