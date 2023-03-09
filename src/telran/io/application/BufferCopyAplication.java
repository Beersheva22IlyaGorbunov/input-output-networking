package telran.io.application;

import telran.io.Copy;
import telran.io.FilesCopyBuilder;

public class BufferCopyAplication {

	public static void main(String[] args) {
		Copy copy;
		try {
			copy = new FilesCopyBuilder().build(CopyOperation.BufferCopy.toString(), args);
			copy.copyRun();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
