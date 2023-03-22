package telran.net.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.net.Protocol;
import telran.net.TcpClient;
import telran.net.TcpServer;

class TcpServerTest {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	enum StringOperations {
		reverse, length
	}

	private static TcpClient tcpClient;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tcpClient = new TcpClient(HOSTNAME, PORT);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		tcpClient.close();
	}
	
	@Test
	void correctRequestTest() {
		String stringToReverse = "abcdefg";
		String expectedResponse = "gfedcba";
		String receivedResponse = tcpClient.send(StringOperations.reverse.toString(), stringToReverse);
		assertEquals(expectedResponse, receivedResponse);
	}
	
	@Test
	void incorrectDataTest() {
		String receivedResponse = tcpClient.send(StringOperations.reverse.toString(), null);
		assertNull(receivedResponse);
	}
	
	@Test
	void incorretTypeTest() {
		String requestData = "abcdefg";
		String receivedResponse = tcpClient.send("uncorrect type", requestData);
		assertNull(receivedResponse);
	}

}
