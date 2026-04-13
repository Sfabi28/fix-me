package com.sfabi.fixme.java.server.Client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

public class ClientSession {
	private final String id;
	private final SocketChannel channel;
	private final Queue<ByteBuffer> writeQueue;

	public ClientSession(String id, SocketChannel channel) {
        this.id = id;
        this.channel = channel;
        this.writeQueue = new LinkedList<>();
    }

	public void enqueueWrite(ByteBuffer buffer) {
        writeQueue.add(buffer);
    }

	public Queue<ByteBuffer> getWriteQueue() {
        return writeQueue;
    }

    public String getId() {
        return id;
    }

	public SocketChannel getChannel() {
        return channel;
    }
}
