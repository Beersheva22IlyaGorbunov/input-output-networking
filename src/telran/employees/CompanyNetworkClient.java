package telran.employees;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import telran.net.NetworkClient;

public class CompanyNetworkClient implements Company {
	private static final long serialVersionUID = 1L;
	
	NetworkClient client;
	
	public CompanyNetworkClient(NetworkClient client) {
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
		return client.send(CompanyRequestType.getAll.toString(), null);
	}

	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		return client.send(CompanyRequestType.getByMonth.toString(), month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		String salaries = String.format("%s-%s", salaryFrom, salaryTo);
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

}
