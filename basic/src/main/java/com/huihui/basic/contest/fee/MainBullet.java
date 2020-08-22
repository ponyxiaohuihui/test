package com.huihui.basic.contest.fee;


import sun.misc.Unsafe;

import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;

public class MainBullet {
    static LinkedList<long[]> arrayList = new LinkedList();
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        RandomAccessFile fs = new RandomAccessFile("C://codes//data//4.in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = System.out;
        int size = intBuffer.get();
        BulletGroup group = new BulletGroup(32);
        for (int i = 0 ; i< size ; i++){
            long id = intBuffer.get();
            int fee = intBuffer.get();
            long value = (id << 32) + fee;
            group.add(value);
        }
        for (int i = 1; i < 4; i++){
            BulletGroup to = new BulletGroup((4 - i) * 8);
            group.transferTo(to);
            group = to;
        }
        Bullet groupBullet = group.groupToBullet();
        stream.println((int)groupBullet.get(groupBullet.size() / 2));
        System.out.println(System.currentTimeMillis() - t);
        int middle = groupBullet.size() / 2;
        BulletGroup to = new BulletGroup(8);
        for (int i = 0; i < groupBullet.size(); i++){
            to.add(groupBullet.get(i));
        }
        group = to;
        Bullet middleBullet = group.getBullet(middle);
        int leftIndex = group.getLeftIndex(middle);
        for (int i = 1; i < 4; i++){
            to = new BulletGroup((i + 1) * 8);
            for (int j = 0; j < middleBullet.size(); j++){
                to.add(middleBullet.get(j));
            }
            group = to;
            middleBullet = group.getBullet(leftIndex);
            leftIndex = group.getLeftIndex(leftIndex);
        }
        stream.println(middleBullet.get(leftIndex) >> 32);
        fc.close();
        System.out.println(System.currentTimeMillis() - t);
        stream.close();
    }

    static long[] getArray(){
        if (arrayList.isEmpty()){
            return new long[1 << 16];
        }
        return arrayList.pollLast();
    }

    static void pushArray(long[] array){
        Arrays.fill(array, 0);
        arrayList.add(array);
    }

    static class BulletGroup{
        Bullet[] bullets;
        int shift;
        public BulletGroup(int shift) {
            bullets = new Bullet[256];
            for (int i = 0; i < bullets.length; i++) {
                bullets[i] = new Bullet();
            }
            this.shift = 64 - shift;
        }

        public void add(long value){
            bullets[(int) ((value >> shift) & 0xFF)].add(value);
        }


        public void transferTo(BulletGroup to) {
            for (Bullet bullet : bullets){
                for (int i = 0; i < bullet.size(); i++){
                    to.add(bullet.get(i));
                }
            }
            for (Bullet bullet : bullets){
                bullet.gc();
            }
        }

        public Bullet groupToBullet() {
            Bullet bullet = new Bullet();
            for (Bullet one : bullets){
                int key = 0;
                try{
                    key = (int) (one.get(0) >> 32);
                } catch (Exception e){
                    continue;
                }
                int sum = 0;
                for (int i = 0; i < one.size(); i++){
                    long value = one.get(i);
                    int vkey = (int) (value >> 32);
                    if (vkey == key){
                        sum += (int)value;
                    } else {
                        bullet.add(((long)sum << 32)+ key);
                        key = vkey;
                        sum = (int) value;
                    }
                }
                if ((int)bullet.get(bullet.size() - 1) != key){
                    bullet.add(((long)sum << 32)+ key);
                }
                one.gc();
            }
            return bullet;
        }

        public Bullet getBullet(int index) {
            int length = 0;
            for(int i = 0; i < bullets.length;i++){
                if (length <= index && length + bullets[i].size() > index){
                    return bullets[i];
                }
                length += bullets[i].size();
            }
            return null;
        }

        public int getLeftIndex(int index) {
            int length = 0;
            for(int i = 0; i < bullets.length;i++){
                if (length <= index && length + bullets[i].size() > index){
                    return index-length;
                }
                length += bullets[i].size();
            }
            return 0;
        }
    }

    public static final Unsafe UNSAFE;

    static {
        Unsafe unsafe;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Throwable cause) {
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    static class Bullet {
        private long address;

        private int size = -1;

        private long maxSize = 1 << 16;
//        //一个分块的大小
//        private int pageSize = 16;
//        private int pageMode = 0xFFFF;

        Bullet() {
            address = UNSAFE.allocateMemory(maxSize << 3);
        }

        public void add(long value) {
            if (size++ > maxSize){
                maxSize = maxSize << 1;
                address = UNSAFE.reallocateMemory(address, maxSize << 3);
            }
            UNSAFE.putLong(address + (size<< 3), value);
        }

        public long get(int index) {
            return UNSAFE.getLong(address + (index << 3));
        }

        public int size() {
            return size;
        }

        public void gc(){
          //UNSAFE.freeMemory(address);
        }

    }


}
