package telran.employees;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import telran.net.NetworkClient;

public class ClientImitator extends Thread {
	private static final String NAME = "name";
	private static final String DEPARTMENT = "department";
	private static final int SALARY = 5000;
	private static final int DEFAULT_N_RUNS = 100;
	private static final int DEFAULT_N_EMPLOYEES = 100;
	static int nRuns = DEFAULT_N_RUNS;
	static int nEmployees = DEFAULT_N_EMPLOYEES;

	public static int getnRuns() {
		return nRuns;
	}

	public static void setnRuns(int nRuns) {
		ClientImitator.nRuns = nRuns;
	}

	public static int getnEmployees() {
		return nEmployees;
	}

	public static void setnEmployees(int nEmployees) {
		ClientImitator.nEmployees = nEmployees;
	}

	private static HashMap<String, Supplier<Serializable>> methods = new HashMap<>();
	private static String[] methodNames = { "addEmployee", "removeEmployee", "getEmployee", "getAllEmployees",
			"getEmployeesByMonthBirth", "getEmployeesBySalary", "getEmployeesByDepartment", "updateDepartment",
			"updateSalary" };
	static {
		methods.put("addEmployee",
				() -> new Employee(getRandomNumber(100000, 1000000), NAME, LocalDate.now(), DEPARTMENT, SALARY));
		methods.put("removeEmployee", () -> getRandomNumber(1, nEmployees + 1));
		methods.put("updateSalary", () -> new PairId<>(getRandomNumber(1, nEmployees + 1), SALARY));
		methods.put("updateDepartment", () -> new PairId<>(getRandomNumber(1, nEmployees + 1), DEPARTMENT));
		methods.put("getEmployee", () -> getRandomNumber(1, nEmployees + 1));
		methods.put("getAllEmployees", () -> "");
		methods.put("getEmployeesByMonthBirth", () -> LocalDate.now().getMonthValue());
		methods.put("getEmployeesBySalary", () -> new int[] { SALARY, SALARY + 1 });
		methods.put("getEmployeesByDepartment", () -> DEPARTMENT);

	}
	NetworkClient client;

	private static long getRandomNumber(int i, int j) {

		return ThreadLocalRandom.current().nextInt(i, j);
	}

	@Override
	public void run() {
		for (int i = 0; i < nRuns; i++) {
			String methodName = methodNames[(int) getRandomNumber(0, methodNames.length)];
			client.send(methodName, methods.get(methodName).get());
		}
		System.out.println("Ended");
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ClientImitator(NetworkClient client) {

		this.client = client;
	}

	public void close() throws IOException {
		client.close();

	}
}