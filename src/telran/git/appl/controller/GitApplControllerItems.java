package telran.git.appl.controller;

import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import telran.git.CommitMessage;
import telran.git.FileState;
import telran.git.GitRepository;
import telran.git.Status;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class GitApplControllerItems {
	private static GitRepository gitRep;
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
	
	private GitApplControllerItems() {}
	
	public static Menu getGitMenu(GitRepository gitRep) {
		GitApplControllerItems.gitRep = gitRep;
		return getMainMenu();
	}
	
	private static Menu getMainMenu() {
		return new Menu("Git application", 
				Item.of("Commit", GitApplControllerItems::commit),
				Item.of("Info", GitApplControllerItems::info),
				Item.of("Create branch", GitApplControllerItems::createBranch),
				Item.of("Rename branch", GitApplControllerItems::renameBranch),
				Item.of("Delete branch", GitApplControllerItems::deleteBranch),
				Item.of("Display branches", GitApplControllerItems::branches),
				Item.of("Display commit log", GitApplControllerItems::log),
				Item.of("Display commit content", GitApplControllerItems::commitContent),
				Item.of("Switch to branch/commit", GitApplControllerItems::switchTo),
				Item.of("Get current HEAD", GitApplControllerItems::getHead),
				Item.of("Add expression to ignore", GitApplControllerItems::addIgnoredFileNameExp),
				Item.of("Merge branch", GitApplControllerItems::merge),
				Item.of("Exit", GitApplControllerItems::save, true));
	}
	
	private static void commit(InputOutput io) { 
		String commitMessage = io.readString("Enter commit message");
		displayRes(io, gitRep.commit(commitMessage));
	}
	
	private static void info(InputOutput io) {
		List<FileState> res = gitRep.info();
		
		List<String> resToDisplay = res.stream().map(fileState -> {
			return String.format("%s - %s", fileState.filePath(), fileState.status());
		}).toList();
		
		Map<Status, Long> statuses = res.stream().collect(Collectors.groupingBy(file -> file.status(), Collectors.counting()));
		long filesToCommit = statuses.getOrDefault(Status.ADDED, 0L) + statuses.getOrDefault(Status.MODIFIED, 0L);
		String commitIsAcceptable = filesToCommit == 0 ? "There are no files to commit" 
					: String.format("There are %d files ready to commit", filesToCommit);
		
		displayRes(io, Stream.concat(resToDisplay.stream(), Stream.of(commitIsAcceptable)).toList());
	}
	
	private static void createBranch(InputOutput io) {
		String newName = io.readString("Enter new branch name");
		displayRes(io, gitRep.createBranch(newName));
	}
	
	private static void renameBranch(InputOutput io) {
		String branchName = io.readString("Enter name of branch to rename");
		String newName = io.readString("Enter new branch name");
		displayRes(io, gitRep.renameBranch(branchName, newName));
	}
	
	private static void deleteBranch(InputOutput io) {
		String branchName = io.readString("Enter name of branch to delete");
		displayRes(io, gitRep.deleteBranch(branchName));
	}
	
	private static void log(InputOutput io) {
		List<CommitMessage> res = gitRep.log();
		
		List<String> resToDisplay = res.stream()
				.map(msg -> String.format("%s %s %s", msg.commitTime().atZone(ZoneId.systemDefault()).format(dtf), msg.commitName(), msg.commitMessage()))
				.toList();
		displayRes(io, resToDisplay);
	}
	
	private static void branches(InputOutput io) {
		List<String> res = gitRep.branches();
		
		displayRes(io, res);
	}

	private static void commitContent(InputOutput io) {
		String commitName = io.readString("Enter name of commit, to display it");
		List<Path> res = gitRep.commitContent(commitName);
		
		if (res != null) {
			displayRes(io, res.stream().map(file -> file.toString()).toList());
		} else {
			displayRes(io, "There is no commit with such a name");
		}
	}
	
	
	private static void switchTo(InputOutput io) {
		String elemToSwitch = io.readString("Enter name of element, to switch");
		String res = gitRep.switchTo(elemToSwitch);
		
		displayRes(io, res);
	}

	private static void getHead(InputOutput io) {
		displayRes(io, gitRep.getHead());
	}
	
	private static void save(InputOutput io) {
		gitRep.save();

	}
	
	private static void merge(InputOutput io) {
		String branchToMerge = io.readString("Enter name of branch, to merge");
		String res = gitRep.merge(branchToMerge);
		
		displayRes(io, res);
	}
	
	private static void addIgnoredFileNameExp(InputOutput io) {
		String exp = io.readString("Enter expression to ignore it");
		String res = gitRep.addIgnoredFileNameExp(exp);
		
		displayRes(io, res);
	}

	private static void displayRes(InputOutput io, String res) {
		io.writeLine(res);
		io.readString("Enter to continue...");
	}
	
	private static void displayRes(InputOutput io, List<String> res) {
		if (res.size() == 0) {
			io.writeLine("There is nothing to display...");
		}
		for (String resLine: res) {
			io.writeLine(resLine);
		}
		io.readString("Enter to continue...");
	}
}
