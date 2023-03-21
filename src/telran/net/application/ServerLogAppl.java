package telran.net.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import telran.util.Level;

public class ServerLogAppl {
	
	private static HashMap<String, Integer> logCounter = new HashMap<>();
	public static final int PORT = 3001;
	public static final String TYPE_COUNTER = "COUNTER";
	public static final String TYPE_LOG = "LOG";
	public static final String WRONG_TYPE = "Wrong request";
	public static final String OK = "ok";

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
		String res = WRONG_TYPE;
		String[] tokens = request.split("#");
		if (tokens.length == 2) {
			tokens[0] = tokens[0].toUpperCase();
			res = switch (tokens[0]) {
				case TYPE_LOG -> proceedLogRequest(tokens[1]);
				case TYPE_COUNTER  -> proceedCounterRequest(tokens[1]);
				default -> "Unsupported first argument";
			};
		}
		return res;
	}
	
	private static String proceedCounterRequest(String key) {
		return logCounter.getOrDefault(key.toUpperCase(), 0).toString();
	}

	private static String proceedLogRequest(String logRecord) {
		Level logLevel = Level.valueOf(logRecord.split(" ")[0].toUpperCase());
		String response = WRONG_TYPE;
		if (logLevel != null) {
			response = OK;
			logCounter.merge(logLevel.toString(), 1, Integer::sum);
		}
		return response;
	}

}
