package main.logic;

import java.util.ArrayList;
import java.util.Collections;

public class DeleteParser extends CommandParser {
	
	private static String MESSAGE_INVALID_ARGUMENT_TYPE = "Sorry, the arguments must only contain the task IDs to be deleted. Please try again.";
	public static String ARGUMENT_ALL = "all";
	
	private String arguments;
	private ArrayList<Integer> listOfIndexes;

	public DeleteParser(String arguments) {
		super(arguments);
		this.arguments = arguments;
		listOfIndexes = new ArrayList<>();
	}

	@Override
	public String parse() {
		if (arguments.equalsIgnoreCase(ARGUMENT_ALL)) {			
			return ARGUMENT_ALL;
		} else {
			int index = 0;
			String[] indexes = arguments.split(" ");
			try {
				for (String indexString : indexes) {
					index = Integer.parseInt(indexString) - 1;
					listOfIndexes.add(index);
				}
				Collections.sort(listOfIndexes);
				Collections.reverse(listOfIndexes);
			} catch (NumberFormatException e) {
				return MESSAGE_INVALID_ARGUMENT_TYPE;
			}
		}
		return MESSAGE_PARSE_SUCCESS;
	}
	
	public ArrayList<Integer> getListOfIndexes() {
		return listOfIndexes;
	}
}
