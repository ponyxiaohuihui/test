package com.winphysoft.basic.contest.fee;


import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        RandomAccessFile fs = new RandomAccessFile("C://codes//data//4.in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = new PrintStream("C://codes//data//4.out");
        int size = intBuffer.get();
        hashGroup(intBuffer, stream, size);
        fc.close();
        stream.close();
    }

    public static void hashGroup( IntBuffer intBuffer, PrintStream stream, int size) throws IOException {
        long t = System.currentTimeMillis();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int id = intBuffer.get();
            int fee = intBuffer.get();
            Integer value = map.get(id);
            value = value == null ? fee : value + fee;
            map.put(id, value);
        }
        List<Integer> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        Collections.sort(keys);
        Collections.sort(values);
        stream.println(keys.get(keys.size() / 2));
        stream.println(values.get(values.size() / 2));
        System.out.println(System.currentTimeMillis() - t);
    }

    public static void bulletGroup(FileChannel fc, IntBuffer intBuffer, PrintStream stream, int size, int n) throws IOException {
        long t = System.currentTimeMillis();
        int[][] fromBullets = new int[8][size];
        int[][] toBullets = new int[8][size];
        int[][] tempBullet = null;
        int[] fromsize = new int[8];
        int[] tosize = new int[8];
        for (int i = 0; i < size; i++) {
            int id = intBuffer.get();
            int x = id & 0x7;
            fromBullets[x][fromsize[x]++] = id;
        }
        for (int i = 0; i < 9; i++) {
            int shift = 3 * (i + 1);
            int mod = 0x7 << shift;
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < fromsize[j]; k++) {
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < fromsize[i]; j++) {
                int id = fromBullets[i][j];
                if (toBullets[0][index] == id) {
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
        System.out.println(System.currentTimeMillis() - t);
    }

    public static void arrayGroup(IntBuffer intBuffer, PrintStream stream, int size) throws IOException {
        long t = System.currentTimeMillis();
        int total = 0;
        int[] results = new int[1500000000];
        Arrays.fill(results, -1);
        Set<Integer> set = new HashSet();
        for (int i = 0; i < size; i++) {
            int id = intBuffer.get();
            int fee = intBuffer.get();
            set.add(id);
            if (results[id] == -1) {
                results[id] = fee;
                total++;
            } else {
                results[id] += fee;
            }
        }
        System.out.println("array cost 1 " + (System.currentTimeMillis() - t));
        int index = total / 2;
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < results.length; i++) {
            if (results[i] != -1){
                set.remove(i);
                if (index-- == 0){
                    stream.println(i);
                }
                values.add(results[i]);
            }
        }
        Collections.sort(values);
        stream.println(values.get(values.size() / 2));
        System.out.println("array cost 2 " + (System.currentTimeMillis() - t));
    }

}
