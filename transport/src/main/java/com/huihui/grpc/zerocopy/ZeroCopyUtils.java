package com.huihui.grpc.zerocopy;

import io.grpc.internal.CompositeReadableBuffer;
import io.grpc.internal.ReadableBuffer;
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author pony
 * Created by pony on 2020/9/9
 */
public class ZeroCopyUtils {
    private static final String BUFFER_INPUT_STREAM_CLASS_NAME =
            "io.grpc.internal.ReadableBuffers$BufferInputStream";
    private static final String BUFFER_FIELD_NAME = "buffer";
    private static final String BUFFERS_FIELD_NAME = "buffers";
    private static final String NETTY_WRITABLE_BUFFER_CLASS_NAME =
            "io.grpc.netty.shaded.io.grpc.netty.NettyWritableBuffer";
    private static final String NETTY_READABLE_BUFFER_CLASS_NAME =
            "io.grpc.netty.shaded.io.grpc.netty.NettyReadableBuffer";
    private static final String BUFFER_CHAIN_OUTPUT_STREAM_CLASS_NAME =
            "io.grpc.internal.MessageFramer$BufferChainOutputStream";
    private static final String BUFFER_LIST_FIELD_NAME = "bufferList";
    private static final String CURRENT_FIELD_NAME = "current";

    private static Constructor<?> sNettyWritableBufferConstructor;
    private static Field sBufferList;
    private static Field sCompositeBuffers = null;
    private static Field sCurrent;
    private static Field sReadableBufferField = null;
    private static Field sReadableByteBuf = null;
    private static boolean sZeroCopySendSupported = true;
    private static boolean sZeroCopyReceiveSupported = true;

    static {
        try {
            sReadableBufferField = getPrivateField(BUFFER_INPUT_STREAM_CLASS_NAME, BUFFER_FIELD_NAME);
        } catch (Exception e) {
            sZeroCopySendSupported = false;
        }
        try {
            sNettyWritableBufferConstructor =
                    getPrivateConstructor(NETTY_WRITABLE_BUFFER_CLASS_NAME, ByteBuf.class);
            sBufferList = getPrivateField(BUFFER_CHAIN_OUTPUT_STREAM_CLASS_NAME, BUFFER_LIST_FIELD_NAME);
            sCurrent = getPrivateField(BUFFER_CHAIN_OUTPUT_STREAM_CLASS_NAME, CURRENT_FIELD_NAME);
            sCompositeBuffers =
                    getPrivateField(CompositeReadableBuffer.class.getName(), BUFFERS_FIELD_NAME);
            sReadableByteBuf = getPrivateField(NETTY_READABLE_BUFFER_CLASS_NAME, BUFFER_FIELD_NAME);
        } catch (Exception e) {
            System.out.println("Cannot get gRPC output stream buffer, zero copy receive will be disabled.");
            e.printStackTrace();
            sZeroCopyReceiveSupported = false;
        }
    }

    private static Field getPrivateField(String className, String fieldName)
            throws NoSuchFieldException, ClassNotFoundException {
        Class<?> declaringClass = Class.forName(className);
        Field field = declaringClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private static Constructor<?> getPrivateConstructor(String className, Class<?> ...parameterTypes)
            throws ClassNotFoundException, NoSuchMethodException {
        Class<?> declaringClass = Class.forName(className);
        Constructor<?> constructor = declaringClass.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor;
    }


    public static boolean addBuffersToStream(ByteBuf[] buffers, OutputStream stream) {
        if (!sZeroCopySendSupported || !stream.getClass().equals(sBufferList.getDeclaringClass())) {
            return false;
        }
        try {
            if (sCurrent.get(stream) != null) {
                return false;
            }
            for (ByteBuf buffer : buffers) {
                Object nettyBuffer = sNettyWritableBufferConstructor.newInstance(buffer);
                List list = (List) sBufferList.get(stream);
                list.add(nettyBuffer);
                buffer.retain();
                sCurrent.set(stream, nettyBuffer);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ReadableBuffer getBufferFromStream(InputStream stream) {
        if (!sZeroCopyReceiveSupported
                || !stream.getClass().equals(sReadableBufferField.getDeclaringClass())) {
            return null;
        }
        try {
            return (ReadableBuffer) sReadableBufferField.get(stream);
        } catch (Exception e) {
            System.out.println("Failed to get data buffer from stream.");
            e.printStackTrace();
            return null;
        }
    }
}
