package telran.io;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import telran.io.Copy.Stopwatch;

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
	
	public void copyRun() throws IOException {
		System.out.println("Start copying");
		Stopwatch stopwatch = new Stopwatch();
		
		long fileSize = copy();
		long copyTime = stopwatch.getDelta();
		
		System.out.println("Copying succesfully finished");
		System.out.println(getDisplayResult(copyTime, fileSize).toString());
	}
	
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
