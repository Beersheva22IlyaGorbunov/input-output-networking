package telran.employees;

import telran.net.Protocol;
import telran.net.UdpServer;

public class CompanyUdpServer {
	
	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception {
		Company company = new CompanyImpl();
		Protocol serverProtocol = new CompanyNetworkProtocol(company);
		UdpServer udpServer = new UdpServer(serverProtocol, PORT);
		udpServer.run();
	}
	
}
