package telran.io;

public class DisplayResultBuffer extends DisplayResult {
	private long bufferSize;

	public DisplayResultBuffer(long fileSize, long copyTime, long bufferSize) {
		super(fileSize, copyTime);
		this.bufferSize = bufferSize;
	}
	
	@Override
	public String toString() {
		String res = super.toString() + String.format(", buffer size is %s",convertSize(bufferSize));
		return res;
	}
}
