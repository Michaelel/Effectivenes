package domain;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;

import network.Message;
import repository.MemberRepository;
import repository.RepositoryManager;

public class ReadAllMembersHandler {

	public static Message readAll(Message requestMessage) {
		Message responseMessage = new Message();

		MemberRepository repo = RepositoryManager.getMemberRepository();

		try {
			List<Member> list = repo.readAll();

			JSONArray jsonArr = new JSONArray();
			for (Member member : list) {
				jsonArr.put(member.serializeJSON());
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
