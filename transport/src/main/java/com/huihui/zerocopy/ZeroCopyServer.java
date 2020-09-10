package com.huihui.zerocopy;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.google.protobuf.UnsafeByteOperations;
import com.huihui.grpc.Server;
import com.huihui.grpc.zerocopy.ZeroCopyREQ;
import com.huihui.grpc.zerocopy.ZeroCopyRES;
import com.huihui.grpc.zerocopy.ZeroCopyServiceGrpc;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.ServerMethodDefinition;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServiceDescriptor;
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.stub.StreamObserver;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author pony
 * Created by pony on 2020/8/20
 */
public class ZeroCopyServer extends Server {
    static byte[] bytes = new byte[10000000];
    static ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10000000);
    static ZeroCopyRES res = ZeroCopyRES.newBuilder().setRes(ByteString.copyFrom(bytes)).build();
    static ZeroCopyRES unsafeRes = ZeroCopyRES.newBuilder().setRes(UnsafeByteOperations.unsafeWrap(bytes)).build();
    static ZeroCopyRES unsafeDirectRes = ZeroCopyRES.newBuilder().setRes(UnsafeByteOperations.unsafeWrap(byteBuffer)).build();
    static {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (i);
            byteBuffer.put((byte) i);
        }
        byteBuffer.flip();
    }

    public static void main(String[] args) throws Exception {
        startServer(new BindService());
    }

    static class Service extends ZeroCopyServiceGrpc.ZeroCopyServiceImplBase {
        private ResMarshaller resMarshaller = new ResMarshaller();

        @Override
        public StreamObserver<ZeroCopyREQ> heapCopy(StreamObserver<ZeroCopyRES> responseObserver) {
            return new StreamObserver<ZeroCopyREQ>() {
                @Override
                public void onNext(ZeroCopyREQ zeroCopyREQ) {
                    responseObserver.onNext(ZeroCopyRES.newBuilder().setRes(UnsafeByteOperations.unsafeWrap(bytes)).build());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }

        @Override
        public StreamObserver<ZeroCopyREQ> zeroCopy(StreamObserver<ZeroCopyRES> responseObserver) {
            return new StreamObserver<ZeroCopyREQ>() {
                @Override
                public void onNext(ZeroCopyREQ zeroCopyREQ) {
                    ZeroCopyRES res = ZeroCopyRES.newBuilder().setRes(ByteString.copyFromUtf8(UUID.randomUUID().toString())).build();
                    resMarshaller.set(res, new ByteBuf[0]);
                    responseObserver.onNext(res);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }

        public Map<MethodDescriptor, MethodDescriptor> getOverriddenMethodDescriptors() {
            return ImmutableMap.of(
                    ZeroCopyServiceGrpc.getZeroCopyMethod(),
                    ZeroCopyServiceGrpc.getZeroCopyMethod().toBuilder()
                            .setResponseMarshaller(resMarshaller).build());
        }

    }

    static class BindService implements BindableService {
        Service service = new Service();

        @Override
        public ServerServiceDefinition bindService() {
            ServerServiceDefinition serverServiceDefinition = service.bindService();
            Map<MethodDescriptor, MethodDescriptor> overriddenMethodDescriptors = service.getOverriddenMethodDescriptors();
            List<ServerMethodDefinition> newMethods = new ArrayList<>();
            List<MethodDescriptor<?, ?>> newDescriptors = new ArrayList<>();
            for (final ServerMethodDefinition method : serverServiceDefinition.getMethods()) {
                MethodDescriptor descriptor = method.getMethodDescriptor();
                MethodDescriptor overriddenMethod = overriddenMethodDescriptors.get(descriptor);
                ServerMethodDefinition newMethod;
                if (overriddenMethod != null) {
                    newMethod = ServerMethodDefinition.create(overriddenMethod, method.getServerCallHandler());
                } else {
                    newMethod = method;
                }
                newDescriptors.add(newMethod.getMethodDescriptor());
                newMethods.add(newMethod);
            }
            final ServerServiceDefinition.Builder serviceBuilder = ServerServiceDefinition
                    .builder(new ServiceDescriptor(serverServiceDefinition.getServiceDescriptor().getName(), newDescriptors));
            for (ServerMethodDefinition<?, ?> definition : newMethods) {
                serviceBuilder.addMethod(definition);
            }
            return serviceBuilder.build();
        }
    }
}
