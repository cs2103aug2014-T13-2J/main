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
import main.storage.Storage;
import main.storage.Task;

public class DeleteHandler extends CommandHandler {
	private static String MESSAGE_DELETE = "List of deleted tasks: ";
	private DeleteParser parser;
	private static Storage storage = Storage.getInstance();
	private static ConsoleReader console;
	

	public DeleteHandler(String details) {
		super(details);
		parser = new DeleteParser(details);
	}

	@Override
	public String execute() {
		parser.parse();
		ArrayList<Integer> listOfIndexes = parser.getListOfIndexes();
		String returnMessage = "";
		String resultTop = "";
		String resultBottom = "";
		String DISPLAY_TABLE_ROW_STRING_FORMAT = displayFormat();
		System.out.println();
		System.out.println(MESSAGE_DELETE);
		resultTop += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
				.fg(RED).a("ID").reset(),
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),
				ansi().fg(CYAN).a(" VENUE").reset(),
				ansi().fg(YELLOW).a(" TIME").reset(),
				ansi().fg(GREEN).a(" DATE").reset());
		resultTop += displayLineSeparator();
		System.out.print(resultTop);

		for (int index : listOfIndexes) {
			ArrayList<Task> list = storage.getTasks();
			System.out.println(DisplayHandler.displayTaskInTable(index,
					list.get(index)));
			storage.deleteTask(index);
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
	
	private static String displayLineSeparator() {
		String lineString = "";
		int terminalWidth = getTerminalWidth();
		for (int i = 0; i < terminalWidth; i++) {
			lineString += "-";
		}
		lineString += "\n";
		return lineString;
	}

}
