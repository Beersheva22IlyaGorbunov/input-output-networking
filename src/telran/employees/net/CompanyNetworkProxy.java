package telran.employees.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import telran.employees.Company;
import telran.employees.Employee;
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
		return client.send(CompanyRequestType.add.toString(), (Serializable) employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		return client.send(CompanyRequestType.removeEmployee.toString(), id);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return client.send(CompanyRequestType.getAll.toString(), "");
	}

	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		return client.send(CompanyRequestType.getByMonth.toString(), month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		int[] salaries = {salaryFrom, salaryTo};
		return client.send(CompanyRequestType.getBySalary.toString(), salaries);
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return client.send(CompanyRequestType.getByDepart.toString(), department);
	}

	@Override
	public Employee getEmployee(long id) {
		return client.send(CompanyRequestType.getById.toString(), id);
	}

	@Override
	public void save(String pathName) {
		client.send(CompanyRequestType.save.toString(), pathName);
	}

	@Override
	public void restore(String pathName) {
		client.send(CompanyRequestType.restore.toString(), pathName);
	}

	@Override
	public void close() throws IOException {
		client.close();
	}

}
