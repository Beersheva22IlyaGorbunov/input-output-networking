package telran.employees.application.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import telran.employees.Company;
import telran.employees.Employee;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class CompanyControllerItems {
	
	private static Company company;
	private static Set<String> departments;
	
	private CompanyControllerItems() {}
	
	public static Menu getCompanyMenu(Company company, Set<String> departments, Consumer<InputOutput> exitFunc) {
		CompanyControllerItems.company = company;
		CompanyControllerItems.departments = departments;
		return getMainMenu(exitFunc);
	}
	
	private static Menu getMainMenu(Consumer<InputOutput> exitFunc) {
		return new Menu("Employees application", 
				adminItemMenu(),
				userItemMenu(),
				Item.of("Exit with save", exitFunc, true),
				Item.exit());
	}
	
	private static Item userItemMenu() {
		return new Menu("User actions", 
				Item.of("Get all employees", CompanyControllerItems::getAllEmployees),
				Item.of("Get employee by Id", CompanyControllerItems::getEmployeeById),
				Item.of("Get employees by bithday month", CompanyControllerItems::getEmployeeByMonth),
				Item.of("Get employees by deparment", CompanyControllerItems::getEmployeeByDepart),
				Item.of("Get employees by salary", CompanyControllerItems::getEmployeeBySalary),
				Item.exit());
	}

	private static void getEmployeeBySalary(InputOutput io) {
		int salaryFrom = io.readInt("Enter minimum salary", "Entered wrong salary", 0, Integer.MAX_VALUE);
		int salaryTo = io.readInt("Enter maximum salary", "Entered wrong salary", salaryFrom, Integer.MAX_VALUE);
		List<Employee> employees = company.getEmployeesBySalary(salaryFrom, salaryTo);
		
		printEmployees(io, String.format("Employees with salaries in range: %d - %d", salaryFrom, salaryTo), employees);
	}

	private static void getEmployeeByDepart(InputOutput io) {
		String depart = readDepart(io);
		List<Employee> employees = company.getEmployeesByDepartment(depart);
		
		printEmployees(io, String.format("Employees of department: %s", depart), employees);
	}

	private static void getEmployeeByMonth(InputOutput io) {
		int month = io.readInt("Enter birthday month of employees", "Wrond date", 1, 12);
		List<Employee> employees = company.getEmployeesByMonth(month);

		printEmployees(io, String.format("Employees born in: %s", Month.of(month)), employees);
	}

	private static void getAllEmployees(InputOutput io) {
		List<Employee> employees = company.getAllEmployees();
		
		printEmployees(io, "All employees:", employees);
	}
	
	private static void getEmployeeById(InputOutput io) {
		Long id = readId(io, true);
		if (id != null) {
			Employee empl = company.getEmployee(id);
			
			io.writeLine("Requested employee: ");
			printEmployee(empl, io);
		} else {
			io.writeLine("Employee with such id doesn't exist");
		}
	}

	private static Item adminItemMenu() {
		return new Menu("Admin actions", 
				Item.of("Add employee", io -> addEmployee(io)),
				Item.of("Remove employee", io -> removeEmployee(io)),
				Item.exit());
	}
	
	private static void removeEmployee(InputOutput io) {
		Long id = readId(io, true);
		if (id == null) {
			io.writeLine("User with such id doesn't exits");
		} else {
			Employee removed = company.removeEmployee(id);
			io.writeLine("Removed employee: ");
			printEmployee(removed, io);
		}		
	}

	private static void addEmployee(InputOutput io) {
		Long id = readId(io, false);
		if (id == null) {
			io.writeLine("User with such id already exists");
		} else {
			String name = readName(io);
			LocalDate birthday = io.readDateISO("Enter birthday of employee, format yyyy-MM-dd", "Wrong date");
			String department = readDepart(io);
			int salary = io.readInt("Enter salary of employee", "Wrong number");
			
			Employee newEmployee = new Employee(id, name, birthday, department, salary);
			if (company.addEmployee(newEmployee)) {
				io.writeLine("User added successfully");
			} else {
				io.writeLine("User with such id already exists");
			}
			printEmployee(newEmployee, io);
		}
	}
	
	private static void printEmployee(Employee empl, InputOutput io) {
		io.writeLine(String.format("%d %s %s %s %d", 
				empl.getId(), empl.getName(), empl.getBirthDate(), empl.getDepartment(), empl.getSalary()));
	}
	
	private static void printEmployees(InputOutput io, String message, List<Employee> employees) {
		io.writeLine(message);
		employees.forEach((empl) -> {
			printEmployee(empl, io);
		});
	}
	
	private static Long readId(InputOutput io, boolean exists) {
		long id = io.readLong("Enter id of employee", "Wrong number", 1, Long.MAX_VALUE);
		Employee empl = company.getEmployee(id);
		return (empl == null && !exists) || (empl != null && exists) ? id : null;
	}
	
	private static String readName(InputOutput io) {
		return io.readString("Enter name of employee");
	}

	private static String readDepart(InputOutput io) {
		return io.readStringOptions(String.format("Enter name of department from: %s", departments.stream().collect(Collectors.joining(", "))), "Entered wrong name of department", departments);
	}
	
}
