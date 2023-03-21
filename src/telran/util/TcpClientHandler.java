package telran.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import telran.net.application.ServerLogAppl;
import telran.net.application.ServerTcpExampleAppl;

public class TcpClientHandler implements Handler {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	
	Socket socket; 
	PrintStream output; 
	BufferedReader input; 
	
	public TcpClientHandler() throws Exception {
		socket = new Socket(HOSTNAME, PORT);
		output = new PrintStream(socket.getOutputStream());
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	public void publish(LoggerRecord loggerRecord) {
		String time = getTimestampString(loggerRecord.timestamp, loggerRecord.zoneId);
		String logLine = String.format("%s#%s %s: %s", ServerLogAppl.TYPE_LOG,loggerRecord.level.toString(), time, loggerRecord.message);
		output.println(logLine);
		try {
			String response = input.readLine();
			if (!response.equals(ServerLogAppl.OK)) {
				throw new Exception("Received unexpected response: " + response);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private String getTimestampString(Instant time, String zoneId) {
		return time.atZone(ZoneId.of(zoneId)).format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss"));
	}
	
	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
