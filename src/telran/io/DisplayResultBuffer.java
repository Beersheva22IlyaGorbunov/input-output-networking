package telran.io;

public class DisplayResultBuffer extends DisplayResult {
	private long bufferSize;

	public DisplayResultBuffer(long fileSize, long copyTime, long bufferSize) {
		super(fileSize, copyTime);
		this.bufferSize = bufferSize;
	}
	
	@Override
	public String toString() {
		String res = String.format("File with size %s was succesfully copied for %s, buffer size is %s", convertSize(fileSize), convertCopyTime(), convertSize(bufferSize));
		return res;
	}
}
