package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import client.Client;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Member;
import network.Message;
import javafx.event.ActionEvent;

public class ChannelChatController implements Initializable {

	@FXML
	private Label channelNameLbl;
	@FXML
	private TextArea chatTextArea;
	@FXML
	private TextField messageTxt;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public Label getChannelNameLabel() {
		return channelNameLbl;
	}

	public TextArea getChatTextArea() {
		return chatTextArea;
	}

	@FXML
	public void send(ActionEvent event) throws IOException {
		if (messageTxt.getText().trim().isEmpty())
			return;
		model.Message msg = new model.Message();
		msg.setBody(messageTxt.getText().trim());
		msg.setChannelId(MainWindowController.currentChannel.getId());
		msg.setMemberId(Client.getCurrentMember().getId());
		JSONObject json = msg.serializeJSON();

		Message requestMessage = new Message();
		requestMessage.getBuilder()
			.setRequestAction("Insert message")
			.setBody(json.toString());

		Message responseMessage = Client.getNetworkConnection().sendMessage(requestMessage);
		int responseCode = responseMessage.getHeader().getResponseCode();
		if (responseCode == Message.Header.OK_RESPONSE_CODE) {
			MainWindowController.update();
		}
		messageTxt.setText("");
	}

}
