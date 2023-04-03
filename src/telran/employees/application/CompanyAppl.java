package telran.employees.application;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.application.controller.CompanyControllerItems;
import telran.view.InputOutput;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyAppl {

private static final String FILENAME = "localCompany.data";
	
	static Company company;

	public static void main(String[] args) throws Exception {
		company = new CompanyImpl();
		InputOutput io = new StandardInputOutput();
		
		try {
			company.restore(FILENAME);
			io.writeLine("Company restored from file successfully!");
		} catch (Exception e) {
			io.writeLine("Can't restore company from file: " + e.getMessage());
		}
		
		CompanyControllerItems controller = new CompanyControllerItems(company);
		Menu mainMenu = controller.getMainMenu(inpOut -> {
			company.save(FILENAME);
		});
		
		mainMenu.perform(io);
	}
}
