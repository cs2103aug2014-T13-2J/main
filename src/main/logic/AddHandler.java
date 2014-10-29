package main.logic;

import java.io.IOException;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;
import main.storage.Task;
import main.storage.TaskBuilder;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.google.api.services.calendar.model.Event;

public class AddHandler extends CommandHandler {

	final static String TASK_ADDED_MESSAGE = "Task added!";
	
	private AddParser parser;
	private static Storage storage = Storage.getInstance();
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

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
			Task task = convertParsedDetailsToTask();
			try {
				if (googleCalendar.isLoggedIn() && task.getHasStartDate()) {
					Event event = googleCalendar.convertNonFloatingTaskToEvent(task);
					String eventId = googleCalendar.syncAddNonFloatingTask(event);
					task.setEventId(eventId);
				}
			} catch (IOException e ) {
				return MESSAGE_SYNC_FAILURE;
			}
			storage.addTask(task);
			saveCurrentState();
			return DisplayHandler.displayTaskForAdd(task);
		} catch (IllegalArgumentException e) {
			TaskerLog.logSystemExceptionError(e.getMessage());
			return e.getMessage();
		}
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

			builder.setEndDate(date);
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