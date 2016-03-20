package com.java.test.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class EchoClient {

	public void doEcho(String server, int port) throws Exception {

		AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel
				.open();
		ByteBuffer sndBuffer = ByteBuffer.wrap(new String("Hello, Server")
				.getBytes());
		ByteBuffer rcvBuffer = ByteBuffer.allocate(1024);
		clientChannel.connect(new InetSocketAddress(server, port)).get();
		if (clientChannel.write(sndBuffer).get() != sndBuffer.position()) {
			System.out.println("Send error");
			return;
		}

		// CompletionHandler implementation
		clientChannel.read(rcvBuffer, rcvBuffer,
				new CompletionHandler<Integer, ByteBuffer>() {

					public void completed(Integer result, ByteBuffer attachment) {
						System.out.println("Got " + result + " bytes: "
								+ new String(attachment.array()));
					}

					public void failed(Throwable exc, ByteBuffer attachment) {
						System.out.println("Receive error");
					}

				});
	}

	public static void main(String[] args) {

		try {
			new EchoClient().doEcho("127.0.0.1", 8080);
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
