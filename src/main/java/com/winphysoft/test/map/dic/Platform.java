package com.winphysoft.test.map.dic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class Platform {

    public static final int BOOLEAN_ARRAY_OFFSET;
    public static final int BYTE_ARRAY_OFFSET;
    public static final int SHORT_ARRAY_OFFSET;
    public static final int INT_ARRAY_OFFSET;
    public static final int LONG_ARRAY_OFFSET;
    public static final int FLOAT_ARRAY_OFFSET;
    public static final int DOUBLE_ARRAY_OFFSET;
    public static final int STRING_CHARARRAY_OFFSET;
    public static final Unsafe _UNSAFE;

    static {
        Unsafe unsafe;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Throwable cause) {
            unsafe = null;
        }
        _UNSAFE = unsafe;

        if (_UNSAFE != null) {
            BOOLEAN_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(boolean[].class);
            BYTE_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(byte[].class);
            SHORT_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(short[].class);
            INT_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(int[].class);
            LONG_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(long[].class);
            FLOAT_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(float[].class);
            DOUBLE_ARRAY_OFFSET = _UNSAFE.arrayBaseOffset(double[].class);
            int stringOff = 0;
            try {
                Field f = String.class.getDeclaredField("value");
                f.setAccessible(true);
                stringOff = (int) _UNSAFE.objectFieldOffset(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            STRING_CHARARRAY_OFFSET = stringOff + _UNSAFE.arrayBaseOffset(char[].class);
        } else {
            BOOLEAN_ARRAY_OFFSET = 0;
            BYTE_ARRAY_OFFSET = 0;
            SHORT_ARRAY_OFFSET = 0;
            INT_ARRAY_OFFSET = 0;
            LONG_ARRAY_OFFSET = 0;
            FLOAT_ARRAY_OFFSET = 0;
            DOUBLE_ARRAY_OFFSET = 0;
            STRING_CHARARRAY_OFFSET = 0;
        }
    }
}
