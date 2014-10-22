package main.logic;

import java.util.ArrayList;

import main.storage.Task;
import main.logic.DisplayHandler;

public class SearchParser extends CommandParser {
	public static final String MESSAGE_ERROR = "No search term specified.";
	public static final String MESSAGE_NULL = "Task list is empty. There is nothing to search from.";
	public static final String MESSAGE_UNAVAILABLE = "Search term cannot be found in task list.";
	public static String returnMessage = "";
	public static String lowerCaseKey = "";

	private String userInput;
	private ArrayList<Task> list;

	public SearchParser(String arguments, ArrayList<Task> tasks) {
		super(arguments);
		userInput = arguments;
		list = tasks;

	}

	@Override
	public String parse() {

		if (list.isEmpty()) {
			return MESSAGE_NULL;
		}

		else if (userInput.equals("")) {
			return MESSAGE_ERROR;
		}

		else if (!isWithin(userInput)) {
			return MESSAGE_UNAVAILABLE;
		}

		else {
			lowerCaseKey = userInput.toLowerCase();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
					System.out.println(DisplayHandler.displayTaskInTable(i,
							list.get(i)));
				}
			}

			return returnMessage;
		}

	}

	public boolean isWithin(String input) {

		lowerCaseKey = userInput.toLowerCase();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
				return true;
			}
		}

		return false;
	}
}
