package telran.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BufferCopy extends Copy {
	private int bufferSize;
	
	BufferCopy(String srcFilePath, String destFilePath, boolean overwrite, int bufferSize) {
		super(srcFilePath, destFilePath, overwrite);
		this.bufferSize = bufferSize;
	}

	@Override
	public long copy() throws IOException {
		File srcFilePath = new File(this.srcFilePath);
		File destFilePath = new File(this.destFilePath);
		
		long copiedBytes = 0;
		byte[] buffer = new byte[bufferSize];
		
		try (InputStream input = new FileInputStream(srcFilePath);
			 OutputStream output = new FileOutputStream(destFilePath);) {
			int copiedSize = 0;
			while ((copiedSize = input.read(buffer)) > -1) {
				copiedBytes += copiedSize;
				output.write(buffer, 0, copiedSize);
			}
		}
		return copiedBytes;
	}

	@Override
	public DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResultBuffer(fileSize, copyTime, bufferSize);
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
