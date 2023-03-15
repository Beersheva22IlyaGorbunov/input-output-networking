package telran.io.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class RestorePersonApplication {

	public static void main(String[] args) throws Exception {
		Persons persons = Persons.restore();
		persons.forEach(System.out::println);
	}

}
