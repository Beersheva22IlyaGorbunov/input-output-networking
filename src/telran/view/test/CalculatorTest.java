package telran.view.test;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CalculatorTest {
	Item exit = Item.exit();
	
	@Test
	void calculatorBuild() {
		InputOutput io = new StandardInputOutput();
		Menu mainMenu = new Menu("Calculator", getArithmecticMenu(), getDateMenu(), exit);
		mainMenu.perform(io);
	}
	
	private Item getArithmecticMenu(){
		return new Menu("Ariphmetic calculator", 
			Item.of("Add numbers", io -> arithmeticCalc.accept(io, (a, b) -> a + b)),
			Item.of("Subtract numbers", io -> arithmeticCalc.accept(io, (a, b) -> a - b)),
			Item.of("Multiply numbers", io -> arithmeticCalc.accept(io, (a, b) -> a * b)),
			Item.of("Divide numbers",  io -> 
					arithmeticCalc.accept(io, (a, b) -> {
						if (b == 0) {
							throw new RuntimeException("Can't divide to 0");
						}
						return a / b;
					})),
			exit
		);
	}
	
	private BiConsumer<InputOutput, BinaryOperator<Double>> arithmeticCalc = (io, func) -> {
		double a = io.readNumber("Enter first number", "Entered number is wrong", -Double.MAX_VALUE, Double.MAX_VALUE);
		double b = io.readNumber("Enter second number", "Entered number is wrong", -Double.MAX_VALUE, Double.MAX_VALUE);
		String res;
		try {
			res = String.format("%.2f", func.apply(a, b));
		} catch (Exception e) {
			res = e.getMessage();
		}
		printResult(io, res);
	};		
	
	private void printResult(InputOutput io, String res) {
		io.writeLine("_".repeat(Menu.STARS_AMOUNT));
		io.writeLine("");
		io.writeLine(String.format("Result = %s", res));
		io.writeLine("_".repeat(Menu.STARS_AMOUNT));
		io.readString("Press enter to continue...");
	}
	
	private Item getDateMenu(){
		return new Menu("Date calculator", 
			Item.of("Add dates", 
					io -> dateCalc.accept(io, (date, days) -> date.plusDays(days))), 
			Item.of("Subtract dates", 
					io -> dateCalc.accept(io, (date, days) -> date.minusDays(days))),
			exit
		);
	}
	
	private BiConsumer<InputOutput, BiFunction<LocalDate, Integer, LocalDate>> dateCalc = (io, func) -> {
		LocalDate date = io.readDateISO("Enter date in format yyyy-MM-dd", "Entered string is wrong");
		int days = io.readInt("Enter number of days", "Entered number is wrong", 0, Integer.MAX_VALUE);
		String res;
		try {
			res = func.apply(date, days).toString();
		} catch (Exception e) {
			res = e.getMessage();
		}
		printResult(io, res);
	};

}
