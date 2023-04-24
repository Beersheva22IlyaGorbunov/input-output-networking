package telran.employees;

import java.io.Serializable;
import java.util.List;

public interface Company extends Iterable<Employee>, Serializable {
	boolean addEmployee(Employee employee);
	Employee removeEmployee(long id);
	List<Employee> getAllEmployees();
	List<Employee> getEmployeesByMonth(int month);
	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);
	List<Employee> getEmployeesByDepartment(String department);
	Employee getEmployee(long id);
	Employee updateSalary(long id, int salary);
	Employee updateDepartment(long id, String department);
	void save(String pathName);
	void restore(String pathName);
}
