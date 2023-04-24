package telran.employees.net;

import java.io.Serializable;
import java.lang.reflect.Method;

import telran.employees.Company;
import telran.employees.Employee;
import telran.employees.PairId;
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
	public  Response getResponse(Request request) {
		Response response = null;
		try {
			Method method = getClass().getDeclaredMethod(request.type,
					Serializable.class);
			response = buildOkResponse((Serializable)method.invoke(this, request.data));
		} catch (NoSuchMethodException  e1) {
			response = new Response(ResponseCode.WRONG_REQUEST, request.type + " Request type not found");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(request.type + " " + request.data);
			response = new Response(ResponseCode.WRONG_DATA, e.toString());
		} 
		return response;
	}
	
	private Response buildOkResponse (Serializable data) {
		return new Response(ResponseCode.OK, data);
	}
	
	private Serializable restore(Serializable data) {
		try {
			company.restore((String) data);
			return "Restored successfully";
		} catch (Exception e) {
			return e.getMessage();
		}
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

	private Serializable getEmployeesByMonthBirth(Serializable data) {
			Integer month = (int) data;
			return (Serializable) company.getEmployeesByMonth(month);
	}

	private Serializable addEmployee(Serializable data) {
		Employee employee = (Employee) data;
		return company.addEmployee(employee);
	}

	private Serializable getAllEmployees(Serializable data) {
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
	
	private Serializable updateSalary(Serializable data) {
		@SuppressWarnings("unchecked")
		PairId<Integer> idSalary = (PairId<Integer>) data;
		return company.updateSalary(idSalary.id(), idSalary.value());
	}
	
	private Serializable updateDepartment(Serializable data) {
		@SuppressWarnings("unchecked")
		PairId<String> idDepartment = (PairId<String>) data;
		return company.updateDepartment(idDepartment.id(), idDepartment.value());
	}

}
