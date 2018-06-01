package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Channel;
import model.Member;
import network.Message;

public class MainWindowController implements Initializable {

	public static Channel currentChannel;
	private static ChannelChatController controller;
	
	@FXML
	private ListView<Member> memberLst;
	@FXML
	private ListView<Channel> channelLst;
	@FXML
	private Pane chatAreaPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Client.mainWindowController = this;
		try {
			memberLst.setItems(readAllMembers());
			channelLst.setItems(readAllChannels());
		} catch (IOException e) {
			e.printStackTrace();
		}
		channelLst.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2) {
					Channel channel = channelLst.getSelectionModel().getSelectedItem();
					try {
						if (channel != null) {
							currentChannel = channel;							
							openChannelChat(channel);
							MainWindowController.update();
							updateChannelChat(channel);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		memberLst.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2) {
					Member member = memberLst.getSelectionModel().getSelectedItem();
					try {
						if (member != null) {
							openMemberForm(member);
							MainWindowController.update();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public static void update() throws IOException {
		try {
			Client.mainWindowController.memberLst.getItems().removeAll(Client.mainWindowController.memberLst.getItems());
			Client.mainWindowController.channelLst.getItems().removeAll(Client.mainWindowController.channelLst.getItems());
			Client.mainWindowController.memberLst.setItems(Client.mainWindowController.readAllMembers());
			Client.mainWindowController.channelLst.setItems(Client.mainWindowController.readAllChannels());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Client.mainWindowController.updateChannelChat(currentChannel);
	}

	private void openMemberForm(Member member) {
	}
	
	private void updateChannelChat(Channel channel) throws IOException {
		if (currentChannel == null)
			return;

		ObservableList<model.Message> messages = readMessagesByChannel(channel.getId());
		controller.getChannelNameLabel().setText(channel.getName());
		
		StringBuilder chatText = new StringBuilder("");
		
		Map<Integer, Member> memberMap = new HashMap<>();
		for (Member member : memberLst.getItems()) {
			memberMap.put(member.getId(), member);
		}
		
		for (model.Message msg : messages) {
			chatText.append(memberMap.get(msg.getMemberId()));
			chatText.append(": ");
			chatText.append(msg.getBody() + "\n");
		}		
		
		controller.getChatTextArea().setText(chatText.toString());
	}

	private void openChannelChat(Channel channel) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/ChannelChat.fxml"));
		Parent chatRoot = loader.load();
		controller = loader.<ChannelChatController>getController();

		if (!chatAreaPane.getChildren().isEmpty())
			chatAreaPane.getChildren().remove(0);
		chatAreaPane.getChildren().add(chatRoot);
	}

	private ObservableList<model.Message> readMessagesByChannel(int id) throws IOException {
		Message requestMessage = new Message();
		requestMessage.getBuilder().setRequestAction("Read messages by channel").setBody(String.valueOf(id));
		Message responseMessage = Client.getNetworkConnection().sendMessage(requestMessage);
		JSONArray messagesArr = new JSONArray(responseMessage.getBody());
		List<model.Message> messageList = new ArrayList<>();
		for (int i = 0; i < messagesArr.length(); ++i) {
			JSONObject messageJson = (JSONObject) messagesArr.get(i);
			model.Message message = new model.Message();
			message.deserializeJSON(messageJson);
			messageList.add(message);
		}
		return FXCollections.observableArrayList(messageList);
	}

	private ObservableList<Channel> readAllChannels() throws IOException {
		Message requestMessage = new Message();
		requestMessage.getBuilder().setRequestAction("Read all channels");
		Message responseMessage = Client.getNetworkConnection().sendMessage(requestMessage);
		JSONArray channelsArr = new JSONArray(responseMessage.getBody());
		List<Channel> channelList = new ArrayList<>();
		for (int i = 0; i < channelsArr.length(); ++i) {
			JSONObject channelJson = (JSONObject) channelsArr.get(i);
			Channel channel = new Channel();
			channel.deserializeJSON(channelJson);
			channelList.add(channel);
		}
		return FXCollections.observableArrayList(channelList);
	}

	private ObservableList<Member> readAllMembers() throws IOException {
		Message requestMessage = new Message();
		requestMessage.getBuilder().setRequestAction("Read all members");
		Message responseMessage = Client.getNetworkConnection().sendMessage(requestMessage);
		JSONArray membersArr = new JSONArray(responseMessage.getBody());
		List<Member> memberList = new ArrayList<>();
		for (int i = 0; i < membersArr.length(); ++i) {
			JSONObject memberlJson = (JSONObject) membersArr.get(i);
			Member member = new Member();
			member.deserializeJSON(memberlJson);
			memberList.add(member);
		}
		return FXCollections.observableArrayList(memberList);
	}

}
