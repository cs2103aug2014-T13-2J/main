package main.logic;

import java.util.ArrayList;

import main.storage.Task;
import main.logic.DisplayHandler;

public class SearchParser extends CommandParser {
	public static final String MESSAGE_ERROR = "No search term specified.";
	public static final String MESSAGE_NULL = "There is nothing to search.";
	private String userInput;
	private ArrayList<Task> list;

	public SearchParser(String arguments, ArrayList<Task> tasks) {
		super(arguments);
		userInput = arguments;
		list = tasks;

	}

	@Override
	public String parse() {
		String returnMessage = "";
		String lowerCaseKey = "";

		if (list.isEmpty()) {
			return MESSAGE_NULL;
		}

		else if (userInput.equals("")) {
			return MESSAGE_ERROR;
		}

		else {
			lowerCaseKey = userInput.toLowerCase();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
					System.out.println(DisplayHandler.displayAllTasks(i, list.get(i)));
				}
			}

			return returnMessage;

		}

	}
}
