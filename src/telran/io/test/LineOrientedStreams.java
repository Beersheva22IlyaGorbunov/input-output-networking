package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class LineOrientedStreams {
	final static String line = "Hello world";
	final static String helloFileName = "test.txt";
	final static String fileNamePrintStream = "lines-stream.txt";
	final static String fileNamePrintWriter = "lines-writer.txt";
	final static int N_RECORDS = 10000;

	@Test
	@Disabled
	void printStreamTest() throws Exception {
		PrintStream printStream = new PrintStream(fileNamePrintStream);
		IntStream.range(0, N_RECORDS).forEach(printStream::println);
	}

	@Test
	@Disabled
	void printWriterTest() throws Exception {
		try (PrintWriter printWriter = new PrintWriter(fileNamePrintWriter);) {
			IntStream.range(0, N_RECORDS).forEach(printWriter::println);
		}
	}
	
	@Test
	void bufferedReaderTest() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(helloFileName));
		while(true) {
			String nextLine = reader.readLine();
			if (nextLine == null) {
				break;
			}
			assertEquals(line, nextLine);
		}
	}
}
