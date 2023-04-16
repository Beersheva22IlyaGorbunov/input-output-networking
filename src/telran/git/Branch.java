package telran.git;

import java.util.Map;

public class Branch implements Headable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Commit currentCommit;

	public Branch(String name) {
		this.name = name;
	}

	@Override
	public Map<String, FileSnapshot> getSnapshot() {
		return currentCommit.getSnapshot();
	}
	
	@Override
	public Commit getCommit() {
		return currentCommit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCommit(Commit newCommit) {
		this.currentCommit = newCommit;
	}

}
