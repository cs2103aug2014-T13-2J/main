package main.logic;

import java.util.ArrayList;

import main.storage.Task;

public class SearchHandler extends CommandHandler {

	private SearchParser parser;

	public SearchHandler(String details) {
		super(details);
		ArrayList<Task> tasks = getCurrentTaskList();
		parser = new SearchParser(details, tasks);
	}

	@Override
	public String execute() {
		return parser.parse();
	}

}
