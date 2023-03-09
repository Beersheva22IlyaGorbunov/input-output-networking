package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineOrientedStreams {
	final static String line = "Hello world";
	final static String fileNamePrintStream = "lines-stream.txt";
	final static String fileNamePrintWriter = "lines-writer.txt";

	@Test
	void printStreamTest() throws Exception {
		PrintStream printStream = new PrintStream(fileNamePrintStream);
		printStream.println(line);
	}

	@Test
	void printWriterTest() throws Exception {
		try (PrintWriter printWriter = new PrintWriter(fileNamePrintWriter);) {
			printWriter.println(line);
		}
	}
}
