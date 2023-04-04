package telran.employees.application;

import java.util.HashSet;
import java.util.Set;

import telran.employees.Company;
import telran.employees.application.controller.CompanyControllerItems;
import telran.employees.net.CompanyNetworkProxy;
import telran.net.NetworkClient;
import telran.net.TcpClient;
import telran.view.InputOutput;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyClientAppl {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3001;
	private static final String FILENAME = "company.data";
	
	static Company company;
	static Set<String> departments = new HashSet<>();

	public static void main(String[] args) throws Exception {
		NetworkClient client = new TcpClient(HOSTNAME, PORT);
		company = new CompanyNetworkProxy(client);
		InputOutput io = new StandardInputOutput();
		
		departments.add("HR");
		departments.add("Management");
		departments.add("Economy");
		departments.add("Engeneer");
		
		try {
			company.restore(FILENAME);
		} catch (Exception e) {
			io.writeLine("Can't restore company from file: " + e.getMessage());
		}
		
		Menu mainMenu = CompanyControllerItems.getCompanyMenu(company, departments, inpOut -> {
			company.save(FILENAME);
		});
		
		mainMenu.perform(io);
		
		((CompanyNetworkProxy)company).close();
	}

	

}
