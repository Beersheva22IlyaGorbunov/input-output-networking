package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class StdInputOutputTest {
	StandardInputOutput stdInputOutput = new StandardInputOutput();
	
	@Test
	void readIntTest() {
		stdInputOutput.readInt("Please, enter integer in a range 0 - 100", 
				"Entered number is wrong ", 0, 100);
	}
	
	@Test
	void readNumberTest() {
		stdInputOutput.readNumber("Please, enter number in a range 0.0 - 100.0", 
				"Entered number is wrong ", 0.0, 100.0);
	}
	
	@Test
	void readStringPredicateTest() {
		stdInputOutput.readStringPredicate("Please, enter e-mail", 
				"Entered string is not an e-mail. E-mail have a template ___@__._", 
				Pattern.compile("^[\\w]+@[\\w]+\\.[\\w]+$").asPredicate());
	}
	
	@Test
	void readStringOptionsTest() {
		Set<String> options = new HashSet<>();
		options.add("Java");
		options.add("JS");
		options.add("Python");
		String optionsDescr = options.stream().collect(Collectors.joining(", "));
		stdInputOutput.readStringOptions("Please, enter name of option from: " + optionsDescr,
				"Entered string is differ from: " + optionsDescr, options);
	}
	
	@Test
	void readDateTest() {
		String dtfString = "dd/MM/yy";
		LocalDate minDate = LocalDate.of(2020, 1, 1);
		LocalDate maxDate = LocalDate.of(2022, 12, 31);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dtfString);
		String prompt = String.format("Please, enter date in format %s in range: %s - %s",
				dtfString, minDate.format(dtf), maxDate.format(dtf));
		stdInputOutput.readDate(prompt, "Entered string is wrong ",
				dtfString, minDate, maxDate);
	}
	
	@Test
	void readDateISOTest() {
		stdInputOutput.readDateISO("Please, enter date in format yyyy-MM-dd", 
				"Entered string is wrong ");
	}
}
