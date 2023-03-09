package telran.io;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DisplayResult {
	private long fileSize;
	private long copyTime;
	
	public DisplayResult(long fileSize, long copyTime) {
		this.fileSize = fileSize;
		this.copyTime = copyTime;
	}
	
	public String toString() {
		String res = String.format("File with size %s was succesfully copied for %s", convertSize(fileSize), convertCopyTime());
		return res;
	}
	
	protected String convertCopyTime() {
		Duration delta = Duration.of(copyTime, ChronoUnit.MILLIS);
		return String.format("%d:%02d:%02d.%03d", delta.toHoursPart(), delta.toMinutesPart(), delta.toSecondsPart(), delta.toMillisPart());
	}

	protected String convertSize(long size) {
		DataUnit unit = DataUnit.b;
		int unitIndex = 0;
		double sizeForUnit = size;
		while (sizeForUnit >= 1024 && unitIndex < DataUnit.values().length - 1) {
			sizeForUnit /= 1024;
			unit = DataUnit.values()[++unitIndex];
		}
		return String.format("%,.2f %s", sizeForUnit, unit);
	}
}
