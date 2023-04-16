package telran.git;

import java.util.HashMap;
import java.util.Map;

public class Commit implements Headable {
	private static final long serialVersionUID = 1L;
	private CommitMessage commitMessage;
	private Map<String, FileSnapshot> snapshot = new HashMap<>();
	private Commit prev;
	
	public Commit(CommitMessage commitMessage, Map<String, FileSnapshot> newSnapshot, Commit prev) {
		this.commitMessage = commitMessage;
		this.snapshot = newSnapshot;
		this.prev = prev;
	}

	@Override
	public Map<String, FileSnapshot> getSnapshot() {
		return snapshot;
	}

	public CommitMessage getCommitMessage() {
		return commitMessage;
	}

	public void setCommitMessage(CommitMessage commitMessage) {
		this.commitMessage = commitMessage;
	}

	@Override
	public String getName() {
		return commitMessage.commitName();
	}

	@Override
	public Commit getCommit() {
		return this;
	}

	public Commit getPrev() {
		return prev;
	}

	public void setPrev(Commit prev) {
		this.prev = prev;
	}

}
