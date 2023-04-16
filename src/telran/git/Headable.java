package telran.git;

import java.io.Serializable;
import java.util.Map;

public interface Headable extends Serializable{
	Map<String, FileSnapshot> getSnapshot();
	String getName();
	Commit getCommit();
}
