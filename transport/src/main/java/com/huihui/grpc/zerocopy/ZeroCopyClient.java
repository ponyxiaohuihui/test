package com.huihui.grpc.zerocopy;

import com.huihui.grpc.Client;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.Semaphore;

/**
 * @author pony
 * Created by pony on 2020/8/20
 */
public class ZeroCopyClient extends Client {
    public static void main(String[] args) throws Exception {
        startClient();
        demo();
    }

    private static void demo() throws Exception {
        heap();
//              zero();
    }

    private static void heap() throws Exception {
        ZeroCopyServiceGrpc.ZeroCopyServiceStub zeroCopyServiceStub = ZeroCopyServiceGrpc.newStub(channel);
        Semaphore semaphore = new Semaphore(0);
        StreamObserver<ZeroCopyREQ> zeroCopyREQStreamObserver = zeroCopyServiceStub.heapCopy(new StreamObserver<ZeroCopyRES>() {
            int count = 0;
            long size = 0;
            @Override
            public void onNext(ZeroCopyRES zeroCopyRES) {
                size += zeroCopyRES.getRes().size();
                if(++count == 1111){
                    semaphore.release();
                    System.out.println(size);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("client error");
                throwable.printStackTrace();
                semaphore.release();
            }

            @Override
            public void onCompleted() {
                System.out.println("client complete");
                semaphore.release();
            }
        });
        ZeroCopyREQ req = ZeroCopyREQ.newBuilder().setE(EE.b).build();
        int idx = 1111;
        long t = System.currentTimeMillis();
        while (idx-- > 0) {
            zeroCopyREQStreamObserver.onNext(req);
        }
        semaphore.acquire();
        System.out.println("cost " + (System.currentTimeMillis() - t));
    }

    private static void zero() throws Exception {
        MethodDescriptor<ZeroCopyREQ, ZeroCopyRES> method = ZeroCopyServiceGrpc.getZeroCopyMethod()
                .toBuilder()
                .setResponseMarshaller(new ResMarshaller())
                .build();
        ZeroCopyServiceGrpc.ZeroCopyServiceStub zeroCopyServiceStub = ZeroCopyServiceGrpc.newStub(channel)
                .withOption(OVERRIDDEN_METHOD_DESCRIPTOR,
                        method);
        Semaphore semaphore = new Semaphore(0);



        StreamObserver<ZeroCopyREQ> zeroCopyREQStreamObserver = ClientCalls.asyncBidiStreamingCall(
                channel.newCall(method, zeroCopyServiceStub.getCallOptions()), new StreamObserver<ZeroCopyRES>() {
                    int count = 0;
                    long size = 0;

                    @Override
                    public void onNext(ZeroCopyRES zeroCopyRES) {
                        size += zeroCopyRES.getRes().size();
                        if (++count == 1111) {
                            semaphore.release();
                            System.out.println(size);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("client error");
                        throwable.printStackTrace();
                        semaphore.release();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("client complete");
                        semaphore.release();
                    }
                });
        ZeroCopyREQ req = ZeroCopyREQ.newBuilder().build();
        int idx = 1111;
        long t = System.currentTimeMillis();
        while (idx-- > 0) {
            zeroCopyREQStreamObserver.onNext(req);
        }
        semaphore.acquire();
        System.out.println("cost " + (System.currentTimeMillis() - t));
    }

}
