package main.logic;

import java.util.ArrayList;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import main.storage.Storage;
import main.storage.Task;

public class UpdateHandler extends CommandHandler {

	private final static String MESSAGE_TASK_NOT_FOUND = "Sorry we couldn't find that task. Please try again.";

	private UpdateParser parser;
	private static Storage storage = Storage.getInstance();

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

			if (field.equals(UpdateParser.FIELD_DESCRIPTION)) {
				task.setDescription(parser.getDescription());
			} else if (field.equals(UpdateParser.FIELD_VENUE)) {
				task.setVenue(parser.getVenue());
			} else if (field.equals(UpdateParser.FIELD_START_DATE)) {
				updateStartDate(task, parser);
			} else if (field.equals(UpdateParser.FIELD_START_TIME)) {
				updateStartTime(task, parser);
			} else if (field.equals(UpdateParser.FIELD_END_DATE)) {
				updateEndDate(task, parser);
			} else if (field.equals(UpdateParser.FIELD_END_TIME)) {
				updateEndTime(task, parser);
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

		} catch (IllegalArgumentException e) {
			return e.getMessage();
		} catch (IndexOutOfBoundsException e) {
			return MESSAGE_TASK_NOT_FOUND;
		}
	}

	public static Task getTask(Integer number) {
		Storage storage = Storage.getInstance();
		ArrayList<Task> tasks = storage.getTasks();
		return tasks.get(number - 1);
	}

	private static void updateStartDate(Task task, UpdateParser parser) {
		Integer startDateYear = Integer.parseInt(parser.getStartDateYear());
		Integer startDateMonth = Integer.parseInt(parser.getStartDateMonth());
		Integer startDateDay = Integer.parseInt(parser.getStartDateDay());

		LocalDate newStartDate = new LocalDate(startDateYear, startDateMonth,
				startDateDay);
		task.setStartDate(newStartDate);
	}

	private static void updateEndDate(Task task, UpdateParser parser) {
		Integer endDateYear = Integer.parseInt(parser.getEndDateYear());
		Integer endDateMonth = Integer.parseInt(parser.getEndDateMonth());
		Integer endDateDay = Integer.parseInt(parser.getEndDateDay());

		LocalDate newEndDate = new LocalDate(endDateYear, endDateMonth,
				endDateDay);
		task.setEndDate(newEndDate);
	}

	private static void updateStartTime(Task task, UpdateParser parser) {
		Integer startTimeHour = Integer.parseInt(parser.getStartTimeHour());
		Integer startTimeMinute = Integer.parseInt(parser.getStartTimeMinute());

		LocalTime newStartTime = new LocalTime(startTimeHour, startTimeMinute);

		task.setStartTime(newStartTime);
	}

	private static void updateEndTime(Task task, UpdateParser parser) {
		int endTimeHour = Integer.parseInt(parser.getEndTimeHour());
		int endTimeMinute = Integer.parseInt(parser.getEndTimeMinute());

		LocalTime newEndTime = new LocalTime(endTimeHour, endTimeMinute);

		task.setEndTime(newEndTime);
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
		if(field.equals(UpdateParser.FIELD_COMPLETE)) {
			result = "Task " + parser.getTaskNumber().toString() + " marked completed!"; 
		} else if (field.equals(UpdateParser.FIELD_INCOMPLETE)) {
			result = "Task " + parser.getTaskNumber().toString() + " marked incomplete!";
		} else {
			result = "Task " + parser.getTaskNumber().toString() + "'s " + parser.getField() + " updated!";
		}
		return result;
	}
}
