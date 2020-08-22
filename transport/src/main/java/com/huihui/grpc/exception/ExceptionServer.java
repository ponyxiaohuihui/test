package com.huihui.grpc.exception;

import com.google.protobuf.Empty;
import com.huihui.grpc.Server;
import io.grpc.stub.StreamObserver;

/**
 * @author pony
 * Created by pony on 2020/8/10
 */
public class ExceptionServer extends Server {
    public static void main(String[] args) throws Exception{
        startServer(new ExceptionServiceGrpc.ExceptionServiceImplBase(){
            @Override
            public void exception(Empty request, StreamObserver<Empty> responseObserver) {
                try {
                    ExceptionCreator.throwException();
                } catch (Throwable t){
                    responseObserver.onError(ExceptionCreator.getException(t));
                }
            }

            @Override
            public StreamObserver<Empty> streamException(StreamObserver<Empty> responseObserver) {
                return super.streamException(responseObserver);
            }
        });
    }
}
