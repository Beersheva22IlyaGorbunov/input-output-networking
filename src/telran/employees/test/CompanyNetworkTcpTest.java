package telran.employees.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import telran.net.TcpClient;

public class CompanyNetworkTcpTest extends CompanyNetworkTest {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		client = new TcpClient(HOSTNAME, PORT);
	}
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		client.close();
	}

}
