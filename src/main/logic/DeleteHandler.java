package main.logic;

import java.util.ArrayList;

import main.storage.Storage;

public class DeleteHandler extends CommandHandler {
	
	private DeleteParser parser;
	private static Storage storage = Storage.getInstance();

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
