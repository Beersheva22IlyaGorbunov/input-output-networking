package telran.employees;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class CompanyImpl implements Company {
	Map<Long, Employee> employees = new HashMap<>();
	Map<String, Set<Employee>> departmentMap = new HashMap<>();
	Map<Integer, Set<Employee>> birthdayMap = new HashMap<>();
	LinkedHashSet<Employee> employeesToIterate = new LinkedHashSet<>();
	NavigableMap<Integer, Set<Employee>> employeesSalaryTree = new TreeMap<>();

	
	private static final long serialVersionUID = 1L;

	@Override
	public Iterator<Employee> iterator() {
		return employeesToIterate.iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		boolean res = false;
		if (employees.putIfAbsent(employee.getId(), employee) == null) {
			addToListInMap(employee, employee.getSalary(), employeesSalaryTree);
			addToListInMap(employee, employee.getDepartment(), departmentMap);
			addToListInMap(employee, employee.getBirthDate().getMonthValue(), birthdayMap);
			employeesToIterate.add(employee);	
			res = true;
		}
		return res;
	}
	
	private <T> void addToListInMap(Employee employee, T key, Map<T, Set<Employee>> map) {
		map.computeIfAbsent(key, k -> new HashSet<>()).add(employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee deleted = employees.remove(id);
		if (deleted != null) {
			removeFromListInMap(deleted, deleted.getSalary(), employeesSalaryTree);
			removeFromListInMap(deleted, deleted.getDepartment(), departmentMap);
			removeFromListInMap(deleted, deleted.getBirthDate().getMonthValue(), birthdayMap);
			employeesToIterate.remove(deleted);
		}
		return deleted;
	}
	
	private <T> void removeFromListInMap(Employee employee, T key, Map<T, Set<Employee>> map) {
		Set<Employee> list = map.get(key);
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
		return new ArrayList<>(birthdayMap.getOrDefault(month, Collections.emptySet()));
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return employeesSalaryTree.subMap(salaryFrom, true, salaryTo, true).values().stream().flatMap(Set::stream).toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return new ArrayList<>(departmentMap.getOrDefault(department, Collections.emptySet()));
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(pathName)));) {
			output.writeObject(getAllEmployees());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName));) {
			Collection<Employee> readedEmployees= (Collection<Employee>) input.readObject();
			readedEmployees.forEach(employee -> this.addEmployee(employee));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
