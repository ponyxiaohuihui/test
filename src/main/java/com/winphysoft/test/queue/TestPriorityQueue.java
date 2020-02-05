package com.winphysoft.test.queue;

import java.util.PriorityQueue;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/12/25
 */
public class TestPriorityQueue {
    static int min = -1;
    static PriorityQueue<Integer> queue = new PriorityQueue();
    static int size = 3;
    public static void main(String[] args) {
        add(1);
        add(2);
        add(3);
        add(4);
    }

    static void add( int value){
            if (queue.size() < size) {
                queue.add(value);
                if (min < value){
                    min = value;
                }
            } else {
                if (value - queue.peek()> 0) {
                    queue.poll();
                    queue.add(value);
                    min = value;
                }
            }
        }
}
