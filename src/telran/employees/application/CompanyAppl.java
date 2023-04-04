package telran.employees.application;

import java.util.HashSet;
import java.util.Set;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.application.controller.CompanyControllerItems;
import telran.view.InputOutput;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyAppl {

private static final String FILENAME = "localCompany.data";
	
	static Company company;
	static Set<String> departments = new HashSet<>();

	public static void main(String[] args) throws Exception {
		company = new CompanyImpl();
		InputOutput io = new StandardInputOutput();
		
		departments.add("HR");
		departments.add("Management");
		departments.add("Economy");
		departments.add("Engeneer");
		
		try {
			company.restore(FILENAME);
			io.writeLine("Company restored from file successfully!");
		} catch (Exception e) {
			io.writeLine("Can't restore company from file: " + e.getMessage());
		}

		Menu mainMenu = CompanyControllerItems.getCompanyMenu(company, departments, inpOut -> {
			company.save(FILENAME);
		});
		
		mainMenu.perform(io);
	}
}
