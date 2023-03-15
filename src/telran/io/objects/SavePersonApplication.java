package telran.io.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SavePersonApplication {

	public static void main(String[] args) throws Exception {
		Person person = new Person(123, "Vasya");
		Person person2 = new Person(124, "Ilya");
		Persons persons = new Persons();
		persons.addPerson(person);
		persons.addPerson(person2);
		person.person = person2;
		person2.person = null;
		persons.save();

	}

}
