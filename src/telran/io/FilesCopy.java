package telran.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FilesCopy extends Copy{

	FilesCopy(String srcFilePath, String destFilePath, boolean overwrite) {
		super(srcFilePath, destFilePath, overwrite);
	}

	@Override
	public long copy() {
		Path srcFilePath = Path.of(this.srcFilePath);
		Path destFilePath = Path.of(this.destFilePath);
		long res = 0;
		try {
			Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING );
			res = Files.size(destFilePath);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return res;
	}

	@Override
	public DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResult(fileSize, copyTime);
	}
	
}
