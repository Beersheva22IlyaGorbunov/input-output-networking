package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InputOutputStreamTest {
	final static String fileName = "test.txt";
	final static String fileNameCopy = "testCopy.txt";
	final static String hello = "Hello world";

	@BeforeAll
	static void setUp() throws Exception {
		Files.deleteIfExists(Path.of(fileName));
	}

	@Order(1)
	@Test
	void OutputStreamTest() throws Exception {
		try (OutputStream output = new FileOutputStream(fileName)) {
			byte[] helloBytes = hello.getBytes();
			output.write(helloBytes);
		}
	}
	
	@Test
	void InputStreamTest() throws Exception {
		String file = fileName;
		readFileTest(file);
	}

	private void readFileTest(String file) throws IOException, FileNotFoundException {
		try (InputStream input = new FileInputStream(file)) {
			byte[] buffer = input.readAllBytes();
			String str = new String(buffer);
			assertEquals(hello, str);
		}
	}
	
	@Test
	@Order(2)
	void transferToTest() throws Exception  {
		try (InputStream input = new FileInputStream(fileName);
			 OutputStream output = new FileOutputStream(fileNameCopy);) {
			input.transferTo(output);
		}
	}
	
	@Test
	void copyTest() throws Exception {
		readFileTest(fileNameCopy);
	}

}
