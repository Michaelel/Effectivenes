package unit;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import network.ClientConnection;

public class NetworkTest {
	
	private final static String TEST_HOSTNAME = "localhost";
	private final static int TEST_PORT = 61337;
	
	@Test(expected = IllegalArgumentException.class)
	public void connectWithWrongHostname() throws IOException {
		new ClientConnection("wildcard", TEST_PORT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void connectWithWrongPort() throws IOException {
		new ClientConnection(TEST_HOSTNAME, -1);
	}

	@Test
	public void connectToTheServer() throws IOException {
		try (ClientConnection connection = new ClientConnection(TEST_HOSTNAME, TEST_PORT)) {
			
		} catch(IOException ex) {
			Assert.fail("Connection to the server was failed");
		}
	}

}
