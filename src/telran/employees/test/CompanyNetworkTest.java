package telran.employees.test;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;

import telran.employees.Employee;
import telran.employees.net.CompanyNetworkProxy;
import telran.net.NetworkClient;

public class CompanyNetworkTest extends CompanyTest {
	
	@BeforeEach
	void setUp() throws Exception {
		Iterator<Employee> iter = company.iterator();
		while (iter.hasNext()) {
			company.removeEmployee(iter.next().getId());
		}
		super.setUp();
	}
	
}
