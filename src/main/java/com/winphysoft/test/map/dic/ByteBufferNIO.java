package com.winphysoft.test.map.dic;

import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author pony
 * @version 1.1
 * 直接用NIO写String的bits
 * Created by pony on 2020/3/5
 */
public class ByteBufferNIO {
    //抄的bits的
    private static int JNI_COPY_TO_ARRAY_THRESHOLD = 6;
    private static int JNI_COPY_TO_CHAR_ARRAY_THRESHOLD = 3;
    private FileChannel fc;
    private int maxSize;
    private MappedByteBuffer byteBuffer;
    private long address;

    public ByteBufferNIO(String path) {
        this(path, Integer.MAX_VALUE);
    }

    public ByteBufferNIO(String path, int capacity) {
        try {
            fc = new RandomAccessFile(path, "rw").getChannel();
            byteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, capacity);
            Method m = byteBuffer.getClass().getDeclaredMethod("address");
            m.setAccessible(true);
            address = (long) m.invoke(byteBuffer);
            maxSize = byteBuffer.limit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putStringASCharArray(String s, int pos) {
        char[] stringChars = (char[]) Platform.getObject(s, Platform.STRING_CHAR_ARRAY_OFFSET);
        if(stringChars.length > JNI_COPY_TO_CHAR_ARRAY_THRESHOLD){
            Platform.copyMemoryNonOverlap(stringChars, Platform.CHAR_ARRAY_OFFSET, null, address + pos, stringChars.length << 1);
        } else {
            for (int i = 0; i < stringChars.length; i++) {
                Platform.putChar(address + pos + i, stringChars[i]);
            }
        }
    }

    public char[] getChars(int pos, int len) {
        char[] stringChars = new char[len];
        if(stringChars.length > JNI_COPY_TO_CHAR_ARRAY_THRESHOLD){
            Platform.copyMemoryNonOverlap(null, address + pos, stringChars, Platform.CHAR_ARRAY_OFFSET, stringChars.length << 1);
        } else {
            for (int i = 0; i < stringChars.length; i++) {
                stringChars[i] = Platform.getChar(address + pos + i);
            }
        }
        return stringChars;
    }

    public void putInt(int i, int pos) {
        Platform.putInt(address + pos, i);
    }

    private void force()  {
        byteBuffer.force();
    }

    private void release() throws Exception {
        byteBuffer.force();
        byteBuffer.clear();
        fc.close();
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = byteBuffer.getClass().getMethod("cleaner");
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner)
                            getCleanerMethod.invoke(byteBuffer);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
