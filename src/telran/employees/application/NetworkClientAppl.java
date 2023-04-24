package telran.employees.application;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import telran.employees.Company;
import telran.employees.application.controller.CompanyControllerItems;
import telran.employees.net.CompanyNetworkProxy;
import telran.net.NetworkClient;
import telran.view.InputOutput;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class NetworkClientAppl {

	private static final String BASE_PACKAGE = "telran.net.";

	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("You must provide path to configuration file");
		} else {
			Path filePath = Path.of(args[0]);
			try (FileInputStream input = new FileInputStream(filePath.toString())) {
				Properties props = new Properties();
				props.load(input);
				
				NetworkClient client = getNetworkClient(props);
				CompanyNetworkProxy company = new CompanyNetworkProxy(client);
				InputOutput io = new StandardInputOutput();
				
				String[] departments = props.getProperty("departments").split(", ");
				String database = (String) props.getProperty("database", "default.data");
				
				Menu mainMenu = CompanyControllerItems.getCompanyMenu(company, Set.of(departments), inpOut -> {
					company.save(database);
				});
				
				mainMenu.perform(io);
				
				company.close();
			} catch (FileNotFoundException e) {
				System.out.println("Can't find file with path: " + filePath);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static NetworkClient getNetworkClient(Properties props) {
		try {
			String hostname;
			int port = -1;
			String transport = null;
			try {
				hostname = (String) props.getProperty("hostname");
			}  catch (Exception e) {
				throw new ClassCastException("Can't read hostname from configuration");
			}
			
			try {
				port = Integer.parseInt((String) props.getProperty("port"));
			} catch (Exception e) {
				throw new ClassCastException("Can't read port from configuration");
			}
			
			try {
				transport = (String) props.getProperty("transport");
			} catch (Exception e) {
				throw new ClassCastException("Can't read transport name from configuration");
			}
			
			String className = transport + "Client";
			Class<NetworkClient> clientClazz = (Class<NetworkClient>) Class.forName(BASE_PACKAGE + className);
			Constructor<NetworkClient> constructor = clientClazz.getConstructor(String.class, int.class);
			return constructor.newInstance(hostname, port);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Can't create connection class");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
