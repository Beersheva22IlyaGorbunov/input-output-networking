package telran.employees.application;

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

	public static void main(String[] args) throws Exception {
		NetworkClient client = new TcpClient(HOSTNAME, PORT);
		company = new CompanyNetworkProxy(client);
		InputOutput io = new StandardInputOutput();
		
		try {
			company.restore(FILENAME);
		} catch (Exception e) {
			io.writeLine("Can't restore company from file: " + e.getMessage());
		}
		
		CompanyControllerItems controller = new CompanyControllerItems(company);
		Menu mainMenu = controller.getMainMenu(inpOut -> {
			company.save(FILENAME);
		});
		
		mainMenu.perform(io);
		
		((CompanyNetworkProxy)company).close();
	}

	

}
