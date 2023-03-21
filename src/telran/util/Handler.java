package telran.util;

public interface Handler {
	public void publish(LoggerRecord loggerRecord);
	
	default void close() {};
}
