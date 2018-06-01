package domain;
import java.io.IOException;

import network.Server;

public class Main {

	public static void main(String[] args) {
		int port = args.length == 0 ? 61337 : Integer.parseInt(args[0]);

		try (Server server = new Server(port)) {
			System.out.println("Server was started at port " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
