package telran.io;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Copy {
	String srcFilePath;
	String destFilePath;
	boolean overwrite;
	
	Copy(String srcFilePath, String destFilePath, boolean overwrite) {
		this.srcFilePath = srcFilePath;
		this.destFilePath = destFilePath;
		this.overwrite = overwrite;
	}
	
	public abstract long copy() throws IOException;
	
	public abstract DisplayResult getDisplayResult(long copyTime, long fileSize);
	
	public abstract void copyRun() throws IOException;
	
	protected static class Stopwatch {
		LocalDateTime start;
		
		protected Stopwatch() {
			start = LocalDateTime.now();
		}
		
		protected long getDelta() {
			LocalDateTime finish = LocalDateTime.now();
			Duration delta = Duration.between(start, finish);
			return delta.toMillis();
		}
	}
}
