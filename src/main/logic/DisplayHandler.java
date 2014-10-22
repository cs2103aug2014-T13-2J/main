package main.logic;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class DisplayHandler extends CommandHandler {

	public static final String DISPLAY_NUM_OF_TASKS = "Total number of tasks: %d\n";
	public static final String DISPLAY_TABLE_ROW_STRING_FORMAT = "%-10s %-35s %-30s %-25s %-20s\n";
	public static final String DISPLAY_TABLE_ROW_STRING_FORMAT_EXTRA = "%-3s %-35s %-30s %-15s %-10s\n";
	public static final String MESSAGE_EMPTY = "There is nothing to display";
	public static final String MESSAGE_DISPLAY_SUCCESS = "Display successful.";
	public static final Integer TIME_STRING_START_INDEX = 0;
	public static final Integer TIME_STRING_END_INDEX = 5;
	public static final String STRING_SPACE = " ";
	public static final String MESSAGE_ADDED = " added!";

	private static String result;
	private static Storage storage = Storage.getInstance();

	public DisplayHandler(String details) {
		super(details);
	}

	@Override
	public String execute() {
		ArrayList<Task> tasks = storage.getTasks();
		result = "";
		if (tasks.isEmpty()) {
			System.out.println(MESSAGE_EMPTY);
		} else {
			int numberOfTasks = tasks.size();
			result = String.format(DISPLAY_NUM_OF_TASKS, numberOfTasks);

			result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset(),
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi()
							.fg(CYAN).a(" VENUE").reset(),
					ansi().fg(BLUE).a(" TIME").reset(),
					ansi().fg(GREEN).a(" DATE").reset());
			result += displayLineSeparator();
			for (int j = 0; j < tasks.size(); j++) {
				result += displayTaskInTable(j, tasks.get(j));
			}
			result += displayLineSeparator();
		}
		return result;
	}

	private static String displayLineSeparator() {
		String lineString = "";
		for (int i = 0; i < 88; i++) {
			lineString += "-";
		}
		lineString += "\n";
		return lineString;
	}

	public static String displayTaskInTable(int number, Task task) {
		result = "";
		String taskNumber = getTaskNumber(number);
		String taskDescription = task.getDescription();
		String taskDescriptionExtra = "";
		if (taskDescription.length() >= 25) {
			taskDescriptionExtra = taskDescription.substring(24);
			taskDescription = taskDescription.substring(0, 24);
		}

		String taskVenue = "-";
		String taskVenueExtra = "";
		if (task.getHasVenue()) {
			taskVenue = task.getVenue();
			if (taskVenue.length() >= 15) {
				taskVenueExtra = taskVenue.substring(14);
				taskVenue = taskVenue.substring(0, 14);
			}
		}

		String taskTime = "-";
		if (task.getHasStartTime()) {
			taskTime = addStartTime(task);
			if (!task.getEndTime().equals(task.getStartTime())) {
				taskTime += "to " + addEndTime(task);
			}
		}

		String taskDate = "-";
		if (task.getHasStartDate()) {
			taskDate = addStartDate(task);
			if (!task.getEndDate().equals(task.getStartDate())) {
				taskDate += "to " + addEndDate(task);
			}
		}

		result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
				.a(taskNumber).reset(), ansi().fg(MAGENTA).a(taskDescription)
				.reset(), ansi().fg(CYAN).a(taskVenue).reset(), ansi().fg(BLUE)
				.a(taskTime).reset(), ansi().fg(GREEN).a(taskDate).reset());

		if (!taskDescriptionExtra.isEmpty() || !taskVenueExtra.isEmpty()) {

			result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT_EXTRA, "", ansi()
					.fg(MAGENTA).a(taskDescriptionExtra).reset(),
					ansi().fg(CYAN).a(taskVenueExtra).reset(), "", "");
		}
		return result;
	}

	public static String displayTaskForAdd(Task task) {
		result = "";
		result = result + displayTask(task) + MESSAGE_ADDED;
		return result;
	}

	public static String displayTask(Task task) {
		result = "";
		result += getDescription(task);
		result += getVenue(task);
		// if a task has a start date, it will definitely have an end date, it
		// just depends whether they are equal
		if (task.getHasStartDate() && !startDateEqualsEndDate(task)) { // append
																		// "from.. ..to"
			result += addFrom();
			result += addStartDate(task);
			if (task.getHasStartTime()) {
				result += addStartTime(task);
			}
			result += addTo();
			result += addEndDate(task);
			if (task.getHasEndTime()) {
				result += addEndTime(task);
			}
		} else if (task.getHasStartDate()) {
			result += addOn(task);
			result += addStartDate(task);
			if (task.getHasStartTime()) {
				result += addAt(task);
				result += addStartTime(task);
			}
		} else if (task.getHasStartTime()) {
			result += addAt(task);
			result += addStartTime(task);
		}

		if (task.getHasRecurrence()) {
			result += addRecurrence(task);
		}

		if (task.getCompleted()) {
			result += addCompleted(task);
		}
		result = result.trim();
		return result;
	}

	private static String getTaskNumber(int number) {
		return number + 1 + "." + STRING_SPACE;
	}

	private static String getDescription(Task task) {
		return (task.getDescription() + STRING_SPACE);
	}

	private static String getVenue(Task task) {
		String venue = task.getVenue();
		if (venue != null) {
			return "at " + task.getVenue() + STRING_SPACE;
		} else {
			return "";
		}
	}

	private static String addFrom() {
		return "from" + STRING_SPACE;
	}

	private static String addTo() {
		return "to" + STRING_SPACE;
	}

	// this function assumes that startDate and endDate will never be null
	private static boolean startDateEqualsEndDate(Task task)
			throws IllegalArgumentException {
		LocalDate startDate = task.getStartDate();
		LocalDate endDate = task.getEndDate();
		DateTime d1 = startDate.toDateTimeAtStartOfDay();
		DateTime d2 = endDate.toDateTimeAtStartOfDay();
		int compareResult = DateTimeComparator.getDateOnlyInstance().compare(
				d1, d2);
		switch (compareResult) {
		case -1: // startDate is earlier than endDate
			return false;
		case 0: // startDate equals endDate
			return true;
		default: // we should never reach this case
			throw new IllegalArgumentException(
					"Unexpected outcome in startDateEqualsEndDate function in DisplayHandler.");
		}
	}

	private static String addStartDate(Task task) {
		return task.getStartDate() + STRING_SPACE;
	}

	private static String addStartTime(Task task) {
		return task.getStartTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ STRING_SPACE;
	}

	private static String addEndDate(Task task) {
		return task.getEndDate() + STRING_SPACE;
	}

	private static String addEndTime(Task task) {
		return task.getEndTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ STRING_SPACE;
	}

	private static String addOn(Task task) {
		return "on" + STRING_SPACE;
	}

	private static String addAt(Task task) {
		return "at" + STRING_SPACE;
	}

	private static String addRecurrence(Task task) {
		return "(" + task.getRecurrence() + ")" + STRING_SPACE;
	}

	private static String addCompleted(Task task) {
		return "(" + task.getCompleted() + ")" + STRING_SPACE;
	}

}