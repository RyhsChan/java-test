package com.java.test.netty;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

class InboundA extends ChannelInboundHandlerAdapter {
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("InboundA.channelInactive");
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("InboundA.channelRead: "
				+ buf.toString(StandardCharsets.UTF_8));
		ctx.fireChannelRead(msg);
	}
}

class OutboundA extends ChannelOutboundHandlerAdapter {
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		System.out.println("OutboundA.close");

		// the 'top' close, will shutdown connection and trigger 'Inactive'
		// event from 'top' to 'bottom'
		ctx.close();
	}
}

class InboundB extends ChannelInboundHandlerAdapter {
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("InboundB.channelInactive");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("InboundB.channelRead: "
				+ buf.toString(StandardCharsets.UTF_8));

		// propagate close event from 'bottom' to 'top'
		ctx.channel().close();
	}
}

class OutboundB extends ChannelOutboundHandlerAdapter {
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		System.out.println("OutboundB.close");

		// propagate close event from 'current position' to 'top'
		ctx.close();
	}
}

public class Closure {

	public static void main(String[] args) throws Exception {
		ServerBootstrap b = new ServerBootstrap();
		b.group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
				.localAddress(8080)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new InboundA())
								.addLast(new OutboundA())
								.addLast(new InboundB())
								.addLast(new OutboundB());
					}

				});

		ChannelFuture future = b.bind().sync();
		future.channel().closeFuture().sync();
	}
}
