package telran.util;

import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SimpleStreamHandler implements Handler {
	private PrintStream stream;
	
	@Override
	public void publish(LoggerRecord loggerRecord) {
		String time = getTimestampString(loggerRecord.timestamp, loggerRecord.zoneId);
		stream.format("%s [%5s]: %s\n", time, loggerRecord.level.toString(), loggerRecord.message);
	}
	
	private String getTimestampString(Instant time, String zoneId) {
		return time.atZone(ZoneId.of(zoneId)).format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss"));
	}

	public SimpleStreamHandler(PrintStream stream) {
		this.stream = stream;
	}

}
