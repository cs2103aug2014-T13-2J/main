package main.logic;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import main.storage.Storage;
import main.storage.Task;
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
		
		Task task = convertParsedDetailsToTask();
		Storage storage = Storage.getInstance();
		storage.addTask(task);
		return null;
	}
	
	public Task convertParsedDetailsToTask() {
		TaskBuilder builder = new TaskBuilder();
		
		String description = parser.getDescription();
		builder.setDescription(description);
		
		if(parser.hasVenue()) {
			String venue = parser.getVenue();
			builder.setVenue(venue);
		}
		
		if(parser.hasStartDate()) {
			Integer startDateYear = Integer.parseInt(parser.getStartDateYear());
			Integer startDateMonth = Integer.parseInt(parser.getStartDateMonth());
			Integer startDateDay = Integer.parseInt(parser.getStartDateDay());
			
			LocalDate date = new LocalDate(startDateYear, startDateMonth, startDateDay);

			builder.setStartDate(date);
		}
		
		if(parser.hasStartTime()) {
			Integer startTimeHour = Integer.parseInt(parser.getStartTimeHour());
			Integer startTimeMinute = Integer.parseInt(parser.getStartTimeMinute());
			
			LocalTime time = new LocalTime(startTimeHour, startTimeMinute);
			
			builder.setStartTime(time);
		}
		
		if(parser.hasEndDate()) {
			Integer endDateYear = Integer.parseInt(parser.getEndDateYear());
			Integer endDateMonth = Integer.parseInt(parser.getEndDateMonth());
			Integer endDateDay = Integer.parseInt(parser.getEndDateDay());
			
			LocalDate date = new LocalDate(endDateYear, endDateMonth, endDateDay);

			builder.setStartDate(date);
		}
		
		if(parser.hasEndTime()) {
			Integer endTimeHour = Integer.parseInt(parser.getEndTimeHour());
			Integer endTimeMinute = Integer.parseInt(parser.getEndTimeMinute());
			
			LocalTime time = new LocalTime(endTimeHour, endTimeMinute);
			
			builder.setEndTime(time);
		}
		
		return builder.buildTask();
		
	}

}
