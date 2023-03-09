package telran.io;

import java.io.File;

public class FilesCopyBuilder {
	public Copy build(String type, String[] args) throws Exception {
			if (args.length < 2) {
				throw new Exception("Invalid number of arguments");
			}
			
			String srcFileString = args[0];
			String destFileString = args[1];
			boolean filesOverride = getFilesOverride(args);
			
			File srcFilePath = new File(srcFileString);
			File destFilePath = new File(destFileString);
			
			if (!srcFilePath.isFile()) {
				throw new Exception("Incorrect file path");
			}
			
			if (!destFilePath.exists() || filesOverride) {
				switch (type) {
					case ("FilesCopy"): return new FilesCopy(srcFileString, destFileString, filesOverride);
					case ("TransferCopy"): return new TransferCopy(srcFileString, destFileString, filesOverride);
					case ("BufferCopy"): 
						int bufferSize = getBufferSize(args);
						return new BufferCopy(srcFileString, destFileString, filesOverride, bufferSize);
				}
			} else {
				throw new Exception("File " + destFilePath.getName() + " already exists");
			}	
		return null;
	}
	
	private int getBufferSize(String[] args) {
		if (args.length > 3) {
			if (args[3].equals("MaxBuffer")) {
				long availableMem = Runtime.getRuntime().freeMemory();
				return availableMem > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) availableMem;
			}
			return Integer.parseInt(args[3]);
		} else {
			return 10*1024*1024;
		}
	}

	private static boolean getFilesOverride(String[] args) throws Exception {
		if (args.length > 2) {
			switch (args[2].toLowerCase()) {
			case "true": return true;
			case "false": return false;
			default: throw new Exception("Override options has written incorrect");
			}
		} else {
			return false;
		}
	}
}
