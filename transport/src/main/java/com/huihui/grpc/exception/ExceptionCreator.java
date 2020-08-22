package com.huihui.grpc.exception;

import com.google.rpc.DebugInfo;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;

/**
 * @author pony
 * Created by pony on 2020/8/10
 */
public class ExceptionCreator {
    static final Metadata.Key<DebugInfo> DEBUG_INFO_TRAILER_KEY =
            ProtoUtils.keyForProto(DebugInfo.getDefaultInstance());


    static void throwException() throws Exception{
        throw new Exception("exception");
    }

    static Throwable getException(Throwable t){
        Metadata trailers = new Metadata();
        DebugInfo.Builder builder = DebugInfo.newBuilder();
        for (int i = 0; i < t.getStackTrace().length; i++) {
            builder.addStackEntries(t.getStackTrace()[i].toString());
        }
        trailers.put(DEBUG_INFO_TRAILER_KEY, builder.build());
        return Status.INTERNAL.withCause(t)
                .asRuntimeException(trailers);
    }

    static void printThrowable(Throwable t){
        Status status = Status.fromThrowable(t);
        Metadata trailers = Status.trailersFromThrowable(t);
        DebugInfo debugInfo = trailers.get(DEBUG_INFO_TRAILER_KEY);
        status.asException().printStackTrace();
        debugInfo.getStackEntriesList().forEach(str -> System.out.println(str));
    }
}
