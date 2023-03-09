package telran.io.application;

import telran.io.Copy;
import telran.io.FilesCopyBuilder;

public class TransferCopyApplication {
	private final static String TRANSFER_COPY = "TransferCopy";
	
	public static void main(String[] args) {
		try {
			Copy copy = new FilesCopyBuilder().build(TRANSFER_COPY, args);
			copy.copyRun();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
