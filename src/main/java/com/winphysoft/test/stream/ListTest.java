package com.winphysoft.test.stream;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add("a");
        list.add("1");
        list.add(1);
        List<String> sets = list.stream().map(Object::hashCode).collect(ArrayList::new, (set, item) -> set.add(item.toString()), (set, set2) -> set2.addAll(set));
        System.out.println(sets);
    }
}
