package domain;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import network.Message;
import repository.MemberRepository;
import repository.RepositoryManager;

public class AuthorizationHandler {

	public static Message authorize(Message requestMessage) {
		Message responseMessage = new Message();
		
		MemberRepository repo = RepositoryManager.getMemberRepository();

		JSONObject json = new JSONObject(requestMessage.getBody());
		Member member = null;
		try {
			member = repo.readMember(json.getString("login"), json.getString("password"));
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}

		if (member == null) {
			responseMessage.getBuilder()
			.setResponseCode(Message.Header.ERROR_RESPONSE_CODE)
			.setBody("Введений логін або пароль неправильний");			
		} else {
			responseMessage.getBuilder()
			.setResponseCode(Message.Header.OK_RESPONSE_CODE)
			.setBody(member.serializeJSON().toString());			
		}
		return responseMessage;
	}

}
