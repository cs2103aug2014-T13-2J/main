package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.util.ArrayList;

import main.storage.Task;
import main.logic.DisplayHandler;

public class SearchParser extends CommandParser {
	public static final String MESSAGE_ERROR = "No search term specified.";
	public static final String MESSAGE_NULL = "Task list is empty. There is nothing to search from.";
	public static final String MESSAGE_UNAVAILABLE = "Search term cannot be found in task list.";
	public static final String MESSAGE_SEARCH = "List of tasks containing";

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
			String resultTop = "";
			String resultBottom = "";
			System.out.println();
			System.out.println(MESSAGE_SEARCH + " " + "'"
					+ ansi().fg(RED).a(userInput).reset() + "'" + ": ");
			resultTop += DisplayHandler.displayLineSeparator();
			resultTop += String.format(DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a("ID").reset()," |",
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),"|",
					ansi().fg(CYAN).a(" VENUE").reset(),"|",
					ansi().fg(YELLOW).a(" TIME").reset(),"|",
					ansi().fg(GREEN).a(" DATE").reset());
			resultTop += DisplayHandler.displayLineSeparator();
			System.out.print(resultTop);

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
					System.out.print(DisplayHandler.displayTaskInTable(i,
							list.get(i)));
				}
			}

			resultBottom += String.format(DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset()," |",
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),"|",
					ansi().fg(CYAN).a(" VENUE").reset(),"|",
					ansi().fg(YELLOW).a(" TIME").reset(),"|",
					ansi().fg(GREEN).a(" DATE").reset());
			resultBottom += DisplayHandler.displayLineSeparator();
			System.out.print(resultBottom);

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
