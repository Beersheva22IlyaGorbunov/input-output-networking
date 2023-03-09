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
	public long copy() throws IOException {
		Path srcFilePath = Path.of(this.srcFilePath);
		Path destFilePath = Path.of(this.destFilePath);
		if (overwrite) {
			Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING );
		} else {
			Files.copy(srcFilePath, destFilePath);
		}
		return Files.size(destFilePath);
	}

	@Override
	public DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResult(fileSize, copyTime);
	}
	
}
