package com.huihui.grpc;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;

/**
 * @author pony
 * Created by pony on 2020/8/10
 */
public class Server {
    protected static void startServer(BindableService... services) throws Exception{
        ServerBuilder serverBuilder = ServerBuilder.forPort(12345);
        for (int i = 0; i < services.length; i++) {
            serverBuilder.addService(services[i]);
        }
       serverBuilder
               .maxInboundMessageSize(Integer.MAX_VALUE)
               .build().start().awaitTermination();
    }
}
