package client;

import java.io.IOException;

import gui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Member;
import network.ClientConnection;

public class Client extends Application {

	private static Member currentMember;
	private static ClientConnection connection;
	private static Stage primaryStage;
	
	public static MainWindowController mainWindowController; 
	
	public static ClientConnection getNetworkConnection() {
		return connection;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Client.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginForm.fxml"));

		Scene scene = new Scene(root, 300, 320);
		primaryStage.setTitle("Effectiveness messanger");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// establish connection with a server
		String hostname = args.length > 0 ? args[0] : "localhost"; // change server hostname
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 61337;
		try {
			connection = new ClientConnection(hostname, port);
			System.out.println("Connection with " + hostname + ":" + port + " was established");
		} catch (IOException ex) {
			System.out.println("Client connection failed: " + ex.getMessage());
			System.exit(1);
		}

		// launch GUI
		launch(args);
	}

	public static Parent replaceScene(int width, int height, String fxml) throws IOException {
        Parent page = (Parent) FXMLLoader.load(Client.class.getResource(fxml));
        Scene scene = Client.getPrimaryStage().getScene();
        if (scene == null) {
            scene = new Scene(page, width, height);
            Client.getPrimaryStage().setScene(scene);
        } else {
        	Client.getPrimaryStage().getScene().setRoot(page);
        }
        Client.getPrimaryStage().sizeToScene();
        Client.getPrimaryStage().setMinWidth(width);
        Client.getPrimaryStage().setMaxWidth(width);
        Client.getPrimaryStage().setWidth(width);
        Client.getPrimaryStage().setMinHeight(height);
        Client.getPrimaryStage().setMaxHeight(height);
        Client.getPrimaryStage().setHeight(height);
        Client.getPrimaryStage().centerOnScreen();
        return page;
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static Member getCurrentMember() {
		return currentMember;
	}

	public static void setCurrentMember(Member currentMember) {
		Client.currentMember = currentMember;
	}
	
}
