package telran.net.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

import telran.net.Protocol;
import telran.net.TcpServer;
import telran.net.test.ExampleProtocol;

public class ServerTcpExampleAppl {

	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception  {
		Protocol serverProtocol = new ExampleProtocol();
		TcpServer tcpServer = new TcpServer(serverProtocol, PORT);
		tcpServer.run();
	}
	
}
