package telran.io.test;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class InOutOutputTest {
	private static final int LEVEL_OFFSET = 4;
	String fileName = "myFile";
	String directoryName = "myDirectory1/myDirectory2";

	@BeforeEach
	void setUp() throws Exception {
		new File(fileName).delete();
		new File(directoryName).delete();
	}

	@Test
	@Disabled
	void fileTest() throws IOException {
		File f1 = new File(fileName);
		assertTrue(f1.createNewFile());
		File dir1 = new File(directoryName);
		assertTrue(dir1.mkdirs());
		System.out.println(dir1.getAbsolutePath());
	}
	
	@Test
//	@Disabled
	void printDirectoryFileTest() throws IOException {
		printDirectoryFile("", 2);
	}

	void printDirectoryFile(String path, int maxLevel) throws IOException {
		//path - directory path
		//maxLevel - maximal level of printing, if maxLevel < 1 - prints all levels, 1 - current folder
		//output format
		// <directoryName> (not point or absolute path)
		// 		<childsName> - dir | file
		// 			<childsName> - dir | file
		// 		<childsName> - dir | file
		File directory = new File(path).getAbsoluteFile();
		if (directory.isDirectory()) {
			
			System.out.println(directory.getName());
			if (maxLevel < 1) {
				maxLevel = -1;
			}
			printChilds(directory, maxLevel, LEVEL_OFFSET);
		}
	}
	
	private void printChilds(File directory, int nextLevel, int offset) {
		if (nextLevel != -1) {
			nextLevel -= 1;
		}
		File[] currentFolder = directory.listFiles();
		for (File elem: currentFolder) {
			if (elem.isDirectory() && nextLevel != 0) {
				System.out.printf("%s%s - dir\n"," ".repeat(offset), elem.getName());
				printChilds(elem, nextLevel, offset + LEVEL_OFFSET);
			} else {
				System.out.printf("%s%s - file\n"," ".repeat(offset), elem.getName());
			}
		}
	}
	
	@Test
	@Disabled
	void filesTest() {
		Path path = Path.of(".");
		System.out.println(path.toAbsolutePath().getRoot());
	}
	
	@Test
//	@Disabled
	void printDirectoryFilesTest() throws IOException {
		printDirectoryFiles("", 2);
	}

	void printDirectoryFiles(String path, int maxLevel) throws IOException {
		//path - directory path
		//maxLevel - maximal level of printing, if maxLevel < 1 - prints all levels, 1 - current folder
		//output format
		// <directoryName> (not point or absolute path)
		// 		<childsName> - dir | file
		// 			<childsName> - dir | file
		// 		<childsName> - dir | file

		Path directory = Path.of(path);
		System.out.println(directory.toAbsolutePath().getFileName());
		if (maxLevel < 1) {
			maxLevel = Integer.MAX_VALUE;
		}
		Files.walk(directory, maxLevel)
			.filter(file -> file != directory)
			.forEach(file -> System.out.printf("%s%s - %s\n",getOffset(file), file.getFileName(), dirOrFile(file)));
	}

	private String getOffset(Path file) {
		return " ".repeat(file.getNameCount() * LEVEL_OFFSET);
	}

	private String dirOrFile(Path file) {
		return Files.isDirectory(file) ? "dir" : "file";
	}
}
