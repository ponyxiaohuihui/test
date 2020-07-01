package com.winphysoft.netty.websocket;

import com.winphysoft.netty.Server;
import com.winphysoft.netty.heartbeat.HeartBeatHandler;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServer extends Server {
    public static void main(String[] args) throws Exception{
        startServer(new HttpServerCodec(),
                new ChunkedWriteHandler(),
                /**
                 * http 聚合
                 */
                new HttpObjectAggregator(8192),
                /**
                 * ws://localhost:port/wstest
                 */
                new WebSocketServerProtocolHandler("/wstest"),
                new WSHandler());

    }
}
