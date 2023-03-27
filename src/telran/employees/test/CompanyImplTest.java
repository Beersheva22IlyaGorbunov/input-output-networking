package telran.employees.test;

import org.junit.jupiter.api.BeforeEach;

import telran.employees.CompanyImpl;

class CompanyImplTest extends CompanyTest {

	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		super.setUp();
	}
	
}
