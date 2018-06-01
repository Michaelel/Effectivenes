package network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ClientSocket {
	
	private int clientSocketId;
	private SocketChannel socketChannel;
	private boolean endOfConnection = false;
	
	private List<Message> completeMessages = new ArrayList<>();
	private Message nextMessage;
	
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	
	private MessageWriter messageWriter;

	public ClientSocket(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		readBuffer = ByteBuffer.allocate(Message.Header.HEADER_SIZE + Message.MAX_BODY_SIZE);
		writeBuffer = ByteBuffer.allocate(Message.Header.HEADER_SIZE + Message.MAX_BODY_SIZE);
		messageWriter = new MessageWriter();
		nextMessage = new Message();
	}

	public void read() throws IOException {
		int bytesRead;
		do {
			bytesRead = socketChannel.read(readBuffer);
		} while (bytesRead > 0);
		if (bytesRead == -1)
			endOfConnection = true;

		readBuffer.flip();
		
		if (readBuffer.remaining() == 0) {
			readBuffer.clear();
			return;
		}

		byte[] data = new byte[readBuffer.remaining()];
		readBuffer.get(data, 0, readBuffer.remaining());

		nextMessage.read(data);
		while (nextMessage.isReady()) {
			Message message = new Message();
			
			completeMessages.add(nextMessage);
			
			for (byte[] bytes : nextMessage.getBuilder().getRestBytes()) {				
				message.read(bytes);
			}

			nextMessage = message;
		}
		readBuffer.clear();
	}

	public List<Message> getCompleteMessages() {
		return completeMessages;
	}
	
	public void write() throws IOException {
		messageWriter.write(this, writeBuffer);
	}
	
	public int getClientSocketId() {
		return clientSocketId;
	}

	public void setClientSocketId(int clientSocketId) {
		this.clientSocketId = clientSocketId;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public boolean isEndOfConnection() {
		return endOfConnection;
	}

	public MessageWriter getMessageWriter() {
		return messageWriter;
	}
}
