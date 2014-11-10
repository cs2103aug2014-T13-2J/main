package main.logic;

import java.io.IOException;
import java.util.ArrayList;

import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;
import main.storage.Task;

//@author A0072744A
/**
 * This class handles the deleting of tasks.
 */
public class DeleteHandler extends CommandHandler {

	private static String MESSAGE_DELETE = "List of deleted tasks:";
	private static String MESSAGE_DELETED = "Note that you can undo to retrieve the deleted tasks.";

	private DeleteParser parser;
	private static Storage storage = Storage.getInstance();
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

	/**
	 * This method creates a new DeleteParser constructor to parse the user
	 * parameters.
	 * 
	 * @param details
	 */
	public DeleteHandler(String details) {
		super(details);
		parser = new DeleteParser(details);
	}

	/*
	 * This method overrides the execute() method of CommandHandler class. It
	 * retrieves the stored tasks and delete according to the user parameters.
	 * 
	 * @see main.logic.CommandHandler#execute()
	 */
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

	/**
	 * This method deletes user-selected tasks before executing the
	 * synchronisation.
	 * 
	 * @param tasks
	 * @return the relevant delete result for the user
	 */
	private String deleteSelectedTasks(ArrayList<Task> tasks) {
		String returnMessage;
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
		return returnMessage;
	}

	/**
	 * This method deletes all tasks before executing the synchronisation.
	 * 
	 * @param tasks
	 * @return the relevant delete result for the user
	 */
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
