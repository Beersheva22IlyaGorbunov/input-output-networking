package telran.employees.net.application;

import java.util.Scanner;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.net.CompanyNetworkProtocol;
import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyTcpServer {
	
	private static final int PORT = 3001;
	private static final String FILE_NAME = "database.data";

	public static void main(String[] args) throws Exception {
		Company company = new CompanyImpl();
		try {
			company.restore(FILE_NAME);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
		Protocol serverProtocol = new CompanyNetworkProtocol(company);
		TcpServer tcpServer = new TcpServer(serverProtocol, PORT);
		Thread serverThread = new Thread(tcpServer);
		serverThread.start();
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while(running) {
			System.out.println("For shutdown the server enter command 'shutdown'");
			if (scanner.nextLine().equals("shutdown")) {
				tcpServer.shutdown();
				serverThread.join();
				company.save(FILE_NAME);
				running = false;
			}
		}
	}

}
