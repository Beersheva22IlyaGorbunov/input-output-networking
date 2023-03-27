package telran.employees;

import java.io.Serializable;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyNetworkProtocol implements Protocol {
	Company company;
	private final String SALARIES_DIVIDER = "-";
	
	public CompanyNetworkProtocol(Company company) {
		this.company = company;
	}
	
	@Override
	public Response getResponse(Request request) {
		CompanyRequestType type = CompanyRequestType.valueOf(request.type);
		Response response = switch (type) {
			case getById -> getEmployee(request.data);
			case getAll -> getAllEmployees();
			case add -> addEmployee(request.data);
			case getByMonth -> getEmployeesByMonth(request.data);
			case getByDepart -> getEmployeesByDepartment(request.data);
			case getBySalary -> getEmployeesBySalary(request.data);
			case removeEmployee -> removeEmployee(request.data);
			case iterator -> iterator(request.data);
			case save -> save(request.data);
			case restore -> restore(request.data);
			default -> new Response(ResponseCode.WRONG_REQUEST, "Unrecognisable type of request: " + request.type);
		};
		return response;
	}
	
	private Response restore(Serializable data) {
		try {
			company.restore((String) data);
			return new Response(ResponseCode.OK, "Restored successfully");
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response save(Serializable data) {
		try {
			company.save((String) data);
			return new Response(ResponseCode.OK, "Saved successfully");
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response iterator(Serializable data) {
		try {
			return new Response(ResponseCode.OK, (Serializable) company.iterator());
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response getEmployeesBySalary(Serializable data) {
		try {
			String[] salaries = ((String) data).split(SALARIES_DIVIDER);
			if (salaries.length == 2) {
				Integer minSalary = Integer.parseInt(salaries[0]);
				Integer maxSalary = Integer.parseInt(salaries[1]);
				return new Response(ResponseCode.OK, (Serializable) company.getEmployeesBySalary(minSalary, maxSalary));
			} else {
				throw new Exception("Couldn't parse data to min and max salaries");
			}
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response getEmployeesByDepartment(Serializable data) {
		try {
			String department = (String) data;
			return new Response(ResponseCode.OK, (Serializable) company.getEmployeesByDepartment(department));
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response getEmployeesByMonth(Serializable data) {
		try {
			Integer month = (Integer) data;
			return new Response(ResponseCode.OK, (Serializable) company.getEmployeesByMonth(month));
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response addEmployee(Serializable data) {
		try {
			Employee employee = (Employee) data;
			return new Response(ResponseCode.OK, company.addEmployee(employee));
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	private Response getAllEmployees() {
		return new Response(ResponseCode.OK, (Serializable) company.getAllEmployees());
	}

	private Response getEmployee(Serializable data) {
		try {
			Long id = (Long) data;
			return new Response(ResponseCode.OK, company.getEmployee(id));
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}
	
	private Response removeEmployee(Serializable data) {
		try {
			Long id = (Long) data;
			return new Response(ResponseCode.OK, company.removeEmployee(id));
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

}
