package telran.net;

import java.net.*;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.io.*;

public class TcpServerClient implements Runnable {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Protocol protocol;
	private ExecutorService executor;
	
	public TcpServerClient(Socket socket, Protocol protocol, ExecutorService executor) throws IOException {
		this.protocol = protocol;
		this.socket = socket;
		this.socket.setSoTimeout(5000);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
		this.executor = executor;
	}
	
	@Override
	public void run() {
		boolean running = true;
		while(running) {
			try {
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.writeObject(response);
			} catch (SocketTimeoutException e){
				if (executor.isShutdown()) {
					System.out.println(LocalDateTime.now().toString() + ": Client is disconnected because the server is shutting down");
					running = false;
				}
			} catch (EOFException e) {
				System.out.println("Client closed connection");
				running = false;
			} catch (Exception e) {
				throw new RuntimeException(e.toString());
			}
		}
	}
}
