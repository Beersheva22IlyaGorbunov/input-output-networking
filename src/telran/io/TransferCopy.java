package telran.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferCopy extends Copy {

	TransferCopy(String srcFilePath, String destFilePath, boolean overwrite) {
		super(srcFilePath, destFilePath, overwrite);
	}

	@Override
	public long copy() {
		File srcFilePath = new File(this.srcFilePath);
		File destFilePath = new File(this.destFilePath);
		
		long copiedBytes = 0;
		
		try (InputStream input = new FileInputStream(srcFilePath);
			 OutputStream output = new FileOutputStream(destFilePath);) {
			copiedBytes = input.transferTo(output);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return copiedBytes;
	}

	@Override
	public DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResult(fileSize, copyTime);
	}
}
