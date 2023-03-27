package telran.net.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import telran.net.NetworkClient;
import telran.net.Protocol;
import telran.net.TcpClient;
import telran.net.TcpServer;
import telran.net.UdpClient;

class TcpServerTest {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	enum StringOperations {
		reverse, length
	}

	private static NetworkClient client;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		client = new UdpClient(HOSTNAME, PORT);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		client.close();
	}
	
	@Test
	void test() {
		assertEquals("olleH", client.send(StringOperations.reverse.toString(), "Hello"));
		Integer expected = 5;
		assertEquals(expected, Integer.parseInt(client.send(StringOperations.length.toString(), "Hello")));
	}
	
	@Test
	void correctRequestTest() {
		String stringToReverse = "abcdefg";
		String expectedResponse = "gfedcba";
		String receivedResponse = client.send(StringOperations.reverse.toString(), stringToReverse);
		assertEquals(expectedResponse, receivedResponse);
	}
	
	@Test
	void incorrectDataTest() {
		String requestData = null;
		assertThrows(RuntimeException.class, () -> client.send(StringOperations.reverse.toString(), requestData));
	}
	
	@Test
	void incorretTypeTest() {
		String requestData = "abcdefg";
		assertThrows(RuntimeException.class, () -> client.send("uncorrect type", requestData));
	}

}
