package telran.employees.test;

import org.junit.jupiter.api.BeforeAll;

import telran.employees.net.CompanyNetworkProxy;
import telran.net.UdpClient;

public class CompanyNetworkUdpTest extends CompanyNetworkTest {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		company = new CompanyNetworkProxy(new UdpClient(HOSTNAME, PORT));
	}

}