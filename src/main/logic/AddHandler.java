package main.logic;

import main.storage.TaskBuilder;

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
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription(parser.getDescription());
		builder.setVenue(parser.getVenue());
		
		DateTime startDateTime = new DateTime(parser.getStartDateYear(), parser.getStartDateMonth(), parser.getStartDateDay, parser.getStartTimeHour(), 
				parser.getStartTimeMinute(), DEFAULT_SECOND, DEFAULT_MILLISECOND);
		DateTime startDateTime = new DateTime(parser.getEndDateYear(), parser.getEndDateMonth(), parser.getEndDateDay, parser.getEndTimeHour(), 
				parser.getEndTimeMinute(), DEFAULT_SECOND, DEFAULT_MILLISECOND);
		//store the task in storage
		
		return null;
	}

}
