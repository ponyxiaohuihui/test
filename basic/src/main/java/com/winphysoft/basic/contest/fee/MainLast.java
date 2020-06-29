package com.winphysoft.basic.contest.fee;

import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainLast {
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        RandomAccessFile fs = new RandomAccessFile("C:\\codes\\in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = new PrintStream("C:\\codes\\l.out");
        int size = intBuffer.get();
        int n = intBuffer.get();
        Map<Integer, Integer> outofBounds = new HashMap<>();
        short[] results = new short[250000000];
        for (int i = 0 ; i< size ; i++){
            int id = intBuffer.get();
            if (results[id] == Short.MAX_VALUE){
                Integer value = outofBounds.get(id);
                value = value == null ? Short.MAX_VALUE : value;
                outofBounds.put(id, ++value);
            } else {
                results[id]++;
            }
        }
        System.out.println(System.currentTimeMillis() - t);
        int total = 0;
        for (int i = 0; i < results.length ; i++){
            if (results[i] != 0){
                total++;
            }
        }
        int index = total / 2;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < results.length ; i++){
            if (results[i] != 0){
                list.add(results[i] == Short.MAX_VALUE ? outofBounds.get(i) : (int)results[i]);
                if (--index == 0){
                    stream.println(i);
                }
            }
        }
        Collections.sort(list);
        int sum = 0;
        for (int i = list.size() - 1;i> list.size() -n; i--){
            sum += list.get(i);
        }
        stream.println(sum / n);
        fc.close();
        stream.close();
        System.out.println(System.currentTimeMillis() - t);
    }

}

