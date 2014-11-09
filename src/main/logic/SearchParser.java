package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.RED;
import java.util.ArrayList;

import main.storage.Task;
import main.logic.DisplayHandler;

public class SearchParser extends CommandParser {
	public static final String MESSAGE_ERROR = "No search term specified.";
	public static final String MESSAGE_NULL = "Task list is empty. There is nothing to search from.";
	public static final String MESSAGE_UNAVAILABLE = "Tasker can't find what you asked for. Perhaps try another keyword?";
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

	@Override
	public String parse() {

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
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == 0) {
						System.out.print(DisplayHandler.displayTaskInTable(i,
								list.get(i)));
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
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == -1) {
						System.out.print(DisplayHandler.displayTaskInTable(i,
								list.get(i)));
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
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == -2) {
						System.out.print(DisplayHandler.displayTaskInTable(i,
								list.get(i)));
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
					if (DisplayHandler.determinePastPresentFuture(list.get(i)) == 1) {
						System.out.print(DisplayHandler.displayTaskInTable(i,
								list.get(i)));
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
		
		else if(userInput.length() == 1){
			return MESSAGE_UNAVAILABLE;
		}

		else {
			lowerCaseKey = userInput.toLowerCase();

			System.out.println();
			System.out.println(MESSAGE_SEARCH + " " + "'"
					+ ansi().fg(RED).a(userInput).reset() + "'" + ": ");
			DisplayHandler.displayTop();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
					System.out.print(DisplayHandler.displayTaskInTable(i,
							list.get(i)));
				}
			}

			DisplayHandler.displayBottom();

			return returnMessage;
		}

	}

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

	private boolean isToday() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isPast() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == -1) {
				return true;
			}
		}
		return false;
	}

	private boolean isFuture() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == 1) {
				return true;
			}
		}
		return false;
	}

	private boolean isFloating() {
		for (int i = 0; i < list.size(); i++) {
			if (DisplayHandler.determinePastPresentFuture(list.get(i)) == -2) {
				return true;
			}
		}
		return false;
	}

}
