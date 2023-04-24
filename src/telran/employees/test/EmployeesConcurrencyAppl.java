package telran.employees.test;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import telran.employees.ClientImitator;
import telran.employees.Employee;
import telran.net.TcpClient;

public class EmployeesConcurrencyAppl {
	private static final String HOST = "localhost";
	private static final int N_INITIAL_EMPLOYEES = 100;
	private static final String DEPARTMENT = "department";
	private static final String NAME = "name";
	private static final int SALARY = 5000;
	private static final int N_CLIENTS = 200;
	private static final int N_RUNS = 100;
	private static final int PORT = 3001;

	public static void main(String[] args) throws Exception {
		ClientImitator.setnEmployees(N_INITIAL_EMPLOYEES);
		ClientImitator.setnRuns(N_RUNS);
		addEmployees();
		ClientImitator[] clients = new ClientImitator[N_CLIENTS];
		startClients(clients);
		waitClients(clients);
		closingConnections(clients);
	}

	private static void closingConnections(ClientImitator[] clients) throws Exception {
		for (int i = 0; i < N_CLIENTS; i++) {
			clients[i].close();
		}

	}

	private static void waitClients(ClientImitator[] clients) throws Exception {
		for (int i = 0; i < N_CLIENTS; i++) {
			clients[i].join();
		}

	}

	private static void startClients(ClientImitator[] clients) throws Exception {
		for (int i = 0; i < N_CLIENTS; i++) {
			clients[i] = new ClientImitator(new TcpClient(HOST, PORT));
			clients[i].start();
		}

	}

	private static void addEmployees() throws Exception {
		TcpClient clientAdds = new TcpClient(HOST, PORT);
		IntStream.rangeClosed(1, N_INITIAL_EMPLOYEES).forEach(
				i -> clientAdds.send("addEmployee", new Employee(i, NAME, LocalDate.now(), DEPARTMENT, SALARY)));
		clientAdds.close();
	}
}