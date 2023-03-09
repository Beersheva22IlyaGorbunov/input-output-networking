package telran.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import telran.io.Copy.Stopwatch;

public class TransferCopy extends Copy {

	TransferCopy(String srcFilePath, String destFilePath, boolean overwrite) {
		super(srcFilePath, destFilePath, overwrite);
	}

	@Override
	public long copy() throws IOException {
		File srcFilePath = new File(this.srcFilePath);
		File destFilePath = new File(this.destFilePath);
		
		long copiedBytes = 0;
		
		try (InputStream input = new FileInputStream(srcFilePath);
			 OutputStream output = new FileOutputStream(destFilePath);) {
			copiedBytes = input.transferTo(output);
		}
		return copiedBytes;
	}

	@Override
	public DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResult(fileSize, copyTime);
	}

	@Override
	public void copyRun() throws IOException {
		System.out.println("Starting copying");
		Stopwatch stopwatch = new Stopwatch();
		
		long fileSize = copy();
		long copyTime = stopwatch.getDelta();
		
		System.out.println("Copying succesfully finished");
		System.out.println(getDisplayResult(copyTime, fileSize).toString());
	}

}
