package com.example.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yyadmin
 * @date 2021/5/12
 *
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",9090);
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isDone()) {
                        System.out.println("客户端绑定端口完成");
                    }
                    if(future.isSuccess()) {
                        System.out.println("客户端绑定端口成功");
                    } else {
                        System.out.println("客户端绑定端口失败");
                    }
                }
            });

            ChannelFuture closeFuture = channelFuture.channel().closeFuture().sync();
            closeFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isDone()) {
                        System.out.println("客户端channel关闭完成");
                    }
                    if(future.isSuccess()) {
                        System.out.println("客户端channel关闭成功");
                    } else {
                        System.out.println("客户端channel关闭失败");
                    }
                }
            });

        } finally {
            group.shutdownGracefully();
        }
    }

}

