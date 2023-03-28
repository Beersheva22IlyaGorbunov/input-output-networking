package telran.employees.net;

import java.io.Serializable;

import telran.employees.Company;
import telran.employees.Employee;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyNetworkProtocol implements Protocol {
	Company company;
	
	public CompanyNetworkProtocol(Company company) {
		this.company = company;
	}
	
	@Override
	public Response getResponse(Request request) {
		Response response = null;
		try {
			CompanyRequestType type = CompanyRequestType.valueOf(request.type);
			return switch (type) {
				case getById -> buildOkResponse(getEmployee(request.data));
				case getAll -> buildOkResponse(getAllEmployees());
				case add -> buildOkResponse(addEmployee(request.data));
				case getByMonth -> buildOkResponse(getEmployeesByMonth(request.data));
				case getByDepart -> buildOkResponse(getEmployeesByDepartment(request.data));
				case getBySalary -> buildOkResponse(getEmployeesBySalary(request.data));
				case removeEmployee -> buildOkResponse(removeEmployee(request.data));
				case save -> buildOkResponse(save(request.data));
				case restore -> buildOkResponse(restore(request.data));
				default -> new Response(ResponseCode.WRONG_REQUEST, "Unrecognisable type of request: " + request.type);
			};
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_REQUEST, "Unrecognisable type of request: " + request.type);
		}
	}
	
	private Response buildOkResponse (Serializable data) {
		return new Response(ResponseCode.OK, data);
	}
	
	private Serializable restore(Serializable data) {
		company.restore((String) data);
		return "Restored successfully";
	}

	private Serializable save(Serializable data) {
		company.save((String) data);
		return "Saved successfully";
	}

	private Serializable getEmployeesBySalary(Serializable data) {
			int[] salaries = (int[]) data;
			return (Serializable) company.getEmployeesBySalary(salaries[0], salaries[1]);
	}

	private Serializable getEmployeesByDepartment(Serializable data) {
			String department = (String) data;
			return (Serializable) company.getEmployeesByDepartment(department);
	}

	private Serializable getEmployeesByMonth(Serializable data) {
			Integer month = (int) data;
			return (Serializable) company.getEmployeesByMonth(month);
	}

	private Serializable addEmployee(Serializable data) {
		Employee employee = (Employee) data;
		return company.addEmployee(employee);
	}

	private Serializable getAllEmployees() {
		return (Serializable) company.getAllEmployees();
	}

	private Serializable getEmployee(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id);
	}
	
	private Serializable removeEmployee(Serializable data) {
		long id = (long) data;
		return company.removeEmployee(id);
	}

}
