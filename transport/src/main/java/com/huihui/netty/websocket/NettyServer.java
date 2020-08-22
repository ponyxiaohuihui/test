package com.huihui.netty.websocket;

import com.huihui.netty.Server;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
