package telran.net.application;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import telran.net.Request;
import telran.net.TcpClient;

public class ClientTcpExampleAppl {
	
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception {
		TcpClient tcpClient = new TcpClient(HOSTNAME, PORT);
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Enter request type or exit: ");
			String type = scanner.nextLine();
			if (type.equals("exit")) {
				break;
			}
			System.out.print("Enter request string: ");
			String data = scanner.nextLine();
			String response = tcpClient.send(type, data);
			System.out.println("Received response: " + response);
			if (response == null) {
				break;
			}
		}
		tcpClient.close();
	}
}
