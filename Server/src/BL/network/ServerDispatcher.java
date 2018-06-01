package network;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ServerDispatcher implements Runnable {

	private Queue<ClientSocket> clientQueue;

	private int nextClientSocketId = 1;
	private Map<Integer, ClientSocket> clientSocketMap = new HashMap<>();

	private Set<ClientSocket> emptyToNonEmptySockets = new HashSet<>();
	private Set<ClientSocket> nonEmptyToEmptySockets = new HashSet<>();

	private Queue<Message> outboundMessageQueue = new LinkedList<>();
	private MessageDispatcher messageDispatcher;
	
	private Selector readSelector;
	private Selector writeSelector;
	
	public ServerDispatcher(Queue<ClientSocket> clientQueue) throws IOException {
		this.clientQueue = clientQueue;
		readSelector = Selector.open();
		writeSelector = Selector.open();
		messageDispatcher = new MessageDispatcher(outboundMessageQueue);
	}

	@Override
	public void run() {
		while (true) {
			try {
				takeNewSockets();
				readFromSockets();
				writeToSockets();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void takeNewSockets() throws IOException {
		ClientSocket clientSocket = clientQueue.poll();
		while (clientSocket != null) {
			clientSocket.setClientSocketId(nextClientSocketId++);
			clientSocket.getSocketChannel().configureBlocking(false);
			
			clientSocketMap.put(clientSocket.getClientSocketId(), clientSocket);
			
			clientSocket.getSocketChannel().register(readSelector, SelectionKey.OP_READ, clientSocket);

			clientSocket = clientQueue.poll();
		}
	}

	private void readFromSockets() throws IOException {
		int readyToRead = readSelector.selectNow();
		
		if (readyToRead <= 0)
			return;
		
		Set<SelectionKey> keys = readSelector.selectedKeys();
		Iterator<SelectionKey> keysIter = keys.iterator();
		
		while (keysIter.hasNext()) {
			SelectionKey key = keysIter.next();
			readFromSocket(key);
			keysIter.remove();
		}
		keys.clear();
	}

	private void readFromSocket(SelectionKey key) throws IOException {
		ClientSocket clientSocket = (ClientSocket)key.attachment();

		clientSocket.read();
		
		List<Message> messages = clientSocket.getCompleteMessages();
		if (!messages.isEmpty()) {
			for (Message message : messages) {
				System.out.println("Dispatching message from: " + clientSocket.getClientSocketId() + "\n" + message);
				message.setClientSocketId(clientSocket.getClientSocketId());
				messageDispatcher.dispatch(message);
			}
			messages.clear();
		}
		
		if (clientSocket.isEndOfConnection()) {
			System.out.println("Client socket closed: " + clientSocket.getClientSocketId());
			clientSocketMap.remove(clientSocket.getClientSocketId());
			key.attach(null);
			key.cancel();
			key.channel().close();
		}
	}

	private void writeToSockets() throws IOException {
		takeNewOutboundedMessages();
		cancelEmptySockets();
		registerNonEmptySockets();
		
		int readyToWrite = writeSelector.selectNow();

		if (readyToWrite <= 0)
			return;
		
		Set<SelectionKey> keys = writeSelector.selectedKeys();
		Iterator<SelectionKey> keyIter = keys.iterator();
		
		while (keyIter.hasNext()) {
			SelectionKey key = keyIter.next();
			
			ClientSocket clientSocket = (ClientSocket) key.attachment();
			clientSocket.write();
			
			if (clientSocket.getMessageWriter().isEmpty()) {
				nonEmptyToEmptySockets.add(clientSocket);
			}
			
			keyIter.remove();
		}
		keys.clear();
	}

	private void takeNewOutboundedMessages() {
		Message message = outboundMessageQueue.poll();
		
		while (message != null) {
			ClientSocket clientSocket = clientSocketMap.get(message.getClientSocketId());
			if (clientSocket != null) {
				MessageWriter messageWriter = clientSocket.getMessageWriter();
				if (messageWriter.isEmpty()) {
					nonEmptyToEmptySockets.remove(clientSocket);
					emptyToNonEmptySockets.add(clientSocket);
				}
				messageWriter.enqueue(message);
			}
			message = outboundMessageQueue.poll();
		}
	}

	private void cancelEmptySockets() {
		for (ClientSocket clientSocket : nonEmptyToEmptySockets) {
			SelectionKey key = clientSocket.getSocketChannel().keyFor(writeSelector);
			key.cancel();
		}
		nonEmptyToEmptySockets.clear();
	}

	private void registerNonEmptySockets() throws ClosedChannelException {
		for (ClientSocket clientSocket : emptyToNonEmptySockets) {
			clientSocket.getSocketChannel().register(writeSelector, SelectionKey.OP_WRITE, clientSocket);
		}
		emptyToNonEmptySockets.clear();
	}
}
