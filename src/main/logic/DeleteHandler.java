package main.logic;

import java.util.ArrayList;

public class DeleteHandler extends CommandHandler {
	
	private DeleteParser parser;

	public DeleteHandler(String details) {
		super(details);
		parser = new DeleteParser(details);
	}

	@Override
	public String execute() {
		parser.parse();
		ArrayList<Integer> listOfIndexes = parser.getListOfIndexes();
		for (int index : listOfIndexes) {
			storage.deleteTask(index);
		}
		return "Deleted successfully!";
	}

}
