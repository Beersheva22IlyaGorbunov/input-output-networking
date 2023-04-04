package telran.sportsman;

public class Footballer implements Sportsman {
	String team;

	@Override
	public void action() {
		System.out.printf("Plays football %s\n", team != null ? "for team " + team : "");
	}

	public Footballer() {
		
	}

	public Footballer(String team) {
		this.team = team;
	}
}
