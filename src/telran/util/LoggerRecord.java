package telran.util;

import java.time.Instant;

public class LoggerRecord {
	final public Instant timestamp;
	final public String zoneId;
	final public Level level;
	final public String loggerName;
	final public String message;
	
	public LoggerRecord(Instant timestamp, String zoneId, Level level, String loggerName, String message) {
		this.timestamp = timestamp;
		this.zoneId = zoneId;
		this.level = level;
		this.loggerName = loggerName;
		this.message = message;
	}
}
