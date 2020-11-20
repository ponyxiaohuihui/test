package com.huihui.netty.base;

import io.grpc.netty.shaded.io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;

import java.nio.ByteBuffer;

/**
 * @author pony
 * Created by pony on 2020/10/29
 */
public class BufferCloseBeforeWrite {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1000000);
        long address = PlatformDependent.directBufferAddress(byteBuffer);
        byte[] bytes = new byte[1000];
        bytes[0] = 1;
        PlatformDependent.freeDirectBuffer(byteBuffer);
        PlatformDependent.copyMemory(bytes, 0, address,1000);
    }
}
