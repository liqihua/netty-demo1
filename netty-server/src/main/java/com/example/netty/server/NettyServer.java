package com.example.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yyadmin
 * @date 2021/5/12
 *
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {

            bootstrap.group(bossGroup, workGroup)   //设置两个线程组
                    .channel(NioServerSocketChannel.class)  //使用 NioServerSocketChannel 作为服务器的channel实现
                    .option(ChannelOption.SO_BACKLOG, 128)  //等待socket队列大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //探测socket是否有效
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("有channel建立，hashCode：" + socketChannel.hashCode());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("服务器已就绪");

            ChannelFuture channelFuture = bootstrap.bind(9090).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isDone()) {
                        System.out.println("服务器绑定端口完成");
                    }
                    if(future.isSuccess()) {
                        System.out.println("服务器绑定端口成功");
                    } else {
                        System.out.println("服务器绑定端口失败");
                    }
                }
            });

            ChannelFuture closeFuture = channelFuture.channel().closeFuture().sync();
            closeFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isDone()) {
                        System.out.println("服务器channel关闭完成");
                    }
                    if(future.isSuccess()) {
                        System.out.println("服务器channel关闭成功");
                    } else {
                        System.out.println("服务器channel关闭失败");
                    }
                }
            });

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}

