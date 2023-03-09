package telran.io;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

public abstract class Copy {
	String srcFilePath;
	String destFilePath;
	boolean overwrite;
	
	Copy(String srcFilePath, String destFilePath, boolean overwrite) {
		this.srcFilePath = srcFilePath;
		this.destFilePath = destFilePath;
		this.overwrite = overwrite;
	}
	
	public abstract long copy();
	
	public abstract DisplayResult getDisplayResult(long copyTime, long fileSize);
	
	public void copyRun() {
		try {
			canOverwrite();
			sourceExists();
			System.out.println("Start copying");
			Stopwatch stopwatch = new Stopwatch();
			
			long fileSize = copy();
			long copyTime = stopwatch.getDelta();
			
			System.out.println("Copying succesfully finished");
			System.out.println(getDisplayResult(copyTime, fileSize).toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private void sourceExists() throws Exception {
		if (!Files.exists(Path.of(srcFilePath))) {
			throw new Exception("Incorrect source file path: " + srcFilePath);
		}
	}

	private void canOverwrite() throws Exception {
		File destFile = new File(destFilePath);
		if (!overwrite && destFile.exists()) {
			throw new Exception("File " + destFile.getName() + " can't be overwritten");
		}
	}

	protected static class Stopwatch {
		Instant start;
		
		protected Stopwatch() {
			start = Instant.now();
		}
		
		protected long getDelta() {
			Instant finish = Instant.now();
			Duration delta = Duration.between(start, finish);
			return delta.toMillis();
		}
	}
}
