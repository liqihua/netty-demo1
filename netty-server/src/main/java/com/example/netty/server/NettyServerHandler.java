package com.example.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yyadmin
 * @date 2021/5/12
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("收到客户端消息：" + buf.toString(CharsetUtil.UTF_8) + " 客户端地址是：" + ctx.channel().remoteAddress());
        // 把长耗时任务放进EventLoop的TaskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                System.out.println("服务端正在执行第一个异步任务");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("服务端第一个异步任务执行完成");
                ctx.writeAndFlush(Unpooled.copiedBuffer("服务端第一个异步任务执行完成", CharsetUtil.UTF_8));
            }
        });
        // 把长耗时任务放进EventLoop的TaskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                System.out.println("服务端正在执行第二个异步任务");
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("服务端第二个异步任务执行完成");
                ctx.writeAndFlush(Unpooled.copiedBuffer("服务端第二个异步任务执行完成", CharsetUtil.UTF_8));
            }
        });
        // 把定时任务放进EventLoop的ScheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            public void run() {
                System.out.println("服务端正在执行定时任务");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("服务端定时任务任务执行完成");
                ctx.writeAndFlush(Unpooled.copiedBuffer("服务端定时任务任务执行完成", CharsetUtil.UTF_8));
            }
        },15, TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel读取完成");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端发生异常");
        cause.printStackTrace();
        ctx.close();
    }
}

