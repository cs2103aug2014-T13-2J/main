package main.logic;

import java.util.ArrayList;

import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;
import main.storage.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class UpdateHandler extends CommandHandler {

	private final static String MESSAGE_TASK_NOT_FOUND = "Sorry we couldn't find that task. Please try again.";

	private UpdateParser parser;
	private static Storage storage = Storage.getInstance();
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

	public UpdateHandler(String details) {
		super(details);
		parser = new UpdateParser(details);
	}

	@Override
	public String execute() {
		try {
			// parser will analyze the user input and store each piece of
			// information into its respective
			// attributes
			parser.parse();
			String field = parser.getField();
			Task task = getTask(parser.getTaskNumber());
			String eventId = task.getEventId();

			if (field.equals(UpdateParser.FIELD_DESCRIPTION)) {
				String newDescription = parser.getDescription();
				task.setDescription(newDescription);
				googleCalendar.syncUpdateTaskDescription(eventId,
						newDescription);
			} else if (field.equals(UpdateParser.FIELD_VENUE)) {
				String newVenue = parser.getVenue();
				task.setVenue(newVenue);
				googleCalendar.syncUpdateTaskVenue(eventId, newVenue);

			} else if (field.equals(UpdateParser.FIELD_START_DATE)) {
				updateStartDate(task, parser, eventId);
			} else if (field.equals(UpdateParser.FIELD_START_TIME)) {
				updateStartTime(task, parser, eventId);
			} else if (field.equals(UpdateParser.FIELD_END_DATE)) {
				updateEndDate(task, parser, eventId);
			} else if (field.equals(UpdateParser.FIELD_END_TIME)) {
				updateEndTime(task, parser, eventId);
			} else if (field.equals(UpdateParser.FIELD_RECURRENCE)) {
				updateRecurrence(task, parser);
			} else if (field.equals(UpdateParser.FIELD_REMINDER)) {
				updateReminder(task, parser);
			} else if (field.equals(UpdateParser.FIELD_COMPLETE)) {
				task.setCompleted(true);
			} else {
				task.setCompleted(false);
			}
			saveCurrentState();
			String successMessage = getSuccessMessage(parser);
			return successMessage;
		} catch (IndexOutOfBoundsException e) {
			return MESSAGE_TASK_NOT_FOUND;
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}

	public static Task getTask(Integer number) {
		ArrayList<Task> tasks = storage.getTasks();
		return tasks.get(number - 1);
	}

	private static void updateStartDate(Task task, UpdateParser parser,
			String eventId) {
		Integer startDateYear = Integer.parseInt(parser.getStartDateYear());
		Integer startDateMonth = Integer.parseInt(parser.getStartDateMonth());
		Integer startDateDay = Integer.parseInt(parser.getStartDateDay());

		LocalDate newStartDate = new LocalDate(startDateYear, startDateMonth,
				startDateDay);
		task.setStartDate(newStartDate);
		googleCalendar.syncUpdateTaskStartDate(eventId, newStartDate);
		
		if(task.startDateEqualsEndDate()) {
			task.setEndDate(newStartDate);
			googleCalendar.syncUpdateTaskEndDate(eventId, newStartDate);
		}
	}

	private static void updateEndDate(Task task, UpdateParser parser,
			String eventId) {
		Integer endDateYear = Integer.parseInt(parser.getEndDateYear());
		Integer endDateMonth = Integer.parseInt(parser.getEndDateMonth());
		Integer endDateDay = Integer.parseInt(parser.getEndDateDay());

		LocalDate newEndDate = new LocalDate(endDateYear, endDateMonth,
				endDateDay);
		task.setEndDate(newEndDate);
		googleCalendar.syncUpdateTaskEndDate(eventId, newEndDate);
	}

	private static void updateStartTime(Task task, UpdateParser parser,
			String eventId) {
		Integer startTimeHour = Integer.parseInt(parser.getStartTimeHour());
		Integer startTimeMinute = Integer.parseInt(parser.getStartTimeMinute());

		LocalTime newStartTime = new LocalTime(startTimeHour, startTimeMinute);
		
		task.setStartTime(newStartTime);
		googleCalendar.syncUpdateTaskStartTime(eventId, newStartTime);

		if(task.startTimeEqualsEndTime()) {
			task.setEndTime(newStartTime);
			googleCalendar.syncUpdateTaskEndTime(eventId, newStartTime);
		}
		
	}

	private static void updateEndTime(Task task, UpdateParser parser,
			String eventId) {
		int endTimeHour = Integer.parseInt(parser.getEndTimeHour());
		int endTimeMinute = Integer.parseInt(parser.getEndTimeMinute());

		LocalTime newEndTime = new LocalTime(endTimeHour, endTimeMinute);

		task.setEndTime(newEndTime);
		googleCalendar.syncUpdateTaskEndTime(eventId, newEndTime);
	}

	private static void updateRecurrence(Task task, UpdateParser parser) {
		task.setRecurrence(parser.getRecurrence());
	}

	private static void updateReminder(Task task, UpdateParser parser) {
		// don't know do what
	}

	private static String getSuccessMessage(UpdateParser parser) {
		String result;
		String field = parser.getField();
		if (field.equals(UpdateParser.FIELD_COMPLETE)) {
			result = "Task " + parser.getTaskNumber().toString()
					+ " marked completed!";
		} else if (field.equals(UpdateParser.FIELD_INCOMPLETE)) {
			result = "Task " + parser.getTaskNumber().toString()
					+ " marked incomplete!";
		} else {
			result = "Task " + parser.getTaskNumber().toString() + "'s "
					+ parser.getField() + " updated!";
		}
		return result;
	}
}
