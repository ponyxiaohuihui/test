package com.huihui.netty.heartbeat;

import com.huihui.netty.Server;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServer extends Server {
    public static void main(String[] args) throws Exception{
        startServer(new IdleStateHandler(4, 8, 16, TimeUnit.SECONDS), new HeartBeatHandler());
    }
}
