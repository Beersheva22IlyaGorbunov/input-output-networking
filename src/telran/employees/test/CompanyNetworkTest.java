package telran.employees.test;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;

import telran.employees.CompanyNetworkClient;
import telran.employees.Employee;
import telran.net.NetworkClient;

public class CompanyNetworkTest extends CompanyTest {
	static NetworkClient client;
	
	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyNetworkClient(client);
		Iterator<Employee> iter = company.iterator();
		while (iter.hasNext()) {
			company.removeEmployee(iter.next().getId());
		}
		super.setUp();
	}
	
}
