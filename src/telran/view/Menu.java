package telran.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Menu implements Item {
	public static final int STARS_AMOUNT = 40;
	private String name;
	private ArrayList<Item> items;

	public Menu(String name, ArrayList<Item> items) {
		this.name = name;
		this.items = items;
	}
	
	public Menu(String name, Item ...items) {
		this(name, new ArrayList<>(Arrays.asList(items)));
	}

	@Override
	public String displayName() {
		return name;
	}

	@Override
	public void perform(InputOutput io) {
		boolean running = true;
		try {
			while (running) {
				displayTitle(io);
				displayItems(io);
				int itemNumber = io.readInt("Enter item number", "Wrong item number", 
						1, items.size());
				Item item = items.get(itemNumber - 1);
				item.perform(io);
				if (item.isExit()) {
					running = false;
				}
			}
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		}
	}

	private void displayItems(InputOutput io) {
		IntStream.rangeClosed(1, items.size())
		.forEach(index -> io.writeLine(String.format("%d. %s", index, items.get(index - 1).displayName())));
	}

	private void displayTitle(InputOutput io) {
		io.writeLine("*".repeat(STARS_AMOUNT));
		int leftOffset = (STARS_AMOUNT - name.length() - 2) / 2;
		int rightOffset = leftOffset;
		if (name.length() % 2 == 1) {
			rightOffset = leftOffset + 1;
		}
		io.writeLine(String.format("*%s%s%s*", " ".repeat(leftOffset), name ," ".repeat(rightOffset)));
		io.writeLine("*".repeat(STARS_AMOUNT));
	}

	@Override
	public boolean isExit() {
		return false;
	}


}
