package telran.io.application;

import telran.io.Copy;
import telran.io.FilesCopyBuilder;

public class FilesCopyApplication {
	public static void main(String[] args) {
		Copy copy;
		try {
			copy = new FilesCopyBuilder().build(CopyOperation.FilesCopy.toString(), args);
			copy.copyRun();
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
		}
	}
}
