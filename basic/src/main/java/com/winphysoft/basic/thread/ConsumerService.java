package com.winphysoft.basic.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 小灰灰 on 2017/8/18.
 */
public class ConsumerService {
    private Map<Object, Container> map = new ConcurrentHashMap<Object, Container>();
    private Map<Object, AtomicInteger> countMap = new ConcurrentHashMap<Object, AtomicInteger>();

    private ExecutorService executorService;

    //每个小块好了都触发下addtask
    private void addTask(Object key){
        AtomicInteger count = getCount(key);
        //如果没有任务，就加一个，有任务就只加一个计数
        if (count.incrementAndGet() == 1){
            executorService.submit(new CTask(key));
        }
    }

    private AtomicInteger getCount(Object key) {
        if (!countMap.containsKey(key)){
            synchronized (countMap){
                if (!countMap.containsKey(key)){
                    countMap.put(key, new AtomicInteger(0));
                }
            }
        }
        return countMap.get(key);
    }

    private class CTask implements Runnable{
        Object key;

        public CTask( Object key) {
            this.key = key;
        }

        @Override
        public void run() {
            //一开始的时候肯定会run一下
            if(map.get(key).consume()){
                countMap.remove(key);
                map.remove(key);
            }
            //执行完了再看看有没有加进来当前任务的，有的话就继续
            while (getCount(key).decrementAndGet() != 0){
                if (map.get(key).consume()){
                    countMap.remove(key);
                    map.remove(key);
                }
            }
        }
    }
}
