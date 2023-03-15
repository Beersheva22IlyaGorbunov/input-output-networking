package telran.employees;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private LocalDate birthDate;
	private String department;
	private int salary;
	
	public Employee(long id, String name, LocalDate birthDate, String department, int salary) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.department = department;
		this.salary = salary;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getDepartment() {
		return department;
	}

	public int getSalary() {
		return salary;
	}
	
}
