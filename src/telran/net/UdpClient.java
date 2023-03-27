package telran.net;

import java.io.*;
import java.net.*;
import static telran.net.UdpUtills.*;

public class UdpClient implements NetworkClient {
	private String hostname;
	private DatagramSocket socket;
	private int port;
	
	public UdpClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		try {
			socket = new DatagramSocket();
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String type, Serializable requestData) {
		Request request = new Request(type, requestData);
		try {
			byte[] bufferSend = toBytesArray(request);
			byte[] bufferReceive = new byte[MAX_BUFFER_LENGTH];
			DatagramPacket packetToSend = new DatagramPacket(bufferSend, bufferSend.length, InetAddress.getByName(hostname), port);
			DatagramPacket packetReceived = new DatagramPacket(bufferReceive, MAX_BUFFER_LENGTH);
			socket.send(packetToSend);
			socket.receive(packetReceived);
			Response response = (Response) toSerializable(packetReceived.getData(), port);
			if (response.code != ResponseCode.OK) {
				throw new Exception(response.data.toString());
			}
			return (T) response.data;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

}
