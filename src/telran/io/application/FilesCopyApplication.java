package telran.io.application;

import telran.io.Copy;
import telran.io.FilesCopyBuilder;

public class FilesCopyApplication {
	private final static String FILES_COPY = "FilesCopy";
	
	public static void main(String[] args) {
		try {
			Copy copy = new FilesCopyBuilder().build(FILES_COPY, args);
			copy.copyRun();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
