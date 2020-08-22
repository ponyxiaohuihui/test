package com.huihui.basic.structure.queue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author pony
 * Created by pony on 2020/7/20
 */
public class ArrayDequeueTest {
    public static void main(String[] args) {
        Deque<String> queue = new ArrayDeque<String>();
        queue.push("A");
        queue.push("B");
        System.out.println(queue.peek());
    }
}
