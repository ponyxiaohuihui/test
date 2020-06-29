package com.winphysoft.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author pony
 * Created by pony on 2020/6/29
 */
public class NettyClient {
    public static void main(String[] args) throws Exception{
        start1();
    }

    private static void start1() throws Exception{
        start(new LogClientHandler());
    }

    private static void start(ChannelHandler... handlers) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(4);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            for (ChannelHandler handler : handlers) {
                b.handler(handler);
            }
            ChannelFuture sync = b.connect("localhost",2333).sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
