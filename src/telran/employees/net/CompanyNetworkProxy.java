package telran.employees.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import telran.employees.Company;
import telran.employees.Employee;
import telran.employees.PairId;
import telran.net.NetworkClient;

public class CompanyNetworkProxy implements Company, Closeable {
	private static final long serialVersionUID = 1L;
	
	NetworkClient client;
	
	public CompanyNetworkProxy(NetworkClient client) {
		this.client = client;
	}

	@Override
	public Iterator<Employee> iterator() {
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		return client.send("addEmployee", (Serializable) employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		return client.send("removeEmployee", id);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return client.send("getAllEmployees", "");
	}

	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		return client.send("getEmployeesByMonthBirth", month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		int[] salaries = {salaryFrom, salaryTo};
		return client.send("getEmployeesBySalary", salaries);
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return client.send("getEmployeesByDepartment", department);
	}

	@Override
	public Employee getEmployee(long id) {
		return client.send("getEmployee", id);
	}

	@Override
	public void save(String pathName) {
		client.send("save", pathName);
	}

	@Override
	public void restore(String pathName) {
		client.send("restore", pathName);
	}

	@Override
	public void close() throws IOException {
		client.close();
	}

	@Override
	public Employee updateSalary(long id, int salary) {
		return client.send("updateSalary", new PairId<>(id, salary));
	}

	@Override
	public Employee updateDepartment(long id, String department) {
		return client.send("updateDepartment", new PairId<>(id, department));
	}

}
