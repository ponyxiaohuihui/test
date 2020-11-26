package com.huihui.grpc.stream;

import com.google.protobuf.ByteString;
import com.huihui.grpc.Client;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;

import java.util.concurrent.Semaphore;

/**
 * @author pony
 * Created by pony on 2020/8/20
 */
public class StreamFlowControlClient extends Client {
    public static void main(String[] args) throws Exception {
        startClient();
        demoFlowControl();
    }


    private static void demoFlowControl() throws Exception {
//        demobbFlowControl();
//        demobsFlowControl();
//        demosbFlowControl();
        demossFlowControl();

    }


    private static void demobbFlowControl() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);
        //用不到flowcontrol,能走到beforestart，发一个不需要流控，isready一开始是true
        stub.blockBlock(REQ.newBuilder().setReq("bb").build(), new ClientResponseObserver<REQ, RES>() {

            long t = System.currentTimeMillis();
            private ClientCallStreamObserver clientCallStreamObserver;

            @Override
            public void beforeStart(ClientCallStreamObserver clientCallStreamObserver) {
                this.clientCallStreamObserver = clientCallStreamObserver;
                clientCallStreamObserver.disableAutoRequestWithInitial(1);
                clientCallStreamObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("not here");
                    }
                });
            }

            @Override
            public void onNext(RES res) {
                System.out.println("client receive " + res + "cost" + (System.currentTimeMillis() - t));
                semaphore.release();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("complete ");
                semaphore.release();
            }
        });
        semaphore.acquire();
    }


    private static void demobsFlowControl() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);

        stub.blockStream(REQ.newBuilder().setReq("bs").build(), new ClientResponseObserver<REQ, RES>() {

            long t = System.currentTimeMillis();
            private ClientCallStreamObserver clientCallStreamObserver;

            @Override
            //只发一个请求，不需发送端流控
            public void beforeStart(ClientCallStreamObserver clientCallStreamObserver) {
                this.clientCallStreamObserver = clientCallStreamObserver;
                clientCallStreamObserver.disableAutoRequestWithInitial(1);
                clientCallStreamObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("not here");
                    }
                });
            }

            @Override
            //接收端消费完一个之后再request一个
            public void onNext(RES res) {
                System.out.println("client receive " + res + "cost" + (System.currentTimeMillis() - t));
                clientCallStreamObserver.request(1);
//                semaphore.release();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("complete ");
                semaphore.release();
            }
        });
        semaphore.acquire();
    }

    private static void demosbFlowControl() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);


        stub.streamBlock(new ClientResponseObserver<REQ, RES>() {
            long t = System.currentTimeMillis();
            int reqCount = 5;
            private ClientCallStreamObserver<REQ> clientCallStreamObserver;

            @Override
            public void onNext(RES req) {
                System.out.println("client receive " + req + "cost" + (System.currentTimeMillis() - t));
                t = System.currentTimeMillis();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                semaphore.release();
            }

            @Override
            public void onCompleted() {
                reqCount = 0;
                semaphore.release();
            }

            @Override
            public void beforeStart(ClientCallStreamObserver<REQ> clientCallStreamObserver) {

                this.clientCallStreamObserver = clientCallStreamObserver;
                clientCallStreamObserver.disableAutoRequestWithInitial(1);
                clientCallStreamObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        while (clientCallStreamObserver.isReady()) {
                            if (reqCount-- > 0) {
                                clientCallStreamObserver.onNext(REQ.newBuilder().setReq("sb" + reqCount).setData(ByteString.copyFrom(new byte[200000])).build());
                            } else {
                                clientCallStreamObserver.onCompleted();
                            }
                        }
                    }
                });
            }
        });

        semaphore.acquire();
    }

    private static void demossFlowControl() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);


        stub.streamStream(new ClientResponseObserver<REQ, RES>() {
            long t = System.currentTimeMillis();
            int reqCount = 5;
            private ClientCallStreamObserver<REQ> clientCallStreamObserver;

            @Override
            public void onNext(RES req) {
                System.out.println("client receive " + req.getRes() + "cost" + (System.currentTimeMillis() - t));
                t = System.currentTimeMillis();
                clientCallStreamObserver.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                semaphore.release();
            }

            @Override
            public void onCompleted() {
                reqCount = 0;
                semaphore.release();
            }

            @Override
            public void beforeStart(ClientCallStreamObserver<REQ> clientCallStreamObserver) {

                this.clientCallStreamObserver = clientCallStreamObserver;
                clientCallStreamObserver.disableAutoRequestWithInitial(1);
                clientCallStreamObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        while (clientCallStreamObserver.isReady()) {
                            if (reqCount-- > 0) {
                                clientCallStreamObserver.onNext(REQ.newBuilder().setReq("ss" + reqCount).setData(ByteString.copyFrom(new byte[200000])).build());
                            } else {
                                clientCallStreamObserver.onCompleted();
                            }
                        }
                    }
                });
            }
        });

        semaphore.acquire();
    }

}
