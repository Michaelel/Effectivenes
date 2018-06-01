package network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MessageWriter {
	private List<Message> writeQueue = new ArrayList<>();
	private Message messageInProgress = null;
	private int bytesWritten = 0;

	public void enqueue(Message message) {
		if (messageInProgress == null) {
			messageInProgress = message;
		} else {
			writeQueue.add(message);
		}
	}
	
	public boolean isEmpty() {
		return writeQueue.isEmpty() && messageInProgress == null;
	}

	public void write(ClientSocket clientSocket, ByteBuffer writeBuffer) throws IOException {
		messageInProgress.write(writeBuffer);
		writeBuffer.flip();
		
		int bytesWritten = clientSocket.getSocketChannel().write(writeBuffer);
		int totalBytesWritten = bytesWritten;
		while (bytesWritten > 0 && writeBuffer.hasRemaining()) {
			bytesWritten = clientSocket.getSocketChannel().write(writeBuffer);
			totalBytesWritten += bytesWritten;
		}
		writeBuffer.clear();
		
		this.bytesWritten += totalBytesWritten;
		if (this.bytesWritten >= messageInProgress.length()) {
			if (!writeQueue.isEmpty()) {
				messageInProgress = writeQueue.remove(0);
			} else {
				messageInProgress = null;
			}
		}
	}
}
