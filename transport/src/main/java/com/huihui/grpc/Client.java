package com.huihui.grpc;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;

import java.lang.reflect.Method;

/**
 * @author pony
 * Created by pony on 2020/8/10
 */
public class Client {
    protected static ManagedChannel channel;

    public static final CallOptions.Key<MethodDescriptor> OVERRIDDEN_METHOD_DESCRIPTOR =
            CallOptions.Key.create("overridden method descriptor");

    protected static void startClient(){
        channel = ManagedChannelBuilder
                .forAddress("localhost", 12345)
                .maxInboundMessageSize(Integer.MAX_VALUE)
                .usePlaintext()
                .intercept(new ClientInterceptor() {
                    @Override
                    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
                        MethodDescriptor overridden = callOptions.getOption(OVERRIDDEN_METHOD_DESCRIPTOR);
                        if (overridden != null){
                            methodDescriptor = overridden;
                        }
                        return channel.newCall(methodDescriptor, callOptions);
                    }
                })
                .build();
    }
}
