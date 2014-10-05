package logic;

public abstract class CommandParser {
	
	public abstract String parse(String userInput);
	
	protected String getCmdType(String userInput) {
		return null;
	}
	
	protected String getCmdDetails(String userInput) {
		return null;
	}
}
