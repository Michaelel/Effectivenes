package domain;

import java.sql.SQLException;

import org.json.JSONObject;

import network.Message;
import repository.MemberRepository;
import repository.RepositoryManager;

public class RegistrationHandler {

	public static Message register(Message requestMessage) {
		Message responseMessage = new Message();
		
		MemberRepository repo = RepositoryManager.getMemberRepository();

		JSONObject json = new JSONObject(requestMessage.getBody());
		Member member = new Member();
		member.deserializeJSON(json);
		try {
			repo.create(member);
			responseMessage.getBuilder()
			.setResponseCode(Message.Header.OK_RESPONSE_CODE);
		} catch (SQLException ex) {
			responseMessage.getBuilder()
			.setResponseCode(Message.Header.ERROR_RESPONSE_CODE)
			.setBody("Не вдалося зареєструвати користувача");			
		}
		return responseMessage;
	}

}
