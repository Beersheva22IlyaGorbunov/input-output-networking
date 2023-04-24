package telran.net;

import java.io.*;
import java.net.*;

public class TcpClient implements NetworkClient {
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String hostname;
	private int port;

	public TcpClient(String hostname, int port) throws Exception {
		connectToSocket(hostname, port);
		this.hostname = hostname;
		this.port = port;
	}

	private void connectToSocket(String hostname, int port) throws IOException {
		socket = new Socket(hostname, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public <T> T send(String type, Serializable requestData) {
		T res = null;
		Request request = new Request(type, requestData);
		boolean serverIsAvailable = true;
		while (serverIsAvailable) {
			try {
				output.writeObject(request);
				Response response = (Response) input.readObject();
				if (response.code == ResponseCode.OK) {
					res = (T) response.data;
					break;
				} else {
					throw new RuntimeException(response.data.toString());
				}
			} catch (SocketException e) {
				serverIsAvailable = tryReconnect();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return res;
	}

	private boolean tryReconnect() {
		boolean reconnectSuccessfully = false;
		try {
			System.out.println("Connection has been lost, try to reconnect");
			socket.close();
			connectToSocket(hostname, port);
		} catch (ConnectException e1) {
			throw new RuntimeException(e1.getMessage());
		} catch (Exception e1) {
			throw new RuntimeException(e1.getMessage());
		}
		reconnectSuccessfully = socket.isConnected();
		return reconnectSuccessfully;
	}

}
