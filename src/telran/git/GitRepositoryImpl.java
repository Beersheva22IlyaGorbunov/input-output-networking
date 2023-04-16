package telran.git;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.*;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitRepositoryImpl implements GitRepository {

	private static final long serialVersionUID = 1L;
	private Headable head = null;
	private Map<String, Branch> branchMap = new HashMap<>();
	private Map<String, Commit> commitMap = new HashMap<>();
	private Set<String> ignoredRegex = new HashSet<>();
	
	static public GitRepositoryImpl init() {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(GIT_FILE))) {
			return (GitRepositoryImpl) input.readObject();
		} catch (Exception e) {
			return new GitRepositoryImpl();
		}
	}

	@Override
	public String commit(String commitMessage) {
		String res = "";
		if (head == null) {
			createBranch("master");
			res += "There are no branches yet, branch \"master\" was created\n";
		}
		Commit newCommit;
		if (head instanceof Branch) {
			try {
				newCommit = getNewCommit(commitMessage);
				if (newCommit != null) {
					res += String.format("Commited successfully. Name of new commit: %s", newCommit.getName());
					commitMap.put(newCommit.getCommitMessage().commitName(), newCommit);
					((Branch) head).setCommit(newCommit);
				} else {
					res += "There are no files to commit";
				}
			} catch (IOException e) {
				res += e.getMessage();
			}
		} else {
			res += "Can't commit current condition, while HEAD refs to commit. You should switchTo branch.";
		}
		return res;
	}

	private Commit getNewCommit(String commitMessage) throws IOException {
		List<FileState> filesState = info();
		Map<String, FileSnapshot> prevSnapshot = head.getCommit() != null ? head.getSnapshot() : null;
		Map<String, FileSnapshot> newSnapshot = new HashMap<>();
		boolean isUpdated = fillNewSnapshot(filesState, prevSnapshot, newSnapshot);
		Commit res = null;
		if (isUpdated) {
			res = new Commit(new CommitMessage(generateCommitName(), commitMessage, Instant.now()), newSnapshot, head.getCommit());
		}
		return res;
	}

	private boolean fillNewSnapshot(List<FileState> filesState,
			Map<String, FileSnapshot> prevSnapshot,
			Map<String, FileSnapshot> newSnapshot) throws IOException {
		boolean isUpdated = false;
		for (FileState fileState: filesState) {
			if (fileState.status().equals(Status.ADDED) || fileState.status().equals(Status.MODIFIED)) {
				newSnapshot.put(fileState.filePath(), getFileSnapshot(fileState));
				isUpdated = true;
			} else if (fileState.status().equals(Status.COMMITED)) {
				newSnapshot.put(fileState.filePath(), prevSnapshot.get(fileState.filePath()));
			}
		}
		return isUpdated;
	}

	private FileSnapshot getFileSnapshot(FileState file) throws IOException {
		try (BufferedReader input = new BufferedReader(new FileReader(file.filePath()))) {
			ArrayList<String> content = new ArrayList<>();
			while (true) {
				String line = input.readLine();
				if (line == null) {
					break;
				} else {
					content.add(line);
				}
			}
			return new FileSnapshot(file.filePath(), Files.getLastModifiedTime(Path.of(file.filePath())).toInstant(), content.toArray(String[]::new));
		} catch (Exception e) {
			throw new RuntimeException("Can't read a file: " + file);
		}
	}

	@Override
	public List<FileState> info() {
		Path directory = Path.of("");
		List<FileState> res = new ArrayList<>();
		try {
			Files.walk(directory, 1)
				.filter(file -> file != directory)
				.forEach(file -> res.add(new FileState(file.toString(), getFileStatus(file.toString()))));
		} catch (IOException e) {
			throw new RuntimeException("Can't read this directory");
		}
		return res;
	}

	private Status getFileStatus(String filePath) {
		Status res;
		if (checkIgnore(filePath)) {
			res = Status.IGNORED;
		} else if (head == null || head.getCommit() == null) {
			res = Status.ADDED;
		} else {
			Map<String, FileSnapshot> filesSnapshot = head.getSnapshot();
			if (filesSnapshot == null || filesSnapshot.get(filePath) == null) {
				res = Status.ADDED;
			} else {
				FileSnapshot fileSnapshot = filesSnapshot.get(filePath.toString());
				try {
					Instant lastModified = Files.getLastModifiedTime(Path.of(filePath)).toInstant();
					res = lastModified.compareTo(fileSnapshot.modifiedTime()) == 0 ? Status.COMMITED : Status.MODIFIED;
				} catch (Exception e) {
					res = Status.IGNORED;
				}
			}
		}
		return res;
	}

	private boolean checkIgnore(String filePath) {
		boolean isNotIgnored = false;
		Iterator<String> iter = ignoredRegex.iterator();
		while (iter.hasNext() && !isNotIgnored) {
			isNotIgnored = filePath.matches(iter.next());
		}
		return isNotIgnored;
	}

	@Override
	public String createBranch(String branchName) {
		String res = checkBranchName(branchName);
		if (res == null) {
			Branch newBranch = new Branch(branchName);
			if (head != null) {
				newBranch.setCommit(head.getCommit());
			}
			head = newBranch;
			branchMap.put(branchName, newBranch);
			res = String.format("Branch \"%s\" was succsefully created", branchName);
		}
		return res;
	}

	@Override
	public String renameBranch(String branchName, String newName) {
		String res;
		if (branchMap.containsKey(branchName)) {
			res = checkBranchName(newName);
			if (res == null) {
				Branch updatedBranch = branchMap.get(branchName);
				updatedBranch.setName(newName);
				res = String.format("Branch \"%s\" name was successfully changed to \"%s\"", branchName, newName);
			}
		} else {
			res = String.format("Branch \"%s\" doesn't exist", branchName);
		}
		return res;
	}
	
	private String checkBranchName(String branchName) {
		String res;
		if (branchMap.containsKey(branchName)) {
			res = String.format("Branch \"%s\" already exists", branchName);
		} else if(commitMap.containsKey(branchName)) {
			res = String.format("Commit \"%s\" already exists", branchName);
		} else {
			res = null;
		}
		return res;
	}

	@Override
	public String deleteBranch(String branchName) {
		String res;
		Branch forDelete = branchMap.get(branchName);
		if (forDelete == null) {
			res = String.format("Branch \"%s\" doesn't exist", branchName);
		} else if (head == forDelete) {
			res = String.format("Can't delete branch \"%s\" while HEAD refers to it", branchName);
		} else {
			branchMap.remove(branchName);
			res = String.format("Branch \"%s\" name was deleted successfully", branchName);
		}
		return res;
	}

	@Override
	public List<CommitMessage> log() {
		return commitMap.values().stream().map(commit -> commit.getCommitMessage()).sorted((a,b) -> b.commitTime().compareTo(a.commitTime())).toList();
	}

	@Override
	public List<String> branches() {
		return branchMap.keySet().stream().map(branch -> {
			return branch.equals(head.getName()) ? branch + "*" : branch;
		}).toList();
	}

	@Override
	public List<Path> commitContent(String commitName) {
		Commit commit = commitMap.get(commitName);
		return commit != null ? commit.getSnapshot().keySet().stream().map(filePath -> Path.of(filePath)).toList() : null;
	}

	@Override
	public String switchTo(String name) {
		String res;
		if (branchMap.containsKey(name)) {
			Branch newHead = branchMap.get(name);
			if (newHead.getCommit() != null) {
				updateFilesFromSnapshot(newHead.getSnapshot());
				deleteExtraFiles(newHead.getSnapshot(), head.getSnapshot());
			}
			head = newHead;
			res = String.format("HEAD was moved to branch \"%s\"", name);
		} else if (commitMap.containsKey(name)) {
			Commit newHead = commitMap.get(name);
			updateFilesFromSnapshot(newHead.getSnapshot());
			deleteExtraFiles(newHead.getSnapshot(), head.getSnapshot());
			head = newHead;
			res = String.format("HEAD was moved to commit \"%s\"", name);
		} else {
			res = String.format("Can't find commit or branch with name \"%s\"", name);
		}
		return res;
	}

	private void deleteExtraFiles(Map<String, FileSnapshot> newHead, Map<String, FileSnapshot> oldHead) {
		oldHead.keySet().forEach(key -> {
			if (!newHead.containsKey(key)) {
				try {
					Files.delete(Path.of(oldHead.get(key).path()));
				} catch (IOException e) {
					throw new RuntimeException(String.format("Can't delete file: %s\nReason: %s", oldHead.get(key).path(), e.getMessage()));
				}
			}
		});
	}

	private void updateFilesFromSnapshot(Map<String, FileSnapshot> snapshot) {
		snapshot.values().forEach(file -> {
			try (BufferedWriter output = new BufferedWriter(new FileWriter(file.path()))) {
				for (String line: file.content()) {
					output.write(line);
					output.newLine();
				}
				output.close();
				Files.setLastModifiedTime(Path.of(file.path()), FileTime.from(file.modifiedTime()));
			} catch (IOException e) {
				throw new RuntimeException(String.format("Can't restore file: %s\nReason: %s", file.path(), e.getMessage()));
			}
		});
	}

	@Override
	public String getHead() {
		return head == null ? null : head.getName();
	}

	@Override
	public void save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(GIT_FILE))){
			output.writeObject(this);
		} catch(Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String addIgnoredFileNameExp(String regex) {
		try {
			"example".matches(regex);
			if (ignoredRegex.add(regex)) {
				return "Entered expression added to regex";
			} else {
				return "Such expression already exists";
			}
		} catch (PatternSyntaxException e) {
			return "Entered expression is uncorrect for regex";
		}
	}
	
	protected Set<String> usedNames() {
		return Stream.concat(commitMap.keySet().stream(),branchMap.keySet().stream()).collect(Collectors.toSet());
	}
	
	private String generateName() {
		return new Random().ints(97, 123).limit(8)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}
	
	private String generateCommitName() {
		String name = generateName();
		while (commitMap.containsKey(name) || branchMap.containsKey(name)) {
			name = generateName();
		}
		return name;
	}

	@Override
	public String merge(String name) {
		String res;
		if (head instanceof Branch) {
			if (branchMap.containsKey(name)) {
				Branch branchToMerge = branchMap.get(name);
				updateFilesFromSnapshot(branchToMerge.getSnapshot());
				commit(String.format("Merge of branch \"%s\" to branch \"%s\"", head.getName(), branchToMerge.getName()));
				res = String.format("Branches \"%s\" and \"%s\" were merged successfully",  head.getName(), branchToMerge.getName());
			} else {
				res = String.format("Can't find commit or branch with name \"%s\"", name);
			}
		} else {
			res = String.format("HEAD must refs to branch");
		}
		return res;
	}

}
