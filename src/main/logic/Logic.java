package main.logic;

import java.util.regex.PatternSyntaxException;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;

//@author A0072744A
/**
 * This class handles the user commands, determines the appropriate command
 * before constructing and executing the corresponding classes.
 */
public class Logic {

	private final static String MESSAGE_INVALID_COMMAND = "Sorry, you entered an invalid command.";
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

	/**
	 * This enumerator is to store the commands available to the user.
	 */
	private enum CommandType {
		ADD, DELETE, DISPLAY, UPDATE, SEARCH, UNDO, REPEAT, REMIND, SYNC, INVALID, EXIT
	}

	/**
	 * This method is to retrieve the user command from the UI component before
	 * executing the corresponding command.
	 * 
	 * @param userCommand
	 * @return the relevant result from the corresponding execution of command
	 */
	public static String uiToLogic(String userCommand) {
		try {
			String commandTypeString = getFirstWord(userCommand);
			CommandType commandType = determineCommandType(commandTypeString);
			String details = removeFirstWord(userCommand);
			String message = executeCommand(commandType, details);
			return message;
		} catch (PatternSyntaxException e) {
			return MESSAGE_INVALID_COMMAND;
		}

	}

	/**
	 * This method determines the command type from the user command.
	 * 
	 * @param commandTypeString
	 * @return the corresponding CommandType
	 */
	private static CommandType determineCommandType(String commandTypeString) {
		TaskerLog.logSystemInfo("determineCommandType received input");
		if (commandTypeString.equalsIgnoreCase("add")) {
			TaskerLog.logSystemInfo("add command detected");
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			TaskerLog.logSystemInfo("delete command detected");
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			TaskerLog.logSystemInfo("display command detected");
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("update")) {
			TaskerLog.logSystemInfo("update command detected");
			return CommandType.UPDATE;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			TaskerLog.logSystemInfo("search command detected");
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			TaskerLog.logSystemInfo("undo command detected");
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("sync")) {
			TaskerLog.logSystemInfo("login command detected");
			return CommandType.SYNC;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			TaskerLog.logSystemInfo("exit command detected");
			return CommandType.EXIT;
		} else {
			TaskerLog.logSystemInfo("Invalid command type detected.");
			return CommandType.INVALID;
		}
	}

	/**
	 * This method executes the corresponding command after determing the
	 * command type.
	 * 
	 * @param commandType
	 * @param details
	 * @return the relevant result from the corresponding execution of commands
	 */
	public static String executeCommand(CommandType commandType, String details) {
		TaskerLog.logSystemInfo("executeCommand received commandType");
		switch (commandType) {
		case ADD:
			return addTask(details);
		case DELETE:
			return deleteTask(details);
		case DISPLAY:
			return displayTasks(details);
		case UPDATE:
			return updateTask(details);
		case SEARCH:
			return searchTask(details);
		case UNDO:
			return undo(details);
		case SYNC:
			return googleCalendar.syncToGoogle();
		case EXIT:
			googleCalendar.killSyncThread();
			System.exit(0);
		default:
			TaskerLog.logSystemInfo("Unable to execute invalid command type.");
			return MESSAGE_INVALID_COMMAND;
		}
	}

	/**
	 * This method constructs the AddHandler to add a new task.
	 * 
	 * @param details
	 * @return the relevant result from the adding
	 */
	private static String addTask(String details) {
		CommandHandler executor = new AddHandler(details);
		String message = executor.execute();
		return message;
	}

	/**
	 * This method constructs the DeleteHandler to delete a task.
	 * 
	 * @param details
	 * @return the relevant result from the deleting
	 */
	private static String deleteTask(String details) {
		CommandHandler executor = new DeleteHandler(details);
		String message = executor.execute();
		return message;
	}

	/**
	 * This method constructs the DisplayHandler to display all tasks.
	 * 
	 * @param details
	 * @return the list of tasks in a formatted table
	 */
	private static String displayTasks(String details) {
		CommandHandler executor = new DisplayHandler(details);
		String message = executor.execute();
		return message;
	}

	/**
	 * This method constructs the UpdateHandler to update a task.
	 * 
	 * @param details
	 * @return the relevant result from the updating
	 */
	private static String updateTask(String details) {
		CommandHandler executor = new UpdateHandler(details);
		String message = executor.execute();
		return message;
	}

	/**
	 * This method constructs the UndoHandler to undo an action.
	 * 
	 * @param details
	 * @return the relevant result from the undo
	 */
	private static String undo(String details) {
		CommandHandler executor = new UndoHandler(details);
		String message = executor.execute();
		return message;
	}

	/**
	 * This method constructs the SearchHandler to search for a task.
	 * 
	 * @param details
	 * @return the relevant search result
	 */
	private static String searchTask(String details) {
		CommandHandler executor = new SearchHandler(details);
		String message = executor.execute();
		return message;
	}

	/**
	 * This method gets the first word in the user command.
	 * 
	 * @param userCommand
	 * @return the first word of the user command
	 */
	private static String getFirstWord(String userCommand) {
		String firstWord = userCommand.trim().split(" ")[0];
		return firstWord;
	}

	/**
	 * This method removes the first word in the user command.
	 * 
	 * @param userCommand
	 * @return the user command without the first word
	 */
	private static String removeFirstWord(String userCommand) {
		String withoutFirstWord = userCommand.replaceFirst(
				getFirstWord(userCommand), "").trim();
		return withoutFirstWord;
	}

}
