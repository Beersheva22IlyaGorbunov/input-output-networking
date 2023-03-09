package telran.io;


public class FilesCopyBuilder {
	private final static String FILES_COPY = "FilesCopy";
	private final static String TRANSFER_COPY = "TransferCopy";
	private final static String BUFFER_COPY = "BufferCopy";
	
	
	public Copy build(String type, String[] args) throws Exception {
			if (args.length < 2) {
				throw new Exception("Invalid number of arguments");
			}
			
			String srcFileString = args[0];
			String destFileString = args[1];
			boolean filesOverride = getFilesOverride(args);
			
			switch (type) {
				case (FILES_COPY): return new FilesCopy(srcFileString, destFileString, filesOverride);
				case (TRANSFER_COPY): return new TransferCopy(srcFileString, destFileString, filesOverride);
				case (BUFFER_COPY): return new BufferCopy(srcFileString, destFileString, filesOverride, getBufferSize(args));
				default: throw new IllegalArgumentException(type + " is incorect for copying type");
			}
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
