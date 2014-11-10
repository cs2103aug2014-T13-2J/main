package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.RED;
import java.util.ArrayList;

import main.storage.Task;
import main.logic.DisplayHandler;

//@author A0100239W
/**
 * This class parses through the user input and prints out either the valid
 * searched items or the appropriate messages if search keyword is not valid.
 */
public class SearchParser extends CommandParser {
	public static final String MESSAGE_ERROR = "Would you like to search \"floating\", \"past\", \"today\" or \"future\"?.";
	public static final String MESSAGE_NULL = "Task list is empty. There is nothing to search from.";
	public static final String MESSAGE_UNAVAILABLE = "Tasker can't find what you asked for. Perhaps you could try another keyword?";
	public static final String MESSAGE_SEARCH = "List of tasks containing";
	public static final String MESSAGE_NOT_TODAY = "There are no tasks due today.";
	public static final String MESSAGE_TODAY = "These are the tasks due today.";
	public static final String MESSAGE_PAST = "These are the tasks that were due in the past";
	public static final String MESSAGE_FLOAT = "This is the list of floating tasks";
	public static final String MESSAGE_NOT_FLOAT = "There are no floating tasks in the list currently.";
	public static final String MESSAGE_NOT_PAST = "There are no past tasks in the list.";
	public static final String MESSAGE_FUTURE = "These are the lists of upcoming tasks.";
	public static final String MESSAGE_NOT_FUTURE = "There are no upcoming tasks from tomorrow onwards.";
	public static final String MESSAGE_LONGER = "A search keyword should be longer than one character. Try again?";

	public static String returnMessage = "";
	public static String lowerCaseKey = "";

	private String userInput;
	private ArrayList<Task> list;

	public SearchParser(String arguments, ArrayList<Task> tasks) {
		super(arguments);
		userInput = arguments;
		list = tasks;

	}

	/*
	 * This method overwrites the parse() method of the CommandParser class. It
	 * checks what message to return based on the input, and prints out the
	 * valid return message.
	 * 
	 * @see main.logic.CommandParser#parse()
	 */
	@Override
	public String parse() {
		int oneCharacter = 1;

		if (list.isEmpty()) {
			return MESSAGE_NULL;
		}

		else if (userInput.equals("")) {
			return MESSAGE_ERROR;
		}

		else if (userInput.toLowerCase().equals("today")) {
			if (isToday()) {
				System.out.println();
				System.out.println(MESSAGE_TODAY);
				DisplayHandler.displayTop();

				for (int i = 0; i < list.size(); i++) {
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.PRESENT) {
						DisplayHandler.displayContents(i, list.get(i));
					}
				}

				DisplayHandler.displayBottom();

			} else {
				System.out.println(MESSAGE_NOT_TODAY);
			}

			return returnMessage;
		}

		else if (userInput.toLowerCase().equals("past")) {
			if (isPast()) {

				System.out.println();
				System.out.println(MESSAGE_PAST);
				DisplayHandler.displayTop();

				for (int i = 0; i < list.size(); i++) {
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.PAST) {
						DisplayHandler.displayContents(i, list.get(i));
					}
				}

				DisplayHandler.displayBottom();

			} else {
				System.out.println(MESSAGE_NOT_PAST);
			}

			return returnMessage;
		}

		else if (userInput.toLowerCase().equals("floating")) {
			if (isFloating()) {
				System.out.println();
				System.out.println(MESSAGE_FLOAT);
				DisplayHandler.displayTop();

				for (int i = 0; i < list.size(); i++) {
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.TASK_DOES_NOT_HAVE_DATE_TIME ) {
						DisplayHandler.displayContents(i, list.get(i));
					}
				}

				DisplayHandler.displayBottom();
			} else {
				System.out.println(MESSAGE_NOT_FLOAT);
			}
			return returnMessage;
		}

		else if (userInput.toLowerCase().equals("future")) {
			if (isFuture()) {

				System.out.println();
				System.out.println(MESSAGE_FUTURE);
				DisplayHandler.displayTop();

				for (int i = 0; i < list.size(); i++) {
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.FUTURE) {
						DisplayHandler.displayContents(i, list.get(i));
					}
				}

				DisplayHandler.displayBottom();

			} else {
				System.out.println(MESSAGE_NOT_FUTURE);
			}

			return returnMessage;
		}

		else if (!isWithin(userInput)) {
			return MESSAGE_UNAVAILABLE;
		}

		else if (userInput.length() == oneCharacter) {
			return MESSAGE_UNAVAILABLE;
		}

		else if (isWithin(userInput)) {
			lowerCaseKey = userInput.toLowerCase();

			System.out.println();
			System.out.println(MESSAGE_SEARCH + " " + "'"
					+ ansi().fg(RED).a(userInput).reset() + "'" + ": ");
			DisplayHandler.displayTop();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
					DisplayHandler.displayContents(i, list.get(i));
				}
			}

			DisplayHandler.displayBottom();

			return returnMessage;
		}
		return returnMessage;
	}

	/**
	 * This method checks whether the keyword is within the task list.
	 * 
	 * @param input
	 * @return
	 */
	private boolean isWithin(String input) {

		lowerCaseKey = userInput.toLowerCase();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDescription().toString().toLowerCase()
					.contains(lowerCaseKey)) {
				return true;
			} else if (list.get(i).hasVenue()
					&& list.get(i).getVenue().toString().toLowerCase()
							.contains(lowerCaseKey)
					&& !list.get(i).getVenue().contains("null")) {

				return true;
			} else if (list.get(i).hasStartDate()
					&& list.get(i).getStartDate().toString()
							.contains(lowerCaseKey)) {

				return true;
			} else if (list.get(i).hasStartTime()
					&& list.get(i).getStartTime().toString()
							.contains(lowerCaseKey)) {

				return true;
			} else if (list.get(i).hasEndDate()
					&& list.get(i).getEndDate().toString()
							.contains(lowerCaseKey)) {

				return true;
			} else if (list.get(i).hasEndTime()
					&& list.get(i).getEndTime().toString()
							.contains(lowerCaseKey)) {

				return true;
			}
		}

		return false;
	}

	/**
	 * This method checks whether there are any tasks to do from the current
	 * time until the end of the current day.
	 * 
	 * @return
	 */
	private boolean isToday() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.PRESENT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks whether there were any tasks that were already due.
	 * 
	 * @return
	 */
	private boolean isPast() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.PAST) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks whether there are tasks due from the next day onwards.
	 * 
	 * @return
	 */
	private boolean isFuture() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.FUTURE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks whether there are any floating tasks.
	 * 
	 * @return
	 */
	private boolean isFloating() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == DisplayHandler.TASK_DOES_NOT_HAVE_DATE_TIME) {
				return true;
			}
		}
		return false;
	}

}
