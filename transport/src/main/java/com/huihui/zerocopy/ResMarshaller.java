package com.huihui.zerocopy;

import com.google.common.base.Preconditions;
import com.google.protobuf.UnsafeByteOperations;
import com.google.protobuf.WireFormat;
import com.huihui.grpc.zerocopy.ZeroCopyRES;
import io.grpc.Drainable;
import io.grpc.MethodDescriptor;
import io.grpc.internal.CompositeReadableBuffer;
import io.grpc.internal.ReadableBuffer;
import io.grpc.internal.ReadableBuffers;
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.buffer.PooledByteBufAllocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pony
 * Created by pony on 2020/9/1
 */
public class ResMarshaller implements MethodDescriptor.Marshaller<ZeroCopyRES> {
    private Map<ZeroCopyRES, ByteBuf[]> bufMap = new ConcurrentHashMap<>();
    byte[] bytes = new byte[10000000];
    {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (i);
        }
    }
    byte[] bytesR = new byte[10000000];

    @Override
    public InputStream stream(ZeroCopyRES zeroCopyRES) {
        return new BufferInputStream(zeroCopyRES);
    }

    @Override
    public ZeroCopyRES parse(InputStream inputStream) {

//        ReadableBuffer rawBuffer = ZeroCopyUtils.getBufferFromStream(inputStream);
        try {
            inputStream.read(bytesR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ZeroCopyRES.newBuilder().setRes(UnsafeByteOperations.unsafeWrap(bytesR)).build();
    }

    public void set(ZeroCopyRES res, ByteBuf[] o) {
        bufMap.put(res, o);
    }

    public class BufferInputStream extends InputStream implements Drainable {

        private final ZeroCopyRES zeroCopyRES;

        public BufferInputStream(ZeroCopyRES zeroCopyRES) {

            this.zeroCopyRES = zeroCopyRES;
        }

        @Override
        public int drainTo(OutputStream outputStream) throws IOException {

            outputStream.write(bytes);
//            ZeroCopyUtils.addBuffersToStream( new ByteBuf[]{buffer}, outputStream);
            return 1;
        }

        @Override
        public int read() throws IOException {
           throw new RuntimeException();
        }
    }
}
