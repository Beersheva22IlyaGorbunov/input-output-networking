package telran.net;

import java.io.*;
import java.net.*;
import telran.net.ResponseCode;

import telran.net.application.ServerLogAppl;

public class TcpClient implements NetworkClient {
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public TcpClient(String hostname, int port) throws Exception {
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
		T result = null;
		Request request = new Request(type, requestData);
		try {
			output.writeObject(request);
			Response response = (Response) input.readObject();
			if (response.code == ResponseCode.OK) {
				System.out.println("Response received succesfully");
				result = (T) response.data;
			} else {
				throw new Exception("Received response with response code: " + response.code);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

}
