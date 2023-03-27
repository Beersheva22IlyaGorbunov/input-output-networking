package telran.net.application;

import telran.net.Protocol;
import telran.net.UdpServer;
import telran.net.test.ExampleProtocol;

public class ServerUdpExamplAppl {
	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception  {
		Protocol serverProtocol = new ExampleProtocol();
		UdpServer server = new UdpServer(serverProtocol, PORT);
		server.run();
	}

}
