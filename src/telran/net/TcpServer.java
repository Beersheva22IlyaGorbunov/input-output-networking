package telran.net;

import java.net.*;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
//		int nThreads = Runtime.getRuntime().availableProcessors();
		int nThreads = 2;
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
					isRunning = false;
					System.out.println(LocalDateTime.now().toString() + ": Server was shutted down");
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
