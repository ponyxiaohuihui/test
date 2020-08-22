package com.huihui.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author pony
 * Created by pony on 2020/8/10
 */
public class Client {
    protected static ManagedChannel channel;

    protected static void startClient(){
        channel = ManagedChannelBuilder
                .forAddress("localhost", 12345)
                .maxInboundMessageSize(Integer.MAX_VALUE)
                .usePlaintext()
                .build();
    }
}
