package com.java.test.nio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 *
 * @description: 
 * @author Administrator
 * @date 2016年3月15日
 *
 */
public final class EchoServer {

	private Selector selector;
	private ServerSocketChannel serverChannel;

	/**
	 * Initialize server listener
	 * 
	 * @param port
	 * @return this
	 * @throws Exception
	 */
	EchoServer listen(int port) throws Exception {
		if (serverChannel != null) {
			throw new UnsupportedOperationException(
					"No support for successive 'listen'");
		}
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(new InetSocketAddress(port));
		serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

		return this;
	}

	/**
	 * Event loop for both server & client SocketChannel, block calling thread
	 * 
	 * @throws Exception
	 */
	void loop() throws Exception {
		if (selector != null) {
			throw new UnsupportedOperationException(
					"No support for successive 'loop'");
		}

		selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("Server started, listen on "
				+ serverChannel.getLocalAddress());
		while (true) {
			selector.select();

			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				iter.remove();

				if (key.isAcceptable()) {
					processServerKey(key);
				} else if (key.isReadable()) {
					processClientKey(key);
				} else {
					throw new UnsupportedOperationException(
							"Unexpected SelectionKey ops " + key.readyOps());
				}
			}
		}
	}

	private void processClientKey(SelectionKey key) throws Exception {
		SocketChannel clientChannel = (SocketChannel) key.channel();

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		if (clientChannel.read(buffer) > 0) {
			System.out.println("Read from request "
					+ clientChannel.getRemoteAddress() + ": "
					+ new String(buffer.array()));

			// send back whatever received
			buffer.flip();
			clientChannel.write(buffer);
		}
		System.out.println("Closing connection "
				+ clientChannel.getRemoteAddress());
		clientChannel.close();
	}

	private void processServerKey(SelectionKey key) throws Exception {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

		SocketChannel clientChannel = serverChannel.accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(selector, SelectionKey.OP_READ);
		System.out.println("Comming request from "
				+ clientChannel.getRemoteAddress());
	}

	public static void main(String[] args) {
		try {
			new EchoServer().listen(8080).loop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
