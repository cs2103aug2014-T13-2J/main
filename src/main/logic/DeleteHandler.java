package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

public class DeleteHandler extends CommandHandler {
	private static String MESSAGE_DELETE = "List of deleted tasks: ";
	private DeleteParser parser;
	private static Storage storage = Storage.getInstance();
	

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
		String DISPLAY_TABLE_ROW_STRING_FORMAT = DisplayHandler.displayFormat();
		System.out.println();
		System.out.println(MESSAGE_DELETE);
		resultTop += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
				.fg(RED).a("ID").reset(),
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),
				ansi().fg(CYAN).a(" VENUE").reset(),
				ansi().fg(YELLOW).a(" TIME").reset(),
				ansi().fg(GREEN).a(" DATE").reset());
		resultTop += DisplayHandler.displayLineSeparator();
		System.out.print(resultTop);

		for (int index : listOfIndexes) {
			ArrayList<Task> list = storage.getTasks();
			System.out.println(DisplayHandler.displayTaskInTable(index,
					list.get(index)));
			storage.deleteTask(index);
		}
		
		resultBottom += DisplayHandler.displayLineSeparator();
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
