package telran.io.objects;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 2L;
	
	public long id;
	public String name;
	public Person person;
	
	public Person(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + "]";
 	}

}
