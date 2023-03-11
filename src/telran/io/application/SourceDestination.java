package telran.io.application;

import java.io.*;
import java.util.Arrays;
import java.util.logging.*;

public class SourceDestination {
	static Logger LOG = Logger.getLogger("Loger");

	public static void main(String[] args) {
		Handler handler = new ConsoleHandler();
		LOG.addHandler(handler);
		handler.setLevel(Level.FINE);
		LOG.setLevel(Level.FINE);
		try {
			if (args.length > 1) {
				LOG.fine(String.format("arguments %s", Arrays.deepToString(args)));
				try (Reader reader = args[0].equalsIgnoreCase("console") ? new InputStreamReader(System.in) : new FileReader(args[0]);
				PrintWriter writer = args[1].equalsIgnoreCase("console") ? new PrintWriter(new OutputStreamWriter(System.out)) : new PrintWriter(args[1]);) {
					sourceToDestination(reader, writer);
				}
			} else {
				System.out.println("too few arguments");
				LOG.severe("No arguments provided");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private static void sourceToDestination(Reader reader, PrintWriter writer) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		while(true) {
			String line = bufferedReader.readLine();
			if (line == null) {
				break;
			}
			writer.println(line);
		}
	}
}
