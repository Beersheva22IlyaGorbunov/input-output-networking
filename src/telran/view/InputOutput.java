package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);
	void writeString(Object obj);
	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}
	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {
		boolean running = true;
		R res = null;
		while (running) {
			try {
				String str = readString(prompt);
				res = mapper.apply(str);
				running = false;
			} catch (Exception e) {
				writeLine(errorPrompt + " - " + e.getMessage());
			}
		}
		return res;
	}
	
	default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, s -> {
			if (!predicate.test(s)) {
				throw new RuntimeException("");
			}
			return s;
		});
	}
	
	default String readStringOptions(String prompt, String errorPrompt, Set<String> options) {
		return readStringPredicate(prompt, errorPrompt, options::contains);
	}
	
	default int readInt(String prompt, String errorPrompt) {
		return readInt(prompt, errorPrompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	default int readInt(String prompt, String errorPrompt, int min, int max) {
		return readObject(prompt, errorPrompt, s -> {
			try {
				int res = Integer.parseInt(s);
				isInRange(min, max, res);
				return res;
			} catch (NumberFormatException e) {
				throw new RuntimeException("Entered string should be a number");
			}
		});
	}
	
	default long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(prompt, errorPrompt, s -> {
			try {
				long res = Long.parseLong(s);
				isInRange(min, max, res);
				return res;
			} catch (NumberFormatException e) {
				throw new RuntimeException("Entered string should be a number");
			}
		});
	}
	
	
	
	default double readNumber(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, s -> {
			try {
				double res  = Double.parseDouble(s);
				isInRange(min, max, res);
				return res;
			} catch (NumberFormatException e) {
				throw new RuntimeException("Entered string should be a number");
			}
		});
	}
	
	default LocalDate readDateISO(String prompt, String errorPrompt) {
		return readDate(prompt, errorPrompt, "yyyy-MM-dd", LocalDate.MIN, LocalDate.MAX);
	}
	
	default LocalDate readDate(String prompt, String errorPrompt,
			String format, LocalDate min, LocalDate max) {
		return readObject(prompt, errorPrompt, s -> {
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
				LocalDate res = LocalDate.parse(s, dtf);
				isInRange(min, max, res);
				return res;
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Wrong data formatter " + format);
			} catch (DateTimeParseException e) {
				throw new RuntimeException("Entered string should be a date in format " + format);
			}			
		});
	} 
	
	private <T extends Comparable<T>> void isInRange(T min, T max, T number) {
		if (number.compareTo(max) > 0) {
			throw new RuntimeException("should be less or equal to " + max);
		}
		if (number.compareTo(min) < 0) {
			throw new RuntimeException("should be greater or equal to " + min);
		}
	}
	
}
