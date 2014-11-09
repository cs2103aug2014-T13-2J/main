package main.logic;

import java.io.IOException;
import java.util.ArrayList;

import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;
import main.storage.Task;

public class DeleteHandler extends CommandHandler {
	
	private static String MESSAGE_DELETE = "List of deleted tasks:";
	private static String MESSAGE_DELETED = "Note that you can undo to retrieve the deleted tasks.";
	private static String MESSAGE_INDEX_OUT_OF_BOUNDS = "Sorry, the task IDs you provided are invalid. Please try again.";

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
		ArrayList<Task> tasks = storage.getTasks();
		if (returnMessage.equals(CommandParser.MESSAGE_PARSE_SUCCESS)) {
			returnMessage = deleteSelectedTasks(tasks);
		} else if (returnMessage.equals(DeleteParser.ARGUMENT_ALL)) {
			returnMessage = deleteAllTasks(tasks);
		}
		return returnMessage;
	}

	private String deleteSelectedTasks(ArrayList<Task> tasks) {
		String returnMessage;
		try {
			ArrayList<Integer> listOfIndexes = parser.getListOfIndexes();
			System.out.println(MESSAGE_DELETE);
			DisplayHandler.displayTop();
			for (int index : listOfIndexes) {
				Task task = tasks.get(index);
				DisplayHandler.displayContents(index, task);
				storage.removeTask(index);
				try {
					googleCalendar.syncDeleteTask(task);
				} catch (IOException e) {
					storage.addDeletedTask(task);
				}
			}
			DisplayHandler.displayBottom();
			storage.saveCurrentState();
			returnMessage = MESSAGE_DELETED;
		} catch (IndexOutOfBoundsException e) {
			returnMessage = MESSAGE_INDEX_OUT_OF_BOUNDS;
		}
		return returnMessage;
	}
	
	private String deleteAllTasks(ArrayList<Task> tasks) {
		String returnMessage;
		int index = 0;
		DisplayHandler.displayTop();
		for (Task task : tasks) {
			DisplayHandler.displayContents(index, task);
			try {
				googleCalendar.syncDeleteTask(task);
			} catch (IOException e) {
				storage.addDeletedTask(task);
			}
			index++;
		}
		storage.clearAllTasks();
		storage.saveCurrentState();
		DisplayHandler.displayBottom();
		returnMessage = MESSAGE_DELETED;
		return returnMessage;
	}

}
