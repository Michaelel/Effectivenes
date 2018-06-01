package network;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.json.JSONObject;

public class ClientConnection implements Closeable {

	private SocketChannel socket;
	
	public ClientConnection(String hostname, int port) throws IOException
	{
		InetSocketAddress inetAddress = new InetSocketAddress(hostname, port);
		socket = SocketChannel.open();
		socket.connect(inetAddress);
	}

	public Message sendMessage(Message requestMessage) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(Message.Header.HEADER_SIZE + Message.MAX_BODY_SIZE);
		requestMessage.write(buffer);
		buffer.flip();
		socket.write(buffer);
		buffer.clear();
		Message responseMessage = new Message();
		socket.read(buffer);
		buffer.flip();
		int length = buffer.remaining();
		byte[] data = new byte[length];
		buffer.get(data, 0, length);
		responseMessage.getBuilder().attach(data);
		return responseMessage;
	}
	
	@Override
	public void close() throws IOException {
		socket.close();
	}

	public SocketChannel getSocket() {
		return socket;
	}
	
}
