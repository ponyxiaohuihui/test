package com.huihui.grpc.stream;

import com.google.common.util.concurrent.ListenableFuture;
import com.huihui.grpc.Client;
import io.grpc.Deadline;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author pony
 * Created by pony on 2020/8/20
 */
public class StreamClient extends Client {
    public static void main(String[] args) throws Exception {
        startClient();
//        blockingDemo();
//        futureDemo();
        demo();
    }

    //BlockingStub只支持BlockBlock, BlockStream两种模式
    private static void blockingDemo() {
        StreamServiceGrpc.StreamServiceBlockingStub stub = StreamServiceGrpc.newBlockingStub(channel);
        System.out.println(stub.blockBlock(REQ.newBuilder().setReq("blockingReqbb").build()));
        //设置(DeadLine)超时
        try {
            stub = StreamServiceGrpc.newBlockingStub(channel).withDeadline(Deadline.after(1, TimeUnit.SECONDS));
            System.out.println(stub.blockBlock(REQ.newBuilder().setReq("blockingReqbb").build()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stub = StreamServiceGrpc.newBlockingStub(channel);
        //client消费速度快与server生产
        Iterator<RES> resIterator = stub.blockStream(REQ.newBuilder().setReq("blockingReqbsfast").build());
        long t = System.currentTimeMillis();
        while (resIterator.hasNext()) {
            System.out.println("client receive " + resIterator.next() + "cost " + (System.currentTimeMillis() - t));
            t = System.currentTimeMillis();
        }
        //client消费速度慢与server生产
        Iterator<RES> delayIterator = stub.blockStream(REQ.newBuilder().setReq("blockingReqbslow").build());
        while (delayIterator.hasNext()) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
            System.out.println("client receive " + delayIterator.next() + "cost" + (System.currentTimeMillis() - t));
            t = System.currentTimeMillis();
        }
    }

    //FutureStub只支持BlockBlock请求
    private static void futureDemo() throws Exception {
        StreamServiceGrpc.StreamServiceFutureStub stub = StreamServiceGrpc.newFutureStub(channel);
        ListenableFuture<RES> bb = stub.blockBlock(REQ.newBuilder().setReq("bb").build());
        System.out.println(bb.get());
        //设置(DeadLine)超时
        try {
            stub = StreamServiceGrpc.newFutureStub(channel).withDeadline(Deadline.after(1, TimeUnit.SECONDS));
            System.out.println(stub.blockBlock(REQ.newBuilder().setReq("bb").build()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void demo() throws Exception {
//        demobb();
//        demobs();
        demosb();
//        demoss();
    }
    private static void demobb() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);
        stub.blockBlock(REQ.newBuilder().setReq("bb").build(), new StreamObserver<RES>() {
            long t = System.currentTimeMillis();
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
    private static void demobs() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);
        stub.blockStream(REQ.newBuilder().setReq("bs").build(), new StreamObserver<RES>() {
            long t = System.currentTimeMillis();
            @Override
            public void onNext(RES res) {
                System.out.println("client receive " + res + "cost" + (System.currentTimeMillis() - t));
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
    private static void demosb() throws Exception {
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
                clientCallStreamObserver.disableAutoInboundFlowControl();
                clientCallStreamObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        while (clientCallStreamObserver.isReady()){
                            if (reqCount-- > 0) {
                                clientCallStreamObserver.onNext(REQ.newBuilder().setReq("ss" + reqCount).build());
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
    private static void demoss() throws Exception {
        StreamServiceGrpc.StreamServiceStub stub = StreamServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);


        stub.streamStream(new ClientResponseObserver<REQ, RES>() {
            long t = System.currentTimeMillis();
            int reqCount = 5;
            private ClientCallStreamObserver<REQ> clientCallStreamObserver;

            @Override
            public void onNext(RES req) {
                System.out.println("client receive " + req + "cost" + (System.currentTimeMillis() - t));
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
                clientCallStreamObserver.disableAutoInboundFlowControl();
                clientCallStreamObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        while (clientCallStreamObserver.isReady()){
                            if (reqCount-- > 0) {
                                clientCallStreamObserver.onNext(REQ.newBuilder().setReq("ss" + reqCount).build());
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
