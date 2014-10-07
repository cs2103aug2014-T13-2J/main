package main.logic;

public abstract class CommandParser {
	
	public static String parse(String userInput) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String getCommandType(String userInput) {
		String commandType = userInput.trim().split(" ")[0];
		return commandType;
	}
	
	protected String getCommandDetails(String userInput) {
		String commandDetails = userInput.trim().split(" ", 2)[1];
		return commandDetails;
	}
}
