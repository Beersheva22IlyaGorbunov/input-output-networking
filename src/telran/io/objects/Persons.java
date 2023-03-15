package telran.io.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Persons implements Serializable, Iterable<Person> {
	static String filePath = "persons.data";
	private static final long serialVersionUID = 1L;
	
	List<Person> persons = new ArrayList<Person>();

	@Override
	public Iterator<Person> iterator() {
		return persons.iterator();
	}
	
	void addPerson(Person person) {
		persons.add(person);
	}
	
	public void save() {
		try {
			writeObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeObject() throws Exception {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(filePath)));) {
			output.writeObject(this);
		}
	}

	static public Persons restore(){
		Persons res = null;
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("persons.data"));) {
			res = (Persons) input.readObject();
		} catch (Exception e) {
			res = new Persons();
		}
		return res;
	}
	
}
