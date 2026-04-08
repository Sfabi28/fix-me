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
import java.util.HashMap;
import java.nio.channels.SocketChannel;

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
					// è arrivata una nuova connessione su una ServerSocketChannel
				}

				if (key.isReadable()) {
					// un client ha inviato dati
				}

				if (key.isWritable()) {
					// puoi inviare dati a un client
				}
			}
		}
	}

}
