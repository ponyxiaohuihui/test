package com.winphysoft.basic.contest.fee;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.RandomAccess;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/12/11
 */
class Perf {

    private static final int cap = 100000000;

    public static void main(String[] args) throws Exception{
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\data\\nio", "rw");
        final ByteBuffer heap = ByteBuffer.allocate(cap);
        final ByteBuffer offHeap = ByteBuffer.allocateDirect(cap);
        final ByteBuffer nioBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, cap * 4);
        fill(heap, offHeap, nioBuffer);
        for (int i = 0; i < 10; i++) {
            perf(offHeap);
            perf(heap);
            perf(nioBuffer);
            System.out.println("   ");
        }
       // perf(new AnchoreBuffer(heap));
    }

    static void fill(ByteBuffer heap, ByteBuffer offHeap, ByteBuffer nioBuffer) {
        final Random r = new Random();
        for (int i = 0; i < cap; i++) {
            final byte b = (byte) r.nextInt(255);
            heap.put(b);
            offHeap.put(b);
            nioBuffer.put(b);
        }
    }

    static int perf(AnchoreBuffer buffer) {
        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < cap; i++) {
            sum += buffer.get(i);
        }
        System.out.println(System.nanoTime() - start);
        return sum;
    }

    static int perf(ByteBuffer buffer) {
        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < cap; i++) {
            sum += buffer.get(i);
        }
        System.out.println((System.nanoTime() - start)/1000000);
        return sum;
    }

    static class AnchoreBuffer{
        private ByteBuffer byteBuffer;

        public AnchoreBuffer(ByteBuffer heap) {
            this.byteBuffer = heap;
        }

        byte get(int i){
            return byteBuffer.get(i);
        }

    }
}
