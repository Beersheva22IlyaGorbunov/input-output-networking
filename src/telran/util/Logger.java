package telran.util;

import java.time.Instant;
import java.time.ZoneId;

public class Logger {
	private Level level;
	private Handler handler;
	private String name;
	
	public Logger(Handler handler, String name) {
		this.level = Level.INFO;
		this.handler = handler;
		this.name = name;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void error(String message) {
		sendToPublish(message, Level.ERROR);
	}
	
	public void warn(String message) {
		sendToPublish(message, Level.WARN);
	}
	
	public void info(String message) {
		sendToPublish(message, Level.INFO);
	}
	
	public void debug(String message) {
		sendToPublish(message, Level.DEBUG);
	}
	
	public void trace(String message) {
		sendToPublish(message, Level.TRACE);
	}
	
	private void sendToPublish(String message, Level messageLevel) {
		if (messageLevel.compareTo(level) > -1) {
			LoggerRecord newErrorRecord = new LoggerRecord(Instant.now(), ZoneId.systemDefault().toString(), messageLevel, name, message);
			handler.publish(newErrorRecord);
		}
	}
}
