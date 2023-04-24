package telran.employees;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Employee implements Serializable, Comparable<Employee> {

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
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return id == other.id;
	}
	
	@Override
	public int compareTo(Employee o) {
		
		return Long.compare(id, o.id);
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}
}
