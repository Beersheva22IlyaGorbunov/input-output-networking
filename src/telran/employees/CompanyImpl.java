package telran.employees;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CompanyImpl implements Company {
	private HashMap<Long, Employee> employees = new HashMap<>();
	private HashMap<Integer, Set<Employee>> employeesMonth = new HashMap<>();
	private HashMap<String, Set<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<Integer, Set<Employee>> employeesSalaryTree = new TreeMap<>();

	
	private static final long serialVersionUID = 1L;

	@Override
	public Iterator<Employee> iterator() {
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		addToSetInMap(employee, employee.getSalary(), employeesSalaryTree);
		addToSetInMap(employee, employee.getDepartment(), employeesDepartment);
		addToSetInMap(employee, employee.getBirthDate().getMonthValue(), employeesMonth);
		return employees.put(employee.getId(), employee) == null ? true : false;
	}
	
	private <T> void addToSetInMap(Employee employee, T key, Map<T, Set<Employee>> map) {
		map.computeIfAbsent(key, k->new HashSet<>()).add(employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee deleted = employees.remove(id);
		if (deleted != null) {
			removeFromListInMap(deleted, deleted.getSalary(), employeesSalaryTree);
			removeFromListInMap(deleted, deleted.getDepartment(), employeesDepartment);
			removeFromListInMap(deleted, deleted.getBirthDate().getMonthValue(), employeesMonth);
		}
		return deleted;
	}
	
	private <T> void removeFromListInMap(Employee employee, T key, Map<T, Set<Employee>> map) {
		Set<Employee> set = map.get(key);
		set.remove(employee);
		if (set.size() == 0) {
			map.remove(key);
		}
	}

	@Override
	public List<Employee> getAllEmployees() {
		return new ArrayList<>(employees.values());
	}

	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		return new ArrayList<>(employeesMonth.getOrDefault(month, Collections.emptySet()));
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		List<Employee> res = new ArrayList<>();
		employeesSalaryTree.subMap(salaryFrom, true, salaryTo, true).values().forEach(set -> res.addAll(set));
		return res;
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return new ArrayList<>(employeesDepartment.getOrDefault(department, Collections.emptySet()));
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))){
			output.writeObject(getAllEmployees());
		} catch(Exception e) {
			throw new RuntimeException(e.toString());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			List<Employee> allEmployees = (List<Employee>) input.readObject();
			allEmployees.forEach(this::addEmployee);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}

	}

}
