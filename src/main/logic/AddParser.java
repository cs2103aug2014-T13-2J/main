package main.logic;

public class AddParser extends CommandParser {
	
	private String userInput;
	
	public AddParser(String arguments) {
		super(arguments);
		userInput = arguments;
	}
	
	public String getUserInput() {
		return this.userInput;
	}

	public String parse() {

		return null;
	}

}
