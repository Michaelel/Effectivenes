package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import client.Client;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Member;
import network.Message;
import javafx.scene.control.Label;

public class LoginController implements Initializable {

	@FXML
	private PasswordField passwordTxt;
	@FXML
	private TextField loginTxt;
	@FXML
	private Button enterBtn;
	@FXML
	private Label errorLbl;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChangeListener<String> listener = (observable, oldValue, newValue) -> {
			errorLbl.setText("");
			enterBtn.setDisable(loginTxt.getText().trim().isEmpty() || passwordTxt.getText().trim().isEmpty());
		};
		loginTxt.textProperty().addListener(listener);
		passwordTxt.textProperty().addListener(listener);
	}

	@FXML
	public void authorize(ActionEvent event) throws IOException {
		Message requestMessage = new Message();
		requestMessage.getBuilder().setRequestAction("Authorization");
		JSONObject json = new JSONObject();
		json.put("login", loginTxt.getText().trim());
		json.put("password", passwordTxt.getText().trim());
		requestMessage.getBuilder().setBody(json.toString());
		Message responseMessage = Client.getNetworkConnection().sendMessage(requestMessage);
		int responseCode = responseMessage.getHeader().getResponseCode();
		if (responseCode == Message.Header.ERROR_RESPONSE_CODE) {
			errorLbl.setText(responseMessage.getBody());
		} else if (responseCode == Message.Header.OK_RESPONSE_CODE) {
			Client.replaceScene(600, 400, "/fxml/MainWindow.fxml");
			Member member = new Member();
			member.deserializeJSON(new JSONObject(responseMessage.getBody()));
			Client.setCurrentMember(member);
			Client.getPrimaryStage().setTitle(Client.getPrimaryStage().getTitle() + " - " + member.getLogin());
		}
	}

	@FXML
	public void register() throws IOException {
		Client.replaceScene(300, 320, "/fxml/RegistrationForm.fxml");
	}
}
