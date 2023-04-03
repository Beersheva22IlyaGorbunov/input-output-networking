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
	private Company company;
	private Set<String> departments = new HashSet<>();
	
	public CompanyControllerItems(Company company) {
		this.company = company;
		departments.add("HR");
		departments.add("Management");
		departments.add("Economy");
		departments.add("Engeneer");
	}
	
	public Menu getMainMenu(Consumer<InputOutput> func) {
		return new Menu("Employees application", 
				adminItemMenu(),
				userItemMenu(),
				Item.of("Exit with save", func, true),
				Item.exit());
	}
	
	public Item userItemMenu() {
		return new Menu("User actions", 
				Item.of("Get all employees", io -> getAllEmployees(io)),
				Item.of("Get employee by Id", io -> getEmployeeById(io)),
				Item.of("Get employees by bithday month", io -> getEmployeeByMonth(io)),
				Item.of("Get employees by deparment", io -> getEmployeeByDepart(io)),
				Item.of("Get employees by salary", io -> getEmployeeBySalary(io)),
				Item.exit());
	}

	private void getEmployeeBySalary(InputOutput io) {
		int salaryFrom = io.readInt(null, null);
		int salaryTo = io.readInt(null, null);
		List<Employee> employees = company.getEmployeesBySalary(salaryFrom, salaryTo);
		
		printEmployees(io, String.format("Employees with salaries in range: %d - %d", salaryFrom, salaryTo), employees);
	}

	private void getEmployeeByDepart(InputOutput io) {
		String depart = readDepart(io);
		List<Employee> employees = company.getEmployeesByDepartment(depart);
		
		printEmployees(io, String.format("Employees of department: %s", depart), employees);
	}

	private void getEmployeeByMonth(InputOutput io) {
		int month = io.readInt("Enter birthday month of employees", "Wrond date", 1, 12);
		List<Employee> employees = company.getEmployeesByMonth(month);

		printEmployees(io, String.format("Employees born in: %s", Month.of(month)), employees);
	}

	private void getAllEmployees(InputOutput io) {
		List<Employee> employees = company.getAllEmployees();
		
		printEmployees(io, "All employees:", employees);
	}
	
	private void getEmployeeById(InputOutput io) {
		int id = readId(io);
		Employee empl = company.getEmployee(id);
		
		io.writeLine("Requested employee: ");
		printEmployee(empl, io);
	}

	public Item adminItemMenu() {
		return new Menu("Admin actions", 
				Item.of("Add employee", io -> addEmployee(io)),
				Item.of("Remove employee", io -> removeEmployee(io)),
				Item.exit());
	}
	
	private void removeEmployee(InputOutput io) {
		int id = readId(io);
		try {
			Employee removed = company.removeEmployee(id);
			io.writeLine("Removed employee: ");
			printEmployee(removed, io);
		} catch (Exception e) {
			io.writeLine(String.format("User with id: %d doesn't exits", id));
		}
		
	}

	private void addEmployee(InputOutput io) {
		int id = readId(io);
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
	
	private void printEmployee(Employee empl, InputOutput io) {
		io.writeLine(String.format("%d %s %s %s %d", 
				empl.getId(), empl.getName(), empl.getBirthDate(), empl.getDepartment(), empl.getSalary()));
	}
	
	private void printEmployees(InputOutput io, String message, List<Employee> employees) {
		io.writeLine(message);
		employees.forEach((empl) -> {
			printEmployee(empl, io);
		});
	}
	
	private int readId(InputOutput io) {
		return io.readInt("Enter id of employee", "Wrong number");
	}
	
	private String readName(InputOutput io) {
		return io.readString("Enter name of employee");
	}

	private String readDepart(InputOutput io) {
		return io.readStringOptions(String.format("Enter name of department from: %s", departments.stream().collect(Collectors.joining(", "))), "Entered wrong name of department", departments);
	}
	
}
