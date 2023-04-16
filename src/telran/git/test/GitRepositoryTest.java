package telran.git.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import telran.git.FileState;
import telran.git.GitRepositoryImpl;
import telran.git.Status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GitRepositoryTest {
	
	private static final String PREFIX = "gitTestFile";
	private static int fileCounter = 1;
	private static List<String> commitNames = new ArrayList<>();
	private static List<String> fileNames = new ArrayList<>();
	private static GitRepositoryImpl gitRep;

	@BeforeAll
	static void setUp() throws Exception {
		gitRep = GitRepositoryImpl.init();
		gitRep.save();
		generateFile(getContentPlaceholder());
	}
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		gitRep = GitRepositoryImpl.init();
		for (FileState fileState: gitRep.info()) {
			if (fileState.filePath().startsWith(PREFIX)) {
				Files.delete(Path.of(fileState.filePath()));
			}
		}
		try {
			Files.delete(Path.of(GitRepositoryImpl.GIT_FILE));
		} catch (Exception e) {}
	}

	@Test
	@Order(1)
	void commitTest() {
		gitRep = GitRepositoryImpl.init();
		String firstCommitRes = gitRep.commit("Initial commit");
		assertTrue(firstCommitRes.startsWith("There are no branches yet"));
		assertTrue(firstCommitRes.contains("Commited successfully. Name of new commit:"));
		commitNames.add(firstCommitRes.substring(firstCommitRes.length() - 8));
		generateFile(getContentPlaceholder());
		String secondCommitRes = gitRep.commit("Added second file");
		assertTrue(secondCommitRes.startsWith("Commited successfully. Name of new commit:"));
		commitNames.add(secondCommitRes.substring(secondCommitRes.length() - 8));
		String thirdCommitRes = gitRep.commit("Without changes commit");
		assertEquals(thirdCommitRes, "There are no files to commit");
		assertEquals(gitRep.commitContent(commitNames.get(0)).size() + 1, gitRep.commitContent(commitNames.get(1)).size());
	}
	
	@Test 
	@Order(2)
	void createBranchTest() {
		gitRep.createBranch("master-2");
		String thirdCommitRes = gitRep.commit("Without changes commit");
		assertEquals(thirdCommitRes, "There are no files to commit");
		randomlyChangeFile(fileNames.get(fileNames.size() - 1));
		thirdCommitRes = gitRep.commit("Without changes commit");
		commitNames.add(thirdCommitRes.substring(thirdCommitRes.length() - 8));
		assertTrue(thirdCommitRes.startsWith("Commited successfully. Name of new commit:"));
		assertEquals(gitRep.commitContent(commitNames.get(1)).size(), gitRep.commitContent(commitNames.get(2)).size());
		gitRep.save();
	}
	
	@Test
	@Order(3)
	void renameBranchTest() {
		GitRepositoryImpl gitRep2 = GitRepositoryImpl.init();
		String tryRenameBranch = gitRep2.renameBranch(gitRep.getHead(), commitNames.get(0));
		assertEquals(tryRenameBranch, String.format("Commit \"%s\" already exists", commitNames.get(0)));
		String oldName = gitRep2.getHead();
		tryRenameBranch = gitRep2.renameBranch(gitRep2.getHead(), "master2");
		assertEquals(String.format("Branch \"%s\" name was successfully changed to \"%s\"", oldName, gitRep2.getHead()), tryRenameBranch);
		gitRep2.save();
	}
	
	@Test
	@Order(4)
	void switchToTest() {
		gitRep.switchTo("dasdasdasd21d12_");
		assertEquals(gitRep.branches().get(0), gitRep.getHead() + "*");
		gitRep.switchTo(gitRep.branches().get(1));
		assertEquals(gitRep.branches().get(1), gitRep.getHead() + "*");
		assertEquals(gitRep.info().stream().filter(fileState -> fileState.status().equals(Status.COMMITED))
				.count(), gitRep.commitContent(commitNames.get(1)).size());
	}
	
	
	private static void generateFile(String[] content) {
		String fileName = PREFIX + getFileNumber() + ".txt";
		try (BufferedWriter output = new BufferedWriter(new FileWriter(fileName))) {
			for (String line: content) {
				output.write(line);
				output.newLine();
			}
			output.close();
			fileNames.add(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void randomlyChangeFile(String path) {
		try (BufferedWriter output = new BufferedWriter(new FileWriter(path))) {
			Random rn = new Random();
			for (int i = 0; i < rn.nextInt(100); i++) {
				String line = generateString(rn.nextInt(100));
				output.write(line);
				output.newLine();
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] getContentPlaceholder() {
		return new String[]{"File " + fileCounter + " string 0",
				"File " + fileCounter + " string 1",
				"File " + fileCounter + " string 2",};
	}
	
	private static int getFileNumber() {
		return fileCounter++;
	}
	
	private static String generateString(int limit) {
		return new Random().ints(97, 123).limit(limit)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

}
