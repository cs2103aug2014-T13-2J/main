package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.IOException;
import java.util.ArrayList;

import jline.ConsoleReader;
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
	private static ConsoleReader console;

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
			String DISPLAY_TABLE_ROW_STRING_FORMAT = displayFormat();
			System.out.println();
			System.out.println(MESSAGE_SEARCH + " " + "'"
					+ ansi().fg(RED).a(userInput).reset() + "'" + ": ");
			resultTop += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
					.fg(RED).a("ID").reset(),
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),
					ansi().fg(CYAN).a(" VENUE").reset(),
					ansi().fg(YELLOW).a(" TIME").reset(),
					ansi().fg(GREEN).a(" DATE").reset());
			resultTop += displayLineSeparator();
			System.out.print(resultTop);

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).toString().toLowerCase().contains(lowerCaseKey)) {
					System.out.println(DisplayHandler.displayTaskInTable(i,
							list.get(i)));
				}
			}

			resultBottom += displayLineSeparator();
			resultBottom += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset(),
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),
					ansi().fg(CYAN).a(" VENUE").reset(),
					ansi().fg(YELLOW).a(" TIME").reset(),
					ansi().fg(GREEN).a(" DATE").reset());
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

	private static String displayLineSeparator() {
		String lineString = "";
		int terminalWidth = getTerminalWidth();
		for (int i = 0; i < terminalWidth; i++) {
			lineString += "-";
		}
		lineString += "\n";
		return lineString;
	}

	private static String displayFormat() {
		String displayFormat = "";
		int terminalWidth = getTerminalWidth();
		int id = terminalWidth / 10;
		int description = terminalWidth / 10 * 5;
		int venue = terminalWidth / 10 * 3;
		int time = terminalWidth / 10 * 2;
		int date = terminalWidth / 10 * 2;
		String ID = "%-" + id + "s";
		String DESCRIPTION = "%-" + description + "s";
		String VENUE = "%-" + venue + "s";
		String TIME = "%-" + time + "s";
		String DATE = "%-" + date + "s";
		displayFormat = ID + "" + DESCRIPTION + "" + VENUE + "" + TIME + ""
				+ DATE + "\n";
		return displayFormat;
	}

	private static int getTerminalWidth() {
		try {
			console = new ConsoleReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int terminalWidth = console.getTermwidth();
		return terminalWidth;
	}
}
