package telran.employees;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class CompanyImpl implements Company {
	private HashMap<Long, Employee> employees = new HashMap<>();
	private HashMap<Integer, Set<Employee>> employeesMonth = new HashMap<>();
	private HashMap<String, Set<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<Integer, Set<Employee>> employeesSalaryTree = new TreeMap<>();
	
	ReentrantReadWriteLock employeesLock = new ReentrantReadWriteLock();
	ReentrantReadWriteLock salaryLock = new ReentrantReadWriteLock();
	ReentrantReadWriteLock departmentLock = new ReentrantReadWriteLock();
	ReentrantReadWriteLock monthLock = new ReentrantReadWriteLock();
	
	ReadLock employeesReadLock = employeesLock.readLock();
	ReadLock salaryReadLock = salaryLock.readLock();
	ReadLock departmentReadLock = departmentLock.readLock();
	ReadLock monthReadLock = monthLock.readLock();
	
	WriteLock employeesWriteLock = employeesLock.writeLock();
	WriteLock salaryWriteLock = salaryLock.writeLock();
	WriteLock departmentWriteLock = departmentLock.writeLock();
	WriteLock monthWriteLock = monthLock.writeLock();
	
	private static final long serialVersionUID = 1L;

	@Override
	public Iterator<Employee> iterator() {
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		lock(employeesWriteLock, salaryWriteLock, departmentWriteLock, monthWriteLock);
		try {
			addToSetInMap(employee, employee.getSalary(), employeesSalaryTree);
			addToSetInMap(employee, employee.getDepartment(), employeesDepartment);
			addToSetInMap(employee, employee.getBirthDate().getMonthValue(), employeesMonth);
			return employees.put(employee.getId(), employee) == null ? true : false;
		} finally {
			unlock(employeesWriteLock, salaryWriteLock, departmentWriteLock, monthWriteLock);
		}
	}

	private <T> void addToSetInMap(Employee employee, T key, Map<T, Set<Employee>> map) {
		map.computeIfAbsent(key, k->new HashSet<>()).add(employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		lock(employeesWriteLock, salaryWriteLock, departmentWriteLock, monthWriteLock);
			try {
			Employee deleted = employees.remove(id);
			if (deleted != null) {
				removeFromListInMap(deleted, deleted.getSalary(), employeesSalaryTree);
				removeFromListInMap(deleted, deleted.getDepartment(), employeesDepartment);
				removeFromListInMap(deleted, deleted.getBirthDate().getMonthValue(), employeesMonth);
			}
			return deleted;
		} finally {
			unlock(employeesWriteLock, salaryWriteLock, departmentWriteLock, monthWriteLock);
		}
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
		lock(employeesReadLock);
		try {
			return new ArrayList<>(employees.values());
		} finally {
			unlock(employeesReadLock);
		}
	}

	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		lock(monthReadLock);
		try {
			return new ArrayList<>(employeesMonth.getOrDefault(month, Collections.emptySet()));
		} finally {
			unlock(monthReadLock);
		}
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		lock(salaryReadLock);
		try {
			List<Employee> res = new ArrayList<>();
			employeesSalaryTree.subMap(salaryFrom, true, salaryTo, true).values().forEach(set -> res.addAll(set));
			return res;
		} finally {
			unlock(salaryReadLock);
		}
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		lock(departmentReadLock);
		try {
			return new ArrayList<>(employeesDepartment.getOrDefault(department, Collections.emptySet()));
		} finally {
			unlock(departmentReadLock);
		}
	}

	@Override
	public Employee getEmployee(long id) {
		lock(employeesReadLock);
		try {
			return employees.get(id);
		} finally {
			unlock(employeesReadLock);
		}
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

	@Override
	public Employee updateSalary(long id, int salary) {
		lock(employeesReadLock);
		lock(salaryWriteLock);
		try {
			Employee employee = employees.get(id);
			if (employee != null) {
				removeFromListInMap(employee, employee.getSalary(), employeesSalaryTree);
				employee.setSalary(salary);
				addToSetInMap(employee, employee.getSalary(), employeesSalaryTree);
			}
			return employee;
		} finally {
			unlock(employeesReadLock);
			unlock(salaryWriteLock);
		}
	}

	@Override
	public Employee updateDepartment(long id, String department) {
		lock(employeesReadLock);
		lock(departmentWriteLock);
		try {
			Employee employee = getEmployee(id);
			if (employee != null) {
				removeFromListInMap(employee, employee.getDepartment(), employeesDepartment);
				employee.setDepartment(department);
				addToSetInMap(employee, employee.getDepartment(), employeesDepartment);
			}
			return employee;
		} finally {
			unlock(employeesReadLock);
			unlock(departmentWriteLock);
		}
	}	
	
	private void lock(Lock ...locks) {
		Arrays.stream(locks).forEach(lock -> lock.lock());
	}
	
	private void unlock(Lock ...locks) {
		Arrays.stream(locks).forEach(lock -> lock.unlock());
	}

}
