package telran.sportsman;

import java.lang.reflect.Constructor;

public class SportsmanAppl {

	private static final String BASE_PACKAGE = "telran.sportsman.";

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Must be at least one argument");
		} else {
			@SuppressWarnings("unchecked")
			Class<Sportsman> sportsmanClazz = (Class<Sportsman>) Class.forName(BASE_PACKAGE + args[0]);
			Constructor<Sportsman> constructor = args.length > 1 ? 
					sportsmanClazz.getConstructor(String.class) : sportsmanClazz.getConstructor();
			Sportsman sportsman = args.length > 1 ? 
					constructor.newInstance(args[1]) : constructor.newInstance();
			sportsman.action();
		}
	}

}
