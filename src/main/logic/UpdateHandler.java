package main.logic;

public class UpdateHandler extends CommandHandler {
	
	private UpdateParser parser;

	public UpdateHandler(String details) {
		super(details);
		parser = new UpdateParser(details);
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
		String field = parser.getField();
		if(field.equals(UpdateParser.FIELD_DESCRIPTION)) {
			//get task
			//update task
		} else if (field.equals(UpdateParser.FIELD_START_DATE)) {
			
		} else if (field.equals(UpdateParser.FIELD_START_TIME)) {
			
		} else if (field.equals(UpdateParser.FIELD_END_DATE)) {
			
		} else if (field.equals(UpdateParser.FIELD_END_TIME)) {
			
		} else if (field.equals(UpdateParser.FIELD_RECURRENCE)) {
			
		} else if (field.equals(UpdateParser.FIELD_REMINDER)) {
			
		} else { //FIELD_COMPLETED
			
		}
		return null;
	}
}
