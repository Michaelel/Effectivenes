package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import network.Message;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class RegistrationController implements Initializable {

	@FXML
	private TextField nameTxt;
	@FXML
	private TextField surnameTxt;
	@FXML
	private TextField secondnameTxt;
	@FXML
	private TextField emailTxt;
	@FXML
	private TextField loginTxt;
	@FXML
	private TextField passwordTxt;
	@FXML
	private Button signupBtn;
	@FXML
	private Label errorLbl;

	@FXML
	public void back() throws IOException {
		Client.replaceScene(300, 320, "/fxml/LoginForm.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChangeListener<String> listener = (observable, oldValue, newValue) -> {
			errorLbl.setText("");
			boolean disableBtn = loginTxt.getText().trim().isEmpty() || passwordTxt.getText().trim().isEmpty()
					|| nameTxt.getText().trim().isEmpty() || surnameTxt.getText().trim().isEmpty()
					|| secondnameTxt.getText().trim().isEmpty() || emailTxt.getText().trim().isEmpty();
			signupBtn.setDisable(disableBtn);
		};
		nameTxt.textProperty().addListener(listener);
		surnameTxt.textProperty().addListener(listener);
		secondnameTxt.textProperty().addListener(listener);
		emailTxt.textProperty().addListener(listener);
		loginTxt.textProperty().addListener(listener);
		passwordTxt.textProperty().addListener(listener);
	}

	@FXML
	public void signup(ActionEvent event) throws IOException {
		Message requestMessage = new Message();
		requestMessage.getBuilder().setRequestAction("Registration");
		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("name", nameTxt.getText().trim());
		json.put("surname", surnameTxt.getText().trim());
		json.put("secondname", secondnameTxt.getText().trim());
		json.put("email", emailTxt.getText().trim());
		json.put("login", loginTxt.getText().trim());
		json.put("password", passwordTxt.getText().trim());
		requestMessage.getBuilder().setBody(json.toString());
		Message responseMessage = Client.getNetworkConnection().sendMessage(requestMessage);
		int responseCode = responseMessage.getHeader().getResponseCode();
		if (responseCode == Message.Header.ERROR_RESPONSE_CODE) {
			errorLbl.setText(responseMessage.getBody());
		} else if (responseCode == Message.Header.OK_RESPONSE_CODE) {
			Client.replaceScene(300, 320, "/fxml/LoginForm.fxml");
		}
	}

}
