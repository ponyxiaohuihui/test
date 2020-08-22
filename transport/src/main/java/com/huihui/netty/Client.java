package com.huihui.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author pony
 * Created by pony on 2020/6/29
 */
public class Client {
    protected static void start(ChannelHandler... handlers) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(4);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            for (ChannelHandler handler : handlers) {
                                socketChannel.pipeline().addLast(handler);
                            }
                        }
                    });
            ChannelFuture sync = b.connect("localhost", 2333).sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
