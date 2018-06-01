package network;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Server implements Closeable {

	private ServerSocketChannel serverSocket;
	private Queue<ClientSocket> clientQueue = new ArrayBlockingQueue<ClientSocket>(32);
	private Thread serverAcceptorThread;
	private Thread serverDispatcherThread;

	public Server(int port) throws IOException {
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverAcceptorThread = new Thread(new ServerAcceptor(clientQueue, serverSocket), "Server Acceptor Thread");
		serverDispatcherThread = new Thread(new ServerDispatcher(clientQueue), "Server Dispatcher Thread");

		serverAcceptorThread.start();
		serverDispatcherThread.start();
	}

	@Override
	public void close() throws IOException {
		try {
			serverAcceptorThread.join();
			serverDispatcherThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serverSocket.close();
	}
	
}
