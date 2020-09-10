package com.huihui.zerocopy;

import com.huihui.grpc.zerocopy.ZeroCopyREQ;
import io.grpc.MethodDescriptor;

import java.io.InputStream;

/**
 * @author pony
 * Created by pony on 2020/9/1
 */
public class ReqMarshaller implements MethodDescriptor.Marshaller<ZeroCopyREQ> {

    @Override
    public InputStream stream(ZeroCopyREQ zeroCopyREQ) {
        return null;
    }

    @Override
    public ZeroCopyREQ parse(InputStream inputStream) {
        return null;
    }
}
