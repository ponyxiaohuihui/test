package com.winphysoft.basic.contest.fee;

import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author SaltedFish
 * Created by SaltedFish on 2019/12/4
 **/

public class MainPart {

    private static int bucketSize = 15000;
    private static int section = 100000;
    private static int unitSize = 820;

    private static int findKthLargest(int[] nums, int left, int right, int k) {
        while (true) {
            int position = partition(nums, left, right);
            if (position == k - 1) {
                return nums[position];
            }
            else if (position > k - 1) {
                right = position - 1;
            }
            else {
                left = position + 1;
            }
        }
    }

    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[left];
        int l = left + 1;
        int r = right;
        while (l <= r) {
            while (l <= r && nums[l] >= pivot) {
                ++l;
            }
            while (l <= r && nums[r] <= pivot) {
                --r;
            }
            if (l <= r && nums[l] < pivot && nums[r] > pivot) {
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
                ++l;
                --r;
            }
        }
        nums[left] = nums[r];
        nums[r] = pivot;
        return r;
    }

    private static void read(IntBuffer intBuffer, int size, PrintStream stream) {
        long[] ropes = new long[bucketSize * unitSize];
        int[] counts = new int[bucketSize];
        int[] level = new int[section];
        int[] buffer = new int[unitSize];
        int[] indexArr = new int[bucketSize];
        int[] statistics = new int[size];

        for (int i = 0; i < size; i++) {
            int id = intBuffer.get();
            int value = intBuffer.get();
            int off = (id/section * unitSize) + (counts[id/section]++);
            ropes[off] = ((long) id << 32 | value);
        }

        int count = 0;
        int countId = 0;
        for (int i = 0; i < bucketSize; i++) {
            int len = counts[i];
            int from = i * section;
            int index = 0;
            int start = i * unitSize;
            for (int j = 0; j < len; j++) {
                int off = (int)(ropes[start + j] >> 32) - from;
                if (level[off] == 0) {
                    buffer[index++] = off;
                    level[off] = 0x80000000;
                }
                level[off] += (int)(ropes[start + j] & 0xffffffff);
            }
            for (int j = 0; j < index; j++) {
                statistics[count++] = level[buffer[j]] - 0x80000000;
                level[buffer[j]] = 0;
            }
            countId += index;
            indexArr[i] = index;
        }

        countId = countId/2;
        for (int i = 0; i < bucketSize; i++) {
            countId = countId - indexArr[i];
            if (countId < 0) {
                int[] sortRope = new int[section];
                int from = i * section;
                int start = i * unitSize;
                for (int j = 0; j < counts[i]; j++) {
                    sortRope[(int)(ropes[start + j] >> 32) - from] = (int)(ropes[start + j] >> 32);
                }
                int k = 0;
                int index = countId + indexArr[i];
                for (int j = 0; j < section; j++) {
                    if (sortRope[j] != 0) {
                        if (k == index) {
                            stream.println(sortRope[j]);
                            break;
                        }
                        ++k;
                    }
                }
                break;
            }
        }

        stream.println(findKthLargest(statistics, 0, count, (count+1)/2));
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        RandomAccessFile fs = new RandomAccessFile("C://codes//data//4.in", "r");
        //RandomAccessFile fs = new RandomAccessFile("in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = System.out;
        //PrintStream stream = new PrintStream("out");
        int size = intBuffer.get();
        read(intBuffer, size, stream);
        fc.close();
        stream.close();
        System.out.println("total cost: " + (System.currentTimeMillis() - start));
    }
}
