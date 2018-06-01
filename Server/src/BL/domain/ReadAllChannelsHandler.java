package domain;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import network.Message;
import repository.ChannelRepository;
import repository.RepositoryManager;

public class ReadAllChannelsHandler {

	public static Message readAll(Message requestMessage) {
		Message responseMessage = new Message();
		
		ChannelRepository repo = RepositoryManager.getChannelRepository();

		try {
			List<Channel> list = repo.readAll();
			
			JSONArray jsonArr = new JSONArray();
			for (Channel channel : list) {
				jsonArr.put(channel.serializeJSON());
			}
			responseMessage.getBuilder()
			.setResponseCode(Message.Header.OK_RESPONSE_CODE)
			.setBody(jsonArr.toString());
		} catch (SQLException ex) {
			responseMessage.getBuilder()
			.setResponseCode(Message.Header.ERROR_RESPONSE_CODE)
			.setBody("На сервері сталася помилка");
		}
		return responseMessage;
	}
	
}
