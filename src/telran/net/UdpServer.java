package telran.net;

import java.net.*;
import static telran.net.UdpUtills.*;

public class UdpServer implements Runnable {
	private DatagramSocket socket;
	private int port;
	private Protocol protocol;
	
	public UdpServer(Protocol protocol, int port) throws Exception {
		this.port = port;
		this.protocol = protocol;
		socket = new DatagramSocket(port);
	}
	
	@Override
	public void run() {
		System.out.println("UDP Server running on port: " + port);
		try {
			byte[] bufferReceive = new byte[MAX_BUFFER_LENGTH];
			byte[] bufferSend = null;
			while (true) {
				DatagramPacket packetReceived = new DatagramPacket(bufferReceive, MAX_BUFFER_LENGTH);
				socket.receive(packetReceived);
				Request request = (Request) toSerializable(packetReceived.getData(), packetReceived.getLength());
				Response response = protocol.getResponse(request);
				bufferSend = toBytesArray(response);
				DatagramPacket packetToSend = new DatagramPacket(bufferSend, bufferSend.length, packetReceived.getAddress(), packetReceived.getPort());
				socket.send(packetToSend);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}

	}

}
