package com.huihui.grpc.exception;

import com.google.protobuf.Empty;
import com.huihui.grpc.Client;

/**
 * @author pony
 * Created by pony on 2020/8/10
 */
public class ExceptionClient extends Client {
    public static void main(String[] args) {
        startClient();
        ExceptionServiceGrpc.ExceptionServiceBlockingStub blockingStub = ExceptionServiceGrpc.newBlockingStub(channel);
        try {
            blockingStub.exception(Empty.getDefaultInstance());
        } catch (Throwable t){
            ExceptionCreator.printThrowable(t);
        }
    }
}
