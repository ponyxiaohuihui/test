package com.winphysoft.test.contest.fee;

import sun.misc.Unsafe;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class MainSwiss {
    public static final Unsafe UNSAFE;
    public static int STATISTIC = 0;
    static boolean isOj = false;

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

    static void assertEq(int a, int b) {
        if (a != b) {
            throw new RuntimeException(a + " != " + b);
        }
    }

    public static void main(String[] args) throws Exception {
        isOj = args.length != 1;
        boolean isCheck = true;
        STATISTIC = 0;
        long st = System.currentTimeMillis();
        long t = st;
        RandomAccessFile fs = new RandomAccessFile("C://codes//data//4.in", "r");
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
        PrintStream stream = System.out;
        int size = intBuffer.get();
        int SIZE = 6295592;
        IntKeyHashMap mymap = new IntKeyHashMap(SIZE * 2);
        for (int i = 0; i < size; i++) {
            int id = intBuffer.get();
            int fee = intBuffer.get();
            mymap.emplace(id, fee);
        }
        System.out.println("map time " + (System.currentTimeMillis() - st));
        st = System.currentTimeMillis();

        long dataAddress = mymap.buckets;
        int dataBytes = mymap.bucketBytes;
        int bytesOffset = 0;
        for (int j = 0; j < dataBytes; j += 8) {
            long val = UNSAFE.getLong(dataAddress + j);
            if (val == -1) {
                continue;
            }
            UNSAFE.putLong(dataAddress + bytesOffset, val);
            bytesOffset += 8;
        }
        System.out.println("copy time " + (System.currentTimeMillis() - st));
        st = System.currentTimeMillis();
        int ans1 = 0, ans2 = 0;
        long start = dataAddress, end = dataAddress + bytesOffset - 8;
        ans1 = Tool.getKth(start, end, (int) mymap.size() / 2 + 1);
        start += 4;
        end += 4;
        ans2 = Tool.getKth(start, end, (int) mymap.size() / 2 + 1);
        stream.println(ans1);
        stream.println(ans2);
        System.out.println("get kth time " + (System.currentTimeMillis() - st));
        System.out.println("total " + (System.currentTimeMillis() - t));
    }

    static class Tool {
        private static long partition(long l, long r) {
            long m = l + (r - l) / 16 * 8;
            long mid = m;
            long i = l, j = r;
            int tmp = UNSAFE.getInt(i);
            while (i <= j) {
                if (tmp - UNSAFE.getInt(mid) > (i > m ? -1 : 0)) {
                    if (mid == i) {
                        mid = j;
                    } else if (mid == j) {
                        mid = i;
                    }
                    int tt = UNSAFE.getInt(j);
                    UNSAFE.putInt(i, tt);
                    //a[i] = a[j];
                    UNSAFE.putInt(j, tmp);
                    tmp = tt;
                    //a[j] = tmp;
                    j -= 8;
                } else {
                    i += 8;
                    tmp = UNSAFE.getInt(i);
                }
            }
            tmp = UNSAFE.getInt(mid);
            UNSAFE.putInt(mid, UNSAFE.getInt(j));
            //a[mid] = a[j];
            UNSAFE.putInt(j, tmp);
            //a[j] = tmp;
            return j;
        }

        static int getKth(long l, long r, int k) {
            if (l >= r) {
                return UNSAFE.getInt(l);
            }
            long m = partition(l, r);
            if (m == k * 8 + l - 8) {
                return UNSAFE.getInt(m);
            }
            if (m > k * 8 + l - 8) {
                return getKth(l, m - 8, k);
            }
            return getKth(m + 8, r, k - (int) (m - l) / 8 - 1);
        }

        public static void main(String[] args) {
            int[] data = new int[]{
                    1, 55,
                    26, 56,
                    7, 58,
                    9, 60,
                    15, 57,
                    2, 59,
            };
            long address = UNSAFE.allocateMemory(data.length * 4);
            UNSAFE.copyMemory(data, UNSAFE.arrayBaseOffset(int[].class), null, address, data.length * 4);
            long start = address, end = address + data.length * 4 - 8;
            assertEq(Tool.getKth(start, end, 1), 1);
            assertEq(Tool.getKth(start, end, 2), 2);
            assertEq(Tool.getKth(start, end, 3), 7);
            assertEq(Tool.getKth(start, end, 4), 9);
            assertEq(Tool.getKth(start, end, 5), 15);
            assertEq(Tool.getKth(start, end, 6), 26);
            start += 4;
            end += 4;
            assertEq(Tool.getKth(start, end, 1), 55);
            assertEq(Tool.getKth(start, end, 2), 56);
            assertEq(Tool.getKth(start, end, 3), 57);
            assertEq(Tool.getKth(start, end, 4), 58);
            assertEq(Tool.getKth(start, end, 5), 59);
            assertEq(Tool.getKth(start, end, 6), 60);
        }
    }

    static class IntKeyHashMap {
        // 控制字节
        static final byte EMPTY = (byte) 0b1111_1111;
        static final byte DELETED = (byte) 0b1000_0000;
        private static final long[] repeats = new long[256];

        static {
            for (byte b = Byte.MIN_VALUE; ; b++) {
                long repeat = b & 0xff;
                repeat = repeat | (repeat << 8);
                repeat = repeat | (repeat << 16);
                repeats[b + 128] = repeat | (repeat << 32);
                if (b == Byte.MAX_VALUE) {
                    break;
                }
            }
        }

        public int bucketBytes;
        public long buckets;
        private int bucketMask;
        private long ctrl;
        //private long values;
        private int length = 0;

        // elementSize: 每个key包含的int值数量
        public IntKeyHashMap(int bucketsSize) {
            int bucketSize = (1 << (31 - Integer.numberOfLeadingZeros(bucketsSize)));
            this.bucketMask = bucketSize - 1;
            this.buckets = UNSAFE.allocateMemory(bucketSize << 3);//new int[this.bucketSize];
            //this.values = UNSAFE.allocateMemory(this.bucketSize * 4);//new int[this.bucketSize];
            this.ctrl = UNSAFE.allocateMemory(bucketSize + Long.SIZE);//new byte[this.bucketSize + Long.SIZE];
            for (int i = 0; i < bucketSize; i += 8) {
                UNSAFE.putLong(this.ctrl + i, -1);
            }
            for (int i = bucketSize; i < bucketSize + 8; i++) {
                UNSAFE.putByte(this.ctrl + i, EMPTY);
            }
            bucketSize = bucketSize << 3;
            for (int i = 0; i < bucketSize; i += 8) {
                UNSAFE.putLong(this.buckets + i, -1);
            }
            this.bucketBytes = bucketSize;
        }

        static private byte h2(int hash) {
            //int top9 = (hash >>> 25);
            return (byte) (hash >>> 24);
        }

        private static long repeat(byte b) {
            return repeats[b + 128];
//            long repeat = b & 0xff;
//            repeat = repeat | (repeat << 8);
//            repeat = repeat | (repeat << 16);
//            return repeat | (repeat << 32);
        }

        private int findInsertSlot(long hash) {
            int pos = (int) hash & this.bucketMask;
            //int stride = 0; // 步长
            while (true) {
                long group = UNSAFE.getLong(ctrl + pos) & 0x80808080_80808080L;
                if (group != 0) {
                    return (pos + BitMask.lowestSetBit(group)) & this.bucketMask;
                }
                //stride += 8;
                pos += 8;
                pos &= this.bucketMask;
            }
        }

        // 返回buckets下标，找不到返回-1
        private int find(int key) {
            int pos = key & this.bucketMask;
            //int stride = 0; // 步长
            while (true) {
                long group = UNSAFE.getLong(ctrl + pos);
                long bitmask = Group.matchByte(group, (byte) (key >> 25));
                //int offset = BitMask.lowestSetBit(bitmask);
                int offsetBase = 0;
                while (bitmask != 0) {
                    //STATISTIC++;
                    //int offset = -1;
                    do {
                        if ((bitmask & 0xff) == 0) {
                            //offset = 1;
                            offsetBase++;
                            bitmask = bitmask >>> 8;
                        } else {
                            //bitmask = bitmask >>> 8;
                            //offsetBase++;
                            bitmask = bitmask & (bitmask - 1);
                            break;
                        }
                    } while (bitmask != 0);
                    int index = (pos + offsetBase) & this.bucketMask;
                    if (key == UNSAFE.getInt(this.buckets + (index << 3))) {
                        return index;
                    }
                    //bitmask = bitmask & (bitmask - 1);
                    //offset = BitMask.lowestSetBit(bitmask);
                }
                if (Group.matchEmptyOrDeleted(group) != 0) {
                    return -1;
                }
                //stride += Group.WIDTH;
                pos += 8;
                //pos &= this.bucketMask;
            }
        }

        // 不会检查元素是否存在
        private void insert(int key, int val) {
            int index = this.findInsertSlot(key);
            UNSAFE.putInt(this.buckets + (index << 3), key);
            UNSAFE.putInt(this.buckets + (index << 3) + 4, val);
            byte ctrl = (byte) (key >>> 25);
            UNSAFE.putByte(this.ctrl + index, ctrl);
            index = ((index - 8) & this.bucketMask) + 8;
            UNSAFE.putByte(this.ctrl + index, ctrl);
            this.length += 1;
        }

        public long size() {
            return this.length;
        }

        // 返回-1表示新插入
        public void emplace(int key, int value) {
            int foundIndex = this.find(key);
            if (foundIndex != -1) {
                UNSAFE.getAndAddInt(null, this.buckets + (foundIndex << 3) + 4, value);
                //long address = this.buckets + (foundIndex << 3) + 4;
                //UNSAFE.putInt(address, UNSAFE.getInt(address) + value);
            } else {
                // 插入新值
                this.insert(key, value);
            }
        }

        static class Group {
            static int WIDTH = 8;

            // return bitmask
            static long matchEmptyOrDeleted(long groupVal) {
                return groupVal & 0x80808080_80808080L;
            }

            // return bitmask
            static long matchByte(long groupVal, byte b) {
                long cmp = groupVal ^ repeats[b + 128];
                return (cmp - 0x01010101_01010101L) & ~cmp & 0x80808080_80808080L;
            }
        }

        static class BitMask {
            //static final private int THREE = 3;

            static int lowestSetBit(long bitmask) {
                if (bitmask == 0) {
                    return -1;
                }
                int ret = 0;
                if ((bitmask & 0xffffffffL) == 0) {
                    ret += 4;
                    bitmask = bitmask >>> 32;
                }
                if ((bitmask & 0xffff) == 0) {
                    ret += 2;
                    bitmask = bitmask >>> 16;
                }
                if ((bitmask & 0xff) == 0) {
                    ret += 1;
                }
                return ret;
            }
        }
    }

}