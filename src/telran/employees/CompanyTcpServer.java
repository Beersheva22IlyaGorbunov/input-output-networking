package telran.employees;

import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyTcpServer {
	
	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception {
		Company company = new CompanyImpl();
		Protocol serverProtocol = new CompanyNetworkProtocol(company);
		TcpServer tcpServer = new TcpServer(serverProtocol, PORT);
		tcpServer.run();
	}

}
