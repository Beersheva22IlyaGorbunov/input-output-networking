package telran.net;

import java.net.*;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TcpServer implements Runnable {
	private Protocol protocol;
	private int port;
	private ServerSocket serverSocket;
	private ExecutorService executor;
	
	public TcpServer(Protocol protocol, int port) throws Exception {
		this.protocol = protocol;
		this.port = port;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(5000);
		int nThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Number thread in Threads Pool is: " + nThreads);
		executor = Executors.newFixedThreadPool(nThreads);
	}
	
	@Override
	public void run() {
		System.out.println("Server listening on port " + this.port);
		boolean isRunning = true;
		while(isRunning) {
			try {
				Socket socket = serverSocket.accept();
				TcpServerClient serverClient = new TcpServerClient(socket, protocol, executor);
				executor.execute(serverClient);
			} catch (SocketTimeoutException e) {
				if (executor.isTerminated()) {
					try {
						executor.awaitTermination(60, TimeUnit.SECONDS);
						isRunning = false;
						System.out.println(LocalDateTime.now().toString() + ": Server was shutted down");
					} catch (InterruptedException e1) {}
				}
			} catch (Exception e) {
					System.out.println(e.toString());
			}
		}
	}
	
	public void shutdown() {
		executor.shutdownNow();
	}

}
