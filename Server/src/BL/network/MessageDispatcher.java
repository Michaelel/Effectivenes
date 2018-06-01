package network;

import java.util.Queue;

import domain.AuthorizationHandler;
import domain.InsertMessageHandler;
import domain.ReadAllChannelsHandler;
import domain.ReadAllMembersHandler;
import domain.ReadMessagesHandler;
import domain.RegistrationHandler;

public class MessageDispatcher {
	private final static String AUTHORIZATION_REQUEST_ACTION = "Authorization";
	private final static String REGISTRATION_REQUEST_ACTION = "Registration";
	private final static String READ_ALL_CHANNELS_REQUEST_ACTION = "Read all channels";
	private final static String READ_ALL_MEMBERS_REQUEST_ACTION = "Read all members";
	private final static String READ_ALL_MESSAGES_REQUEST_ACTION = "Read messages by channel";
	private final static String INSERT_MESSAGE_REQUEST_ACTION = "Insert message";
	
	private Queue<Message> outboundMessageQueue;

	public MessageDispatcher(Queue<Message> outboundMessageQueue) {
		this.outboundMessageQueue = outboundMessageQueue;
	}

	public void dispatch(Message requestMessage) {
		Message responseMessage = null;
		
		String action = requestMessage.getHeader().getRequestAction();

		switch (action) {
		case AUTHORIZATION_REQUEST_ACTION: { responseMessage = AuthorizationHandler.authorize(requestMessage); break; }
		case REGISTRATION_REQUEST_ACTION: { responseMessage = RegistrationHandler.register(requestMessage); break; }
		case READ_ALL_CHANNELS_REQUEST_ACTION: { responseMessage = ReadAllChannelsHandler.readAll(requestMessage); break; }
		case READ_ALL_MEMBERS_REQUEST_ACTION: { responseMessage = ReadAllMembersHandler.readAll(requestMessage); break; }
		case READ_ALL_MESSAGES_REQUEST_ACTION: { responseMessage = ReadMessagesHandler.readAll(requestMessage); break; }
		case INSERT_MESSAGE_REQUEST_ACTION: { responseMessage = InsertMessageHandler.insert(requestMessage); break; }
		default: System.out.println("Unrecognized action: " + action);
		}
		
		if (responseMessage != null) {
			responseMessage.setClientSocketId(requestMessage.getClientSocketId());
			outboundMessageQueue.offer(responseMessage);
		}
	}
}
