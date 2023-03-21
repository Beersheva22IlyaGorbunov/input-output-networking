package telran.net;

import java.io.Serializable;

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	public String type;
	public Serializable data;
	
	public Request(String type, Serializable data) {
		super();
		this.type = type;
		this.data = data;
	}
}
