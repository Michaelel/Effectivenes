package domain;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import network.Message;
import repository.MessageRepository;
import repository.RepositoryManager;

public class InsertMessageHandler {
	public static Message insert(Message requestMessage) {
		Message responseMessage = new Message();

		MessageRepository repo = RepositoryManager.getMessageRepository();

		try {
			domain.Message msg = new domain.Message(); 
			msg.deserializeJSON(new JSONObject(requestMessage.getBody()));
			repo.create(msg);
			responseMessage.getBuilder()
				.setResponseCode(Message.Header.OK_RESPONSE_CODE);
		} catch (SQLException ex) {
			ex.printStackTrace();
			responseMessage.getBuilder()
				.setResponseCode(Message.Header.ERROR_RESPONSE_CODE)
				.setBody("На сервері сталася помилка");
		}
		return responseMessage;
	}
}
