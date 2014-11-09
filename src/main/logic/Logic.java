package main.logic;

import java.util.regex.PatternSyntaxException;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;

public class Logic {

	private final static String MESSAGE_UNRECOGNIZED_COMMAND = "Unrecognized command.";
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

	private enum CommandType {
		ADD, DELETE, DISPLAY, UPDATE, SEARCH, UNDO, REPEAT, REMIND, SYNC, INVALID, EXIT
	}

	public static String uiToLogic(String userCommand) {
		try {
			String commandTypeString = getFirstWord(userCommand);
			CommandType commandType = determineCommandType(commandTypeString);
			String details = removeFirstWord(userCommand);
			String message = executeCommand(commandType, details);
			return message;
		} catch (PatternSyntaxException e) {
			return MESSAGE_UNRECOGNIZED_COMMAND;
		}

	}

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
		} /* else if (commandTypeString.equalsIgnoreCase("repeat")) {
			TaskerLog.logSystemInfo("repeat command detected");
			return CommandType.REPEAT;
		} else if (commandTypeString.equalsIgnoreCase("remind")) {
			TaskerLog.logSystemInfo("remind command detected");
			return CommandType.REMIND; 
		} */ else if (commandTypeString.equalsIgnoreCase("sync")) {
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
		case REPEAT:
			return repeatTask(details);
		case REMIND:
			return remindTask(details);
		case SYNC:
			return googleCalendar.syncToGoogle();
		case EXIT:
			googleCalendar.killSyncThread();
			System.exit(0);
		default:
			TaskerLog.logSystemInfo("Unable to execute invalid command type.");
			return MESSAGE_UNRECOGNIZED_COMMAND;
		}
	}

	private static String addTask(String details) {
		CommandHandler executor = new AddHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String deleteTask(String details) {
		CommandHandler executor = new DeleteHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String displayTasks(String details) {
		CommandHandler executor = new DisplayHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String updateTask(String details) {
		CommandHandler executor = new UpdateHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String remindTask(String details) {
		CommandHandler executor = new RemindHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String repeatTask(String details) {
		CommandHandler executor = new RepeatHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String undo(String details) {
		CommandHandler executor = new UndoHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String searchTask(String details) {
		CommandHandler executor = new SearchHandler(details);
		String message = executor.execute();
		return message;
	}

	private static String getFirstWord(String userCommand) {
		String firstWord = userCommand.trim().split(" ")[0];
		return firstWord;
	}

	private static String removeFirstWord(String userCommand) {
		String withoutFirstWord = userCommand.replaceFirst(
				getFirstWord(userCommand), "").trim();
		return withoutFirstWord;
	}

}
