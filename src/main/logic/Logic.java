package main.logic;

public class Logic {

	private enum CommandType {
		ADD, DELETE, DISPLAY, UPDATE, SEARCH, UNDO, REPEAT, REMIND, INVALID, EXIT
	}

	public static String uiToLogic(String userCommand) {
		String commandTypeString = getFirstWord(userCommand);
		CommandType commandType = determineCommandType(commandTypeString);
		String details = removeFirstWord(userCommand);
		String message = executeCommand(commandType, details);
		return message;
	}

	private static CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add")) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("update")) {
			return CommandType.UPDATE;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("repeat")) {
			return CommandType.REPEAT;
		} else if (commandTypeString.equalsIgnoreCase("remind")) {
			return CommandType.REMIND;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}

	public static String executeCommand(CommandType commandType, String details) {
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
		case EXIT:
			System.exit(0);
		default:
			return null;
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
		String withoutFirstWord = userCommand.replaceFirst(getFirstWord(userCommand), "").trim();
		return withoutFirstWord;
	}

}
