package main.logic;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

//@author A0100239W
/**
 * This class handles the searching of tasks.
 */
public class SearchHandler extends CommandHandler {
	private SearchParser parser;
	private static Storage storage = Storage.getInstance();

	/*
	 * This method passes the user input and current task list to the search
	 * parser.
	 */
	public SearchHandler(String details) {
		super(details);
		ArrayList<Task> tasks = storage.getTasks();
		parser = new SearchParser(details, tasks);
	}

	/*
	 * This method overrides the main execute() function of CommandHandler. 
	 * @see main.logic.CommandHandler#execute()
	 */
	@Override
	public String execute() {
		return parser.parse();
	}

}
