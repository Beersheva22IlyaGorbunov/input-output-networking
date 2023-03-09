package telran.io.application;

import telran.io.Copy;
import telran.io.FilesCopyBuilder;

public class BufferCopyAplication {
	private final static String BUFFER_COPY = "BufferCopy";

	public static void main(String[] args) {
		try {
			Copy copy = new FilesCopyBuilder().build(BUFFER_COPY, args);
			copy.copyRun();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
