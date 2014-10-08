package main.logic;

import java.util.ArrayList;

public class Logic {

	private enum CommandType {
		ADD, DELETE, DISPLAY, UPDATE, INVALID, EXIT
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
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}

	public static String executeCommand(CommandType commandType, String details) {
		return null;
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
