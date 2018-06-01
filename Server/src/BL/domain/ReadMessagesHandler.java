package domain;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;

import network.Message;
import repository.MessageRepository;
import repository.RepositoryManager;

public class ReadMessagesHandler {
	public static Message readAll(Message requestMessage) {
		Message responseMessage = new Message();

		MessageRepository repo = RepositoryManager.getMessageRepository();

		try {
			int id = Integer.parseInt(requestMessage.getBody());
			List<domain.Message> list = repo.readAllByChannelId(id);

			JSONArray jsonArr = new JSONArray();
			for (domain.Message message: list) {
				jsonArr.put(message.serializeJSON());
			}
			responseMessage.getBuilder()
				.setResponseCode(Message.Header.OK_RESPONSE_CODE)
				.setBody(jsonArr.toString());
		} catch (SQLException ex) {
			ex.printStackTrace();
			responseMessage.getBuilder()
				.setResponseCode(Message.Header.ERROR_RESPONSE_CODE)
				.setBody("На сервері сталася помилка");
		}
		return responseMessage;
	}
}
