package main.logic;

public class AddHandler extends CommandHandler {
	
	private AddParser parser;

	public AddHandler(String details) {
		super(details);
		parser = new AddParser(details);
	}

	@Override
	public String execute() {
		
		return null;
	}

}
