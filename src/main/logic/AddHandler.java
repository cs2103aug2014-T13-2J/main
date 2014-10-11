package main.logic;

public class AddHandler extends CommandHandler {
	
	private AddParser parser;
	
	public AddHandler(String details) {
		super(details);
		parser = new AddParser(details);
	}

	@Override
	public String execute() {
		try {
			//parser will analyze the user input and store each piece of information into its respective
			//attributes
			parser.parse();
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		
		//use TaskBuilder to create the task
		//store the task in storage
		
		return null;
	}

}
