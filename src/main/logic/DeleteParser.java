package main.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import main.storage.Storage;

//@author A0072744A
public class DeleteParser extends CommandParser {

	private static String MESSAGE_INVALID_ARGUMENT_TYPE = "Sorry, you have entered invalid task IDs. Please try again.";
	private static String MESSAGE_DUPLICATE_ARGUMENTS = "Sorry, you have entered duplicate task IDs. Please try again.";
	public static String ARGUMENT_ALL = "all";

	private static Storage storage = Storage.getInstance();
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
			int numberOfStoredTasks = storage.getTasks().size();
			String[] indexes = arguments.split(" ");
			try {
				for (String indexString : indexes) {
					index = Integer.parseInt(indexString) - 1;
					if (index < 0 || index >= numberOfStoredTasks) {
						return MESSAGE_INVALID_ARGUMENT_TYPE;
					} else {
						listOfIndexes.add(index);
					}
				}
				Collections.sort(listOfIndexes);
				Collections.reverse(listOfIndexes);
			} catch (NumberFormatException e) {
				return MESSAGE_INVALID_ARGUMENT_TYPE;
			}
			index = -1;
			for (int deleteIndex : listOfIndexes) {
				if (Objects.equals(deleteIndex, index)) {
					return MESSAGE_DUPLICATE_ARGUMENTS;
				}
				index = deleteIndex;
			}
		}
		return MESSAGE_PARSE_SUCCESS;
	}

	public ArrayList<Integer> getListOfIndexes() {
		return listOfIndexes;
	}
}
