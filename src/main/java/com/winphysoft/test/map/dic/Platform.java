package com.winphysoft.test.map.dic;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public final class Platform {

    public static final Unsafe UNSAFE;

    public static final int BOOLEAN_ARRAY_OFFSET;

    public static final long BYTE_ARRAY_OFFSET;

    public static final int SHORT_ARRAY_OFFSET;

    public static final int CHAR_ARRAY_OFFSET;

    public static final int INT_ARRAY_OFFSET;

    public static final int LONG_ARRAY_OFFSET;

    public static final int FLOAT_ARRAY_OFFSET;

    public static final int DOUBLE_ARRAY_OFFSET;

    public static final long STRING_CHAR_ARRAY_OFFSET;

    private static final boolean unaligned;

    static {
        boolean _unaligned;
        String arch = System.getProperty("os.arch", "");
        if (arch.equals("ppc64le") || arch.equals("ppc64") || arch.equals("s390x")) {
            // Since java.nio.Bits.unaligned() doesn't return true on ppc (See JDK-8165231), but
            // ppc64 and ppc64le support it
            _unaligned = true;
        } else {
            try {
                Class<?> bitsClass =
                        Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
                Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned");
                unalignedMethod.setAccessible(true);
                _unaligned = Boolean.TRUE.equals(unalignedMethod.invoke(null));
            } catch (Throwable t) {
                // We at least know x86 and x64 support unaligned access.
                //noinspection DynamicRegexReplaceableByCompiledPattern
                _unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64|aarch64)$");
            }
        }
        unaligned = _unaligned;
    }

    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

    /**
     * @return true when running JVM is having sun's Unsafe package available in it and underlying
     * system having unaligned-access capability.
     */
    public static boolean unaligned() {
        return unaligned;
    }

    public static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    public static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    public static void putInt(Object base, long offset, int value) {
        UNSAFE.putInt(base, offset, value);
    }

    public static void addInt(long address, int value) {
        UNSAFE.getAndAddInt(null, address, value);
    }

    public static boolean getBoolean(Object object, long offset) {
        return UNSAFE.getBoolean(object, offset);
    }

    public static void putBoolean(Object object, long offset, boolean value) {
        UNSAFE.putBoolean(object, offset, value);
    }

    public static byte getByte(long offset) {
        return UNSAFE.getByte(offset);
    }

    public static void putByte(long offset, byte value) {
        UNSAFE.putByte(offset, value);
    }

    public static void putChar(long offset, char value) {
        UNSAFE.putChar(offset, value);
    }

    public static char getChar(long address) {
        return UNSAFE.getChar(address);
    }


    public static void putBoolean(long offset, boolean value) {
        UNSAFE.putBoolean(null, offset, value);
    }

    public static short getShort(Object object, long offset) {
        return UNSAFE.getShort(object, offset);
    }

    public static short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    public static void putShort(Object object, long offset, short value) {
        UNSAFE.putShort(object, offset, value);
    }

    public static long getLong(long offset) {
        return UNSAFE.getLong(offset);
    }

    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    public static float getFloat(Object object, long offset) {
        return UNSAFE.getFloat(object, offset);
    }

    public static void putFloat(Object object, long offset, float value) {
        UNSAFE.putFloat(object, offset, value);
    }

    public static double getDouble(long address) {
        return UNSAFE.getDouble(address);
    }

    public static void putDouble(final long address, final double value) {
        UNSAFE.putDouble(address, value);
    }

    public static void addDouble(final long address, final double value) {
        double old = getDouble(address);
        UNSAFE.putDouble(address, old + value);
    }

    public static Object getObject(Object object, long offset) {
        return UNSAFE.getObject(object, offset);
    }


    public static Object getObjectVolatile(Object object, long offset) {
        return UNSAFE.getObjectVolatile(object, offset);
    }

    public static void putObjectVolatile(Object object, long offset, Object value) {
        UNSAFE.putObjectVolatile(object, offset, value);
    }

    public static long allocateMemory(long size) {
        return UNSAFE.allocateMemory(size);
    }

    public static void freeMemory(long address) {
        UNSAFE.freeMemory(address);
    }

    public static long reallocateMemory(long address, long oldSize, long newSize) {
        long newMemory = UNSAFE.allocateMemory(newSize);
        copyMemory(null, address, null, newMemory, oldSize);
        freeMemory(address);
        return newMemory;
    }

    public static ByteBuffer allocateDirectBuffer(int size) {
        return ByteBuffer.allocateDirect(size);
    }

    public static void setMemory(Object object, long offset, long size, byte value) {
        UNSAFE.setMemory(object, offset, size, value);
    }

    public static void setMemory(long address, byte value, long size) {
        UNSAFE.setMemory(address, size, value);
    }

    public static void copyMemory(
            Object src, long srcOffset, Object dst, long dstOffset, long length) {
        // Check if dstOffset is before or after srcOffset to determine if we should copy
        // forward or backwards. This is necessary in case src and dst overlap.
        if (dstOffset < srcOffset) {
            while (length > 0) {
                long size = Math.min(length, UNSAFE_COPY_THRESHOLD);
                UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
                length -= size;
                srcOffset += size;
                dstOffset += size;
            }
        } else {
            srcOffset += length;
            dstOffset += length;
            while (length > 0) {
                long size = Math.min(length, UNSAFE_COPY_THRESHOLD);
                srcOffset -= size;
                dstOffset -= size;
                UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
                length -= size;
            }

        }
    }

    public static void copyMemoryNonOverlap(long src, long dst, long length) {
        UNSAFE.copyMemory(src, dst, length);
    }

    public static void copyMemoryNonOverlap(Object srcBase, long srcOffset, Object dstBase, long dstOffset, int length) {
        UNSAFE.copyMemory(srcBase, srcOffset, dstBase, dstOffset, length);
    }

    /**
     * Raises an exception bypassing compiler checks for checked exceptions.
     */
    public static void throwException(Throwable t) {
        UNSAFE.throwException(t);
    }

    /**
     * Limits the number of bytes to copy per {@link Unsafe#copyMemory(long, long, long)} to
     * allow safepoint polling during a large copy.
     */
    private static final long UNSAFE_COPY_THRESHOLD = 1024L * 1024L;

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

        if (UNSAFE != null) {
            BOOLEAN_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(boolean[].class);
            BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
            SHORT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(short[].class);
            CHAR_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(char[].class);
            INT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(int[].class);
            LONG_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
            FLOAT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(float[].class);
            DOUBLE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(double[].class);
            int stringOff = 0;
            try {
                Field f = String.class.getDeclaredField("value");
                f.setAccessible(true);
                stringOff = (int) UNSAFE.objectFieldOffset(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            STRING_CHAR_ARRAY_OFFSET = stringOff;
        } else {
            BOOLEAN_ARRAY_OFFSET = 0;
            BYTE_ARRAY_OFFSET = 0;
            SHORT_ARRAY_OFFSET = 0;
            CHAR_ARRAY_OFFSET = 0;
            INT_ARRAY_OFFSET = 0;
            LONG_ARRAY_OFFSET = 0;
            FLOAT_ARRAY_OFFSET = 0;
            DOUBLE_ARRAY_OFFSET = 0;
            STRING_CHAR_ARRAY_OFFSET = 0;
        }
    }
}
