package telran.net.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerLogAppl {
	
	private static HashMap<String, Integer> logCounter = new HashMap<>();
	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server started and listening on port: " + PORT);
		while (true) {
			Socket socket = serverSocket.accept();
			try {
				runServerClient(socket);
			} catch (IOException e) {
				System.out.println("Unnormal closed connection");
			}
		}
	}

	private static void runServerClient(Socket socket) throws IOException {
		System.out.println("Client is connected");
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream output = new PrintStream(socket.getOutputStream());
		while (true) {
			String request = input.readLine();
			if (request == null) {
				break;
			}
			String response = getResponse(request);
			output.println(response);
		}
		System.out.println("Client closed connection");
	}

	private static String getResponse(String request) {
		String res = "Wrong request";
		String[] tokens = request.split("#");
		if (tokens.length == 2) {
			tokens[0] = tokens[0].toLowerCase();
			res = switch (tokens[0]) {
				case("trace") -> increaseMap(tokens[0]);
				case("debug") -> increaseMap(tokens[0]);
				case("info") -> increaseMap(tokens[0]);
				case("warn") -> increaseMap(tokens[0]);
				case("error") -> increaseMap(tokens[0]);
				case("counter") -> getCounter(tokens[1]);
				default -> "Unsupported first argument";
			};
		}
		return res;
	}
	
	private static String getCounter(String key) {
		Integer counter = logCounter.get(key);
		return counter == null ? "0" : counter.toString();
	}
	
	private static String increaseMap(String key) {
		logCounter.merge(key, 1, (oldValue, newValue) -> oldValue + newValue);
		return "OK";
	}

}
