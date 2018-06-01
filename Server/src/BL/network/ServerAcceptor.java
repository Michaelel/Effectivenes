package network;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

public class ServerAcceptor implements Runnable {

	private Queue<ClientSocket> clientQueue;
	private ServerSocketChannel serverSocket;
	
	public ServerAcceptor(Queue<ClientSocket> clientQueue, ServerSocketChannel serverSocket) {
		this.clientQueue = clientQueue;
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				SocketChannel clientSocket = serverSocket.accept();
				System.out.println("Client socket accepted: " + clientSocket);
				clientQueue.add(new ClientSocket(clientSocket));
			} catch (IOException ex) {
				System.out.println("Client socket adoption was failed: " + ex.getMessage());
			}
		}
	}
	
}
