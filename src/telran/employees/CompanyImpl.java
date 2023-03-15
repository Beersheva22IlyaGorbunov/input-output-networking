package telran.employees;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CompanyImpl implements Company {
	Map<Long, Employee> employees;
	Map<String, LinkedList<Employee>> departmentMap;
	List<LinkedList<Employee>> listByBirthday;
	LinkedHashSet<Employee> employeesToIterate;
	NavigableMap<Integer, LinkedList<Employee>> employeesSalaryTree;

	
	private static final long serialVersionUID = 1L;
	
	public CompanyImpl () {
		employees = new HashMap<>();
		departmentMap = new HashMap<>();
		listByBirthday = new ArrayList<LinkedList<Employee>>(12);
		employeesToIterate = new LinkedHashSet<>();
		employeesSalaryTree = new TreeMap<>();
		
		for (int i = 0; i < 12; i++) {
			listByBirthday.add(new LinkedList<Employee>());
		}
	}

	@Override
	public Iterator<Employee> iterator() {
		return employeesToIterate.iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		addToListInMap(employee, employee.getSalary(), employeesSalaryTree);
		addToListInMap(employee, employee.getDepartment(), departmentMap);
		employeesToIterate.add(employee);
		listByBirthday.get(employee.getBirthDate().getMonthValue()).add(employee);
		return employees.put(employee.getId(), employee) == null ? true : false;
	}
	
	private <T> void addToListInMap(Employee employee, T key, Map<T, LinkedList<Employee>> map) {
		LinkedList<Employee> list = map.get(key);
		if (list == null) {
			list = new LinkedList<Employee>();
			map.put(key, list);
		}
		list.add(employee);	
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee deleted = employees.remove(id);
		if (deleted != null) {
			removeFromListInMap(deleted, deleted.getSalary(), employeesSalaryTree);
			removeFromListInMap(deleted, deleted.getDepartment(), departmentMap);
			employeesToIterate.remove(deleted);
			listByBirthday.get(deleted.getBirthDate().getMonthValue()).remove(deleted);
		}
		return deleted;
	}
	
	private <T> void removeFromListInMap(Employee employee, T key, Map<T, LinkedList<Employee>> map) {
		LinkedList<Employee> list = map.get(key);
		list.remove(employee);
		if (list.size() == 0) {
			map.remove(key);
		}
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeesToIterate.stream().toList();
	}

	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		return listByBirthday.get(month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		List<Employee> res = new ArrayList<>();
		employeesSalaryTree.subMap(salaryFrom, true, salaryTo, true).values().forEach(set -> res.addAll(set));
		return res;
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return departmentMap.get(department);
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(pathName)));) {
			output.writeObject(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName));) {
			CompanyImpl savedCompany = (CompanyImpl) input.readObject();
			this.employees = savedCompany.employees;
			this.departmentMap = savedCompany.departmentMap;
			this.employeesSalaryTree = savedCompany.employeesSalaryTree;
			this.employeesToIterate = savedCompany.employeesToIterate;
			this.listByBirthday = savedCompany.listByBirthday;
			savedCompany = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
