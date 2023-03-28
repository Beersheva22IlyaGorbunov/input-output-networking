package telran.employees.net.application;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.net.CompanyNetworkProtocol;
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
