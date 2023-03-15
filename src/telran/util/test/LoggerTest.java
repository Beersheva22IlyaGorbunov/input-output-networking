package telran.util.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import telran.util.Handler;
import telran.util.Level;
import telran.util.Logger;
import telran.util.SimpleStreamHandler;

public class LoggerTest {
	@Test
	void loggerTest() throws FileNotFoundException {
		
		Handler handlerToConsole = new SimpleStreamHandler(System.out);
		Logger LOG = new Logger(handlerToConsole, "Logger");
		
		File errorLog = new File("errorLog.txt");
		Handler errorsToFileHandler = new SimpleStreamHandler(new PrintStream(errorLog));
		Logger ERROR_LOG = new Logger(errorsToFileHandler, "Error_Logger");
		ERROR_LOG.setLevel(Level.ERROR);
		
		System.out.println("***** Default logger level - INFO *****");
		LOG.trace("New trace");
		LOG.debug("New debug");
		LOG.info("New info");
		LOG.warn("New warn");
		LOG.error("New error");
		ERROR_LOG.error("New error to file");
		
		System.out.println("\n***** Changed logger level - TRACE *****");
		LOG.setLevel(Level.TRACE);
		LOG.trace("New trace");
		LOG.debug("New debug");
		LOG.info("New info");
		LOG.warn("New warn");
		LOG.error("New error");
	}
}
