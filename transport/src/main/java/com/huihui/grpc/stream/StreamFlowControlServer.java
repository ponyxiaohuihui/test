package com.huihui.grpc.stream;

import com.huihui.grpc.Server;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

/**
 * @author pony
 * Created by pony on 2020/8/20
 */
public class StreamFlowControlServer extends Server {
    public static void main(String[] args) throws Exception {
        startServer(new Service());
    }

    static class Service extends StreamServiceGrpc.StreamServiceImplBase {
        @Override
        //用不到flowcontrol,能走到beforestart，发一个不需要流控，isready一开始是true
        public void blockBlock(REQ request, StreamObserver<RES> responseObserver) {
            blockBlockFinal(request, responseObserver);

        }

        public void blockBlockFlowControl(REQ request, StreamObserver<RES> responseObserver) {
            ServerCallStreamObserver serverCallStreamObserver = (ServerCallStreamObserver) responseObserver;
            serverCallStreamObserver.disableAutoRequest();
            serverCallStreamObserver.setOnReadyHandler(() -> {
                RES res = RES.newBuilder().setRes(request.getReq()).build();
                System.out.println("server bb response : " + res);
                responseObserver.onNext(res);
                // 只能onNext一次，要不client端会报错,需要多次onNext,则走BS模式
                //    responseObserver.onNext(res);
                responseObserver.onCompleted();
            });

        }

        public void blockBlockFinal(REQ request, StreamObserver<RES> responseObserver) {
            RES res = RES.newBuilder().setRes(request.getReq()).build();
            System.out.println("server bb response : " + res);
            responseObserver.onNext(res);
            // 只能onNext一次，要不client端会报错,需要多次onNext,则走BS模式
            //    responseObserver.onNext(res);
            responseObserver.onCompleted();

        }

             @Override
        public void blockStream(REQ request, StreamObserver<RES> responseObserver) {
            blockStreamFlowControl(request, responseObserver);
        }

        public void blockStreamFlowControl(REQ request, StreamObserver<RES> responseObserver) {
            int idx = 0;
            ServerCallStreamObserver serverCallStreamObserver = (ServerCallStreamObserver) responseObserver;
            serverCallStreamObserver.disableAutoRequest();
            StringBuilder sb  = new StringBuilder();
            for (int i = 0; i < 5000; i++) {
                sb.append(i);
            }
            final String tag = sb.toString();
            class OnReadyHandler implements Runnable {
                long t = System.currentTimeMillis();
                int idx = 0;
                @Override
                public void run() {
                    if (idx == 5){
                        serverCallStreamObserver.onCompleted();
                    }
                    while (serverCallStreamObserver.isReady() && idx ++ < 5) {
                        System.out.println("blockStream ready");
                        RES res = RES.newBuilder().setRes(request.getReq() + idx + tag).build();
                        System.out.println("server bs response : " + res + "cost " + (System.currentTimeMillis() - t));
                        t = System.currentTimeMillis();
                    }
                }
            }
            final OnReadyHandler onReadyHandler = new OnReadyHandler();
            serverCallStreamObserver.setOnReadyHandler(onReadyHandler);
            serverCallStreamObserver.onCompleted();
        }


        @Override
        public StreamObserver<REQ> streamBlock(StreamObserver<RES> responseObserver) {
            ServerCallStreamObserver serverCallStreamObserver = (ServerCallStreamObserver) responseObserver;
            serverCallStreamObserver.disableAutoInboundFlowControl();

            class OnReadyHandler implements Runnable {
                private boolean wasReady = false;

                @Override
                public void run() {
                    if (serverCallStreamObserver.isReady() && !wasReady) {
                        wasReady = true;
                        System.out.println("streamBlock ready");
                        serverCallStreamObserver.request(1);
                    }
                }
            }
            final OnReadyHandler onReadyHandler = new OnReadyHandler();
            serverCallStreamObserver.setOnReadyHandler(onReadyHandler);
            return new StreamObserver<REQ>() {
                String s = "";

                @Override
                public void onNext(REQ req) {
                    s += req.getReq();
                    if (serverCallStreamObserver.isReady()) {
                        serverCallStreamObserver.request(1);
                    } else {
                        onReadyHandler.wasReady = false;
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    responseObserver.onError(throwable);
                }

                @Override
                public void onCompleted() {
                    System.out.println("sb complete");
                    responseObserver.onNext(RES.newBuilder().setRes(s).build());
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<REQ> streamStream(StreamObserver<RES> responseObserver) {
            ServerCallStreamObserver serverCallStreamObserver = (ServerCallStreamObserver) responseObserver;
            serverCallStreamObserver.disableAutoInboundFlowControl();

            class OnReadyHandler implements Runnable {
                private boolean wasReady = false;

                @Override
                public void run() {
                    if (serverCallStreamObserver.isReady() && !wasReady) {
                        wasReady = true;
                        System.out.println("streamStream ready");
                        serverCallStreamObserver.request(1);
                    }
                }
            }
            final OnReadyHandler onReadyHandler = new OnReadyHandler();
            serverCallStreamObserver.setOnReadyHandler(onReadyHandler);
            return new StreamObserver<REQ>() {
                int idx = 0;

                @Override
                public void onNext(REQ req) {
                    try {
                        System.out.println("ss receive req " + req);
                        Thread.sleep(2000);
                        RES res = RES.newBuilder().setRes(req.getReq() + idx++).build();
                        responseObserver.onNext(res);
                        System.out.println("ss send res " + res);
                        if (serverCallStreamObserver.isReady()) {
                            serverCallStreamObserver.request(1);
                        } else {
                            onReadyHandler.wasReady = false;
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        responseObserver.onError(
                                Status.UNKNOWN.withDescription("Error handling request").withCause(throwable).asException());
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    responseObserver.onError(throwable);
                }

                @Override
                public void onCompleted() {
                    System.out.println("ss complete");
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
