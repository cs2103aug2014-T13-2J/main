package main.logic;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

public class SearchHandler extends CommandHandler {

	private SearchParser parser;
	private static Storage storage = Storage.getInstance();

	public SearchHandler(String details) {
		super(details);
		ArrayList<Task> tasks = storage.getTasks();
		parser = new SearchParser(details, tasks);
	}

	@Override
	public String execute() {
		return parser.parse();
	}

}
