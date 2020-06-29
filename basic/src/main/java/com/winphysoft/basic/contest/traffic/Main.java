package com.winphysoft.basic.contest.traffic;


import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        RandomAccessFile fs = new RandomAccessFile("C:\\codes\\in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = new PrintStream("C:\\codes\\2.out");
        int size = intBuffer.get();
        int n = intBuffer.get();
      //  if (size < 1000001){
       //     bulletGroup( fc, intBuffer, stream, size, n);
   //     } else {
           byteArrayGroup( fc, intBuffer, stream, size, n);
 //       }
        System.out.println(System.currentTimeMillis() - t);

    }

    public static void hashGroup(FileChannel fc, IntBuffer intBuffer, PrintStream stream, int size, int n) throws IOException {
        long t = System.currentTimeMillis();
        Map<Integer, Integer> map= new HashMap<>();
        for (int i = 0 ; i< size ; i++){
            int id = intBuffer.get();
            Integer value = map.get(id);
            value = value == null ? 0 : value++;
            map.put(id, ++value);
        }
        System.out.println(System.currentTimeMillis() - t);
        List<Integer> keys = new ArrayList<>();
        NTree nTree = new NTree(n);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()){
            keys.add(entry.getKey());
            nTree.add(entry.getValue());
        }
        Collections.sort(keys);
        stream.println(keys.get(keys.size() / 2));
        System.out.println(System.currentTimeMillis() - t);

        int sum = 0;
        for (Map.Entry<Integer, NTree.Counter> entry : nTree.map.entrySet()){
            sum += entry.getKey() * entry.getValue().count;
        }
        stream.println(sum / n);
        fc.close();
        stream.close();
        System.out.println(System.currentTimeMillis() - t);
    }


    public static void bulletGroup(FileChannel fc, IntBuffer intBuffer, PrintStream stream, int size, int n) throws IOException {
        long t = System.currentTimeMillis();
        int[][] fromBullets = new int[8][size];
        int[][] toBullets = new int[8][size];
        int[][] tempBullet = null;
        int[] fromsize = new int[8];
        int[] tosize = new int[8];
        for (int i = 0 ; i< size ; i++){
            int id = intBuffer.get();
            int x = id & 0x7;
            fromBullets[x][fromsize[x]++] = id;
        }
        for (int i = 0 ; i< 9 ; i++){
            int shift = 3 * (i + 1);
            int mod = 0x7 << shift;
            for (int j = 0; j < 8; j++){
                for (int k = 0; k < fromsize[j]; k++){
                    int id = fromBullets[j][k];
                    int x = (id & mod) >> shift;
                    toBullets[x][tosize[x]++] = id;
                }
            }
            tempBullet = toBullets;
            toBullets = fromBullets;
            fromBullets = tempBullet;
            fromsize = tosize;
            tosize = new int[8];
        }
        int index = 0;
        toBullets[0][0] = 0;
        toBullets[1][0] = 0;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < fromsize[i]; j++){
                int id = fromBullets[i][j];
                if (toBullets[0][index] == id){
                    toBullets[1][index]++;
                } else {
                    index++;
                    toBullets[0][index] = id;
                    toBullets[1][index] = 1;
                }

            }
        }
        System.out.println(System.currentTimeMillis() - t);
        stream.println(toBullets[0][index / 2]);
        NTree nTree = new NTree(n);
        for (int i = 0; i < index ; i++){
            nTree.add(toBullets[1][i]);
        }
        int sum = 0;
        for (Map.Entry<Integer, NTree.Counter> entry : nTree.map.entrySet()){
            sum += entry.getKey() * entry.getValue().count;
        }
        stream.println(sum / n);
        fc.close();
        stream.close();
        System.out.println(System.currentTimeMillis() - t);
    }


    public static void byteArrayGroup(FileChannel fc, IntBuffer intBuffer, PrintStream stream, int size, int n) throws IOException {
        long t = System.currentTimeMillis();
        Map<Integer, Integer> outofBounds = new HashMap<>();
        int total = 0;
        byte[] results = new byte[250000000];
        for (int i = 0 ; i< size ; i++){
            int id = intBuffer.get();
            if (results[id] == 0){
                total++;
            }
            if (results[id] == Byte.MAX_VALUE){
                Integer value = outofBounds.get(id);
                value = value == null ? Byte.MAX_VALUE : value;
                outofBounds.put(id, ++value);
            } else {
                results[id]++;
            }
        }
        System.out.println(System.currentTimeMillis() - t);
        int index = total / 2;
        NTree nTree = new NTree(n);
        for (int i = 0; i < results.length ; i++){
            if (results[i] != 0){
                nTree.add(results[i] == Byte.MAX_VALUE ? outofBounds.get(i) : (int)results[i]);
                if (--index == 0){
                    stream.println(i);
                }
            }
        }
        System.out.println(System.currentTimeMillis() - t);

        int sum = 0;
        for (Map.Entry<Integer, NTree.Counter> entry : nTree.map.entrySet()){
            sum += entry.getKey() * entry.getValue().count;
        }
        stream.println(sum / n);
        fc.close();
        stream.close();
        System.out.println(System.currentTimeMillis() - t);
    }

    private static Comparator<Integer> COMP = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    };

    static class NTree {

        private TreeMap<Integer, Counter> map = new TreeMap<>(COMP);

        private int n;

        private int addCount = 0;

        public NTree(int n) {
            this.n = n;
        }

        public void add(Integer value) {
            if (addCount < n) {
                putValue(value);
            } else {
                Map.Entry<Integer, Counter> entry = map.lastEntry();
                if (COMP.compare(value , entry.getKey()) < 0) {
                    putValue(value);
                    Counter c = entry.getValue();
                    c.reduce();
                    if (c.isEmpty()) {
                        map.remove(entry.getKey());
                    }
                }
            }
            addCount++;
        }

        private void putValue(Integer value) {
            Counter c = map.get(value);
            if (c == null) {
                c = new Counter();
                map.put(value, c);
            }
            c.plus();
        }


        private class Counter {
            int count = 0;

            void plus() {
                count++;
            }

            void reduce() {
                count--;
            }

            boolean isEmpty() {
                return count == 0;
            }
        }
    }

}
