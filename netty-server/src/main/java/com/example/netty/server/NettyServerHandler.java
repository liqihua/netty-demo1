package com.example.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author yyadmin
 * @date 2021/5/12
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("收到客户端消息：" + buf.toString(CharsetUtil.UTF_8) + " 客户端地址是：" + ctx.channel().remoteAddress());
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

