package telran.net;

import java.net.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.io.*;

public class TcpServerClient implements Runnable {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Protocol protocol;
	private ExecutorService executor;
	private Instant clientStartTime;
	
	public TcpServerClient(Socket socket, Protocol protocol, ExecutorService executor) throws IOException {
		this.protocol = protocol;
		this.socket = socket;
		this.socket.setSoTimeout(5000);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
		this.executor = executor;
		clientStartTime = Instant.now();
	}
	
	@Override
	public void run() {
		boolean running = true;
		while(running && !executor.isShutdown()) {
			try {
				synchronized(executor) {
					if (stopThisThread()) {
						System.out.println("Client was disconnected by timeout");
						break;
					}
				}
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.reset();
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
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean stopThisThread() {
		return ChronoUnit.SECONDS.between(clientStartTime, Instant.now()) > 10 
				&& ((ThreadPoolExecutor) executor).getQueue().size() > 0;
	}
}
