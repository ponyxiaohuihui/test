package com.winphysoft.basic.contest.traffic;


import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MainBullet {
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        RandomAccessFile fs = new RandomAccessFile("C://codes//in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = new PrintStream("C://codes//3.out");
        int size = intBuffer.get();
        int n = intBuffer.get();
        int currentSize = size;
        int[][] fromBullets = new int[8][currentSize/4];
        int[][] toBullets = new int[8][currentSize/4];
        int[][] tempBullet = null;
        int[] fromsize = new int[8];
        int[] tosize = new int[8];
        for (int i = 0 ; i< size ; i++){
            int id = intBuffer.get();
            int x = id & 0x7;
            fromBullets[x][fromsize[x]++] = id;
            if (fromsize[x] >= fromBullets[x].length){
                ensureCapa(fromBullets, x);
            }
        }
        for (int i = 0 ; i< 9 ; i++){
            int shift = 3 * (i + 1);
            int mod = 0x7 << shift;
            for (int j = 0; j < 8; j++){
                for (int k = 0; k < fromsize[j]; k++){
                    int id = fromBullets[j][k];
                    int x = (id & mod) >> shift;
                    toBullets[x][tosize[x]++] = id;
                    if (tosize[x] >= toBullets[x].length){
                        ensureCapa(toBullets, x);
                    }
                }
            }
            tempBullet = toBullets;
            toBullets = fromBullets;
            fromBullets = tempBullet;
            fromsize = tosize;
            tosize = new int[8];
        }
        int index = 0;
        toBullets[0][0] = fromBullets[0][0];
        toBullets[1][0] = 0;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < fromsize[i]; j++){
                int id = fromBullets[i][j];
                if (toBullets[0][index] == id){
                    toBullets[1][index]++;
                } else {
                    index++;
                    if (index >= toBullets[0].length){
                        ensureCapa(toBullets, 0);
                    }
                    if (index >= toBullets[1].length){
                        ensureCapa(toBullets, 1);
                    }
                    toBullets[0][index] = id;
                    toBullets[1][index] = 1;
                }
            }
        }
        index ++;
        stream.println(toBullets[0][index / 2 - 1]);
        System.out.println(System.currentTimeMillis() - t);
        NTree nTree = new NTree(n);
        for (int i = 0; i < index ; i++){
            nTree.add(toBullets[1][i]);
        }
        int sum = 0;
        for (Map.Entry<Integer, NTree.Counter> entry : nTree.map.entrySet()){
            sum += entry.getKey() * entry.getValue().count;
        }
        System.out.println(System.currentTimeMillis() - t);
        stream.println(sum / n);
        fc.close();
        stream.close();
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void ensureCapa(int[][] toBullets, int x) {
        int[] ints = new int[toBullets[x].length * 2];
        System.arraycopy(toBullets[x], 0, ints, 0, toBullets[x].length);
        toBullets[x] = ints;
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
