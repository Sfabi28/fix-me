package com.sfabi.fixme.java.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.ByteBuffer;

import com.sfabi.fixme.java.server.Client.ClientSession;

public class Server { //classe fatta singleton cosi' un unico server presente nel programma
	private static final Server INSTANCE = new Server();

    private Server() {}

    public static Server getFactory() {
        return INSTANCE;
    }
	private Selector selector;

	public void initializeSelector() {
		try {
			selector = Selector.open();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private ServerSocketChannel broker;
	private ServerSocketChannel market;

	private Map<String, SocketChannel> table = new HashMap<>();

	private String generateUniqueClientId() {
		String id;
		do {
			id = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		} while (table.containsKey(id));
		return id;
	}

	public void start() {

		initializeSelector();
		
		try {
			broker = ServerSocketChannel.open();
			broker.configureBlocking(false);
			broker.bind(new InetSocketAddress(5000));
			broker.register(selector, SelectionKey.OP_ACCEPT);
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			market = ServerSocketChannel.open();
			market.configureBlocking(false);
			market.bind(new InetSocketAddress(5001));
			market.register(selector, SelectionKey.OP_ACCEPT);
		} catch(IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();

				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					try {
						ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
						SocketChannel client = serverChannel.accept();

						if (client != null) {
							client.configureBlocking(false);
							SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

							String clientId = generateUniqueClientId();
							table.put(clientId, client);
							ClientSession session = new ClientSession(clientId, client);
							session.enqueueWrite(ByteBuffer.wrap(clientId.getBytes()));
							clientKey.attach(session);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (key.isReadable()) {
					// un client ha inviato dati
				}

				if (key.isWritable()) {
					ClientSession session = (ClientSession) key.attachment();
					if (session != null && !session.getWriteQueue().isEmpty()) {
						ByteBuffer buffer = session.getWriteQueue().peek();
						try {
							SocketChannel ch = (SocketChannel) key.channel();
							ch.write(buffer);
							if (!buffer.hasRemaining()) {
								session.getWriteQueue().poll();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
