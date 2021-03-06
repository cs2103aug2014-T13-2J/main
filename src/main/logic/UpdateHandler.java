package main.logic;

import java.io.IOException;
import java.util.ArrayList;

import main.googlecalendar.GoogleCalendar;
import main.googlecalendar.SyncNonFloatingTasks;
import main.storage.Storage;
import main.storage.Task;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class UpdateHandler extends CommandHandler {

	private final static String MESSAGE_TASK_NOT_FOUND = "Sorry we couldn't find that task. Please try again.";
	private final static String MESSAGE_CANNOT_UPDATE_VENUE_FOR_FLOATING_TASK = "Sorry you can't update the venue field for a floating task.";
	private final static String MESSAGE_CANNOT_UPDATE_DATE_FOR_FLOATING_TASK = "Sorry you can't update the date field for a floating task.";
	private final static String MESSAGE_CANNOT_UPDATE_TIME_FOR_FLOATING_TASK = "Sorry you can't update the time field for a floating task.";

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

			if (field.equals(UpdateParser.FIELD_DESCRIPTION_FULL)) {
				String newDescription = parser.getDescription();
				task.setDescription(newDescription);
				googleCalendar.syncUpdateTaskDescription(task, newDescription);
			} else if (field.equals(UpdateParser.FIELD_VENUE_FULL)) {
				if (isFloatingTask(task)) {
					throw new IllegalArgumentException(
							MESSAGE_CANNOT_UPDATE_VENUE_FOR_FLOATING_TASK);
				}
				String newVenue = parser.getVenue();
				task.setVenue(newVenue);
				SyncNonFloatingTasks.syncUpdateTaskVenue(task, newVenue);

			} else if (field.equals(UpdateParser.FIELD_START_DATE)) {
				if (isFloatingTask(task)) {
					throw new IllegalArgumentException(
							MESSAGE_CANNOT_UPDATE_DATE_FOR_FLOATING_TASK);
				}
				updateStartDate(task, parser);
			} else if (field.equals(UpdateParser.FIELD_START_TIME)) {
				if (isFloatingTask(task)) {
					throw new IllegalArgumentException(
							MESSAGE_CANNOT_UPDATE_TIME_FOR_FLOATING_TASK);
				}
				updateStartTime(task, parser);
			} else if (field.equals(UpdateParser.FIELD_END_DATE)) {
				if (isFloatingTask(task)) {
					throw new IllegalArgumentException(
							MESSAGE_CANNOT_UPDATE_DATE_FOR_FLOATING_TASK);
				}
				updateEndDate(task, parser);
			} else if (field.equals(UpdateParser.FIELD_END_TIME)) {
				if (isFloatingTask(task)) {
					throw new IllegalArgumentException(
							MESSAGE_CANNOT_UPDATE_TIME_FOR_FLOATING_TASK);
				}
				updateEndTime(task, parser);
			}
			storage.saveCurrentState();
			String successMessage = getSuccessMessage(parser);
			return successMessage;
		} catch (IndexOutOfBoundsException e) {
			return MESSAGE_TASK_NOT_FOUND;
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	public static Task getTask(Integer number) {
		ArrayList<Task> tasks = storage.getTasks();
		return tasks.get(number - 1);
	}

	private static void updateStartDate(Task task, UpdateParser parser) {
		try {
			Integer startDateYear = Integer.parseInt(parser.getStartDateYear());
			Integer startDateMonth = Integer.parseInt(parser
					.getStartDateMonth());
			Integer startDateDay = Integer.parseInt(parser.getStartDateDay());

			LocalDate newStartDate = new LocalDate(startDateYear,
					startDateMonth, startDateDay);
			task.setStartDate(newStartDate);
			SyncNonFloatingTasks.syncUpdateTaskStartDate(task, newStartDate);

			if (task.startDateEqualsEndDate()) {
				task.setEndDate(newStartDate);
			}

		} catch (IllegalFieldValueException e) {
			throw new IllegalArgumentException(MESSAGE_INVALID_DATE_VALUE);
		}
	}

	private static void updateEndDate(Task task, UpdateParser parser) {
		try {
			Integer endDateYear = Integer.parseInt(parser.getEndDateYear());
			Integer endDateMonth = Integer.parseInt(parser.getEndDateMonth());
			Integer endDateDay = Integer.parseInt(parser.getEndDateDay());

			LocalDate newEndDate = new LocalDate(endDateYear, endDateMonth,
					endDateDay);
			task.setEndDate(newEndDate);
			SyncNonFloatingTasks.syncUpdateTaskEndDate(task, newEndDate);

		} catch (IllegalFieldValueException e) {
			throw new IllegalArgumentException(MESSAGE_INVALID_DATE_VALUE);
		}
	}

	private static void updateStartTime(Task task, UpdateParser parser)
			throws IOException {
		try {
			Integer startTimeHour = Integer.parseInt(parser.getStartTimeHour());
			Integer startTimeMinute = Integer.parseInt(parser
					.getStartTimeMinute());

			LocalTime newStartTime = new LocalTime(startTimeHour,
					startTimeMinute);

			task.setStartTime(newStartTime);
			try {
				SyncNonFloatingTasks.syncUpdateTaskStartTime(task, newStartTime);
			} catch (IOException e) {
				// do nothing
			}
			try {
				if (task.startTimeEqualsEndTime()) {
					task.setEndTime(newStartTime);
					SyncNonFloatingTasks.syncUpdateTaskEndTime(task, newStartTime);
				}
			} catch (IOException e) {
				
			}
		} catch (IllegalFieldValueException e) {
			throw new IllegalArgumentException(MESSAGE_INVALID_TIME_VALUE);
		}
	}

	private static void updateEndTime(Task task, UpdateParser parser) {
		try {
			int endTimeHour = Integer.parseInt(parser.getEndTimeHour());
			int endTimeMinute = Integer.parseInt(parser.getEndTimeMinute());

			LocalTime newEndTime = new LocalTime(endTimeHour, endTimeMinute);

			task.setEndTime(newEndTime);
			SyncNonFloatingTasks.syncUpdateTaskEndTime(task, newEndTime);
		} catch (IllegalFieldValueException e) {
			throw new IllegalArgumentException(MESSAGE_INVALID_TIME_VALUE);
		} catch (IOException e) {
			
		}
	}

	private static String getSuccessMessage(UpdateParser parser) {
		String result;
		result = "Task " + parser.getTaskNumber().toString() + "'s "
					+ parser.getField() + " updated!";
		return result;
	}

	private static boolean isFloatingTask(Task task) {
		if ((task.hasStartDate() && task.hasEndDate())
				|| (task.hasStartTime() && task.hasEndTime())) {
			return false;
		} else {
			return true;
		}
	}
}
