package telran.util.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.util.Handler;
import telran.util.Level;
import telran.util.Logger;
import telran.util.TcpClientHandler;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TcpLoggerTest {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	
	static Handler handler;
	static Logger LOG;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		handler = new TcpClientHandler();
		LOG = new Logger(handler, "Logger");
		LOG.setLevel(Level.TRACE);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
	}

	@Test
	@Order(1)
	void sendLogTest() throws Exception {
		
		LOG.trace("New trace");
		LOG.debug("New debug");
		LOG.info("New info");
		LOG.warn("New warn");
		LOG.error("New error");
		
		LOG.trace("New trace");
		LOG.warn("New warn");
		LOG.error("New error");
		
		handler.close();
		
	}
	
	@Test
	@Order(2)
	void counterTest() throws UnknownHostException, IOException {
		Socket socket = new Socket(HOSTNAME, PORT);
		PrintStream output = new PrintStream(socket.getOutputStream());
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output.println("counter#info");
		try {
			String response = input.readLine();
			assertEquals(1, Integer.parseInt(response));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		socket.close();
	}

}
