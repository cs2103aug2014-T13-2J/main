package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.util.ArrayList;

import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;
import main.storage.Task;

public class DeleteHandler extends CommandHandler {
	private static String MESSAGE_DELETE = "List of deleted tasks: \n";
	private static String MESSAGE_INDEX_OUT_OF_BOUNDS = "Sorry, the arguments are out of bounds of the task list. Please try again.";
	private static final String DISPLAY_TABLE_ROW_STRING_FORMAT = "%-10s %-35s %-30s %-20s %-20s\n";

	private DeleteParser parser;
	private static Storage storage = Storage.getInstance();
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

	public DeleteHandler(String details) {
		super(details);
		parser = new DeleteParser(details);
	}

	@Override
	public String execute() {
		String returnMessage = parser.parse();
		if (returnMessage.equals(CommandParser.MESSAGE_PARSE_SUCCESS)) {
			try {
				ArrayList<Integer> listOfIndexes = parser.getListOfIndexes();
				String resultTop = "";
				String resultBottom = "";
				returnMessage += "\n";
				returnMessage += MESSAGE_DELETE;
				resultTop += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
						ansi().fg(RED).a("ID").reset(),
						ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi()
								.fg(CYAN).a(" VENUE").reset(), ansi()
								.fg(YELLOW).a(" TIME").reset(), ansi()
								.fg(GREEN).a(" DATE").reset());
				resultTop += DisplayHandler.displayLineSeparator();
				returnMessage += resultTop;

				for (int index : listOfIndexes) {
					ArrayList<Task> list = storage.getTasks();
					Task task = list.get(index);
					String eventId = task.getEventId();
					returnMessage += DisplayHandler.displayTaskInTable(index,
							task) + "\n";
					storage.deleteTask(index);
					googleCalendar.syncDeleteNonFloatingTask(eventId);

				}
				saveCurrentState();
				resultBottom += DisplayHandler.displayLineSeparator();
				resultBottom += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
						ansi().fg(RED).a("ID").reset(),
						ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi()
								.fg(CYAN).a(" VENUE").reset(), ansi()
								.fg(YELLOW).a(" TIME").reset(), ansi()
								.fg(GREEN).a(" DATE").reset());
				returnMessage += resultBottom;
			} catch (IndexOutOfBoundsException e) {
				returnMessage = MESSAGE_INDEX_OUT_OF_BOUNDS;
			}
		}

		return returnMessage;
	}

}
