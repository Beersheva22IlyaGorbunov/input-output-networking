package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.Employee;

class CompanyImplTest {
	final private String FILE_PATH = "SavedCompany.data";
	Company company;
	Employee vasya = new Employee(1, "Vasya", LocalDate.of(1992, 6, 12), "IT", 60000);
	Employee petya = new Employee(2, "Petya", LocalDate.of(1986, 3, 1), "Sales", 50000);
	Employee natasha = new Employee(3, "Natasha", LocalDate.of(2002, 6, 1), "Sales", 40000);
	
	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		company.addEmployee(vasya);
		company.addEmployee(petya);
		company.addEmployee(natasha);
	}
	
	@Test
	void addTest() {
		assertFalse(company.addEmployee(natasha));
	}
	
	@Test
	void getAllTest() {
		List<Employee> employeesList = company.getAllEmployees();
		assertEquals(3, employeesList.size());
	}
	
	@Test
	void getEmployeeTest() {
		Employee employee = company.getEmployee(1);
		assertEquals(employee, vasya);
	}
	
	@Test
	void getByBirthdateTest() {
		List<Employee> employeesList = company.getEmployeesByMonth(6);
		String [] expectedNames = new String[2];
		expectedNames[0] = "Vasya";
		expectedNames[1] = "Natasha";
		assertEquals(2, employeesList.size());
		String [] actual = employeesList.stream().map(empl -> empl.getName()).toArray(String[]::new);
		Arrays.sort(actual);
		Arrays.sort(expectedNames);
		assertArrayEquals(actual, expectedNames);
	}
	
	@Test
	void getBySalaryTest() {
		List<Employee> employeesList = company.getEmployeesBySalary(40000, 50000);
		String [] expectedNames = new String[2];
		expectedNames[0] = "Natasha";
		expectedNames[1] = "Petya";
		assertEquals(2, employeesList.size());
		String[] actual = employeesList.stream().map(empl -> empl.getName()).toArray(String[]::new);
		Arrays.sort(actual);
		Arrays.sort(expectedNames);
		assertArrayEquals(expectedNames, actual);
	}
	
	@Test
	void getByDepartmentTest() {
		List<Employee> employeesList = company.getEmployeesByDepartment("Sales");
		String [] expectedNames = new String[2];
		expectedNames[0] = "Petya";
		expectedNames[1] = "Natasha";
		assertEquals(2, employeesList.size());
		String[] actual = employeesList.stream().map(empl -> empl.getName()).toArray(String[]::new);
		Arrays.sort(actual);
		Arrays.sort(expectedNames);
		assertArrayEquals(expectedNames, actual);
	}
	
	@Test
	void removeTest() {
		Employee deleted = company.removeEmployee(2);
		assertEquals(deleted.getName(), "Petya");
		assertNull(company.removeEmployee(25));
		
		List<Employee> employeesInDepartment = company.getEmployeesByDepartment("Sales");
		assertEquals(1, employeesInDepartment.size());
		
		List<Employee> employeesBySalary = company.getEmployeesBySalary(40000, 50000);
		assertEquals(1, employeesBySalary.size());
		
		List<Employee> employeesList = company.getAllEmployees();
		assertEquals(2, employeesList.size());
		
		List<Employee> employeesByBirthdate = company.getEmployeesByMonth(deleted.getBirthDate().getMonthValue());
		assertEquals(0, employeesByBirthdate.size());
	}
	
	@Test
	void iteratorTest() {
		Iterator<Employee> employeesIterator = company.iterator();
		String [] expectedNames = new String[3];
		expectedNames[0] = "Vasya";
		expectedNames[1] = "Petya";
		expectedNames[2] = "Natasha";
		for (int i = 0; i < expectedNames.length; i++) {
			assertEquals(expectedNames[i], employeesIterator.next().getName());
		}
		assertFalse(employeesIterator.hasNext());
		assertThrowsExactly(NoSuchElementException.class, () -> employeesIterator.next());
	}
	
	@Test
	void saveAndRestoreTest() {
		company.save(FILE_PATH);
		company = new CompanyImpl();
		assertEquals(0, company.getAllEmployees().size());
		company.restore(FILE_PATH);
		assertEquals(3, company.getAllEmployees().size());
		String [] expectedNames = new String[3];
		expectedNames[0] = "Vasya";
		expectedNames[1] = "Petya";
		expectedNames[2] = "Natasha";
		assertArrayEquals(expectedNames, company.getAllEmployees().stream().map(empl -> empl.getName()).toArray());
		
		company.save("");
		company.restore("");
	}

}
