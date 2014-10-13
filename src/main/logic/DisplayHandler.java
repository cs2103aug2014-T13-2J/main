package main.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import main.storage.Task;

public class DisplayHandler extends CommandHandler {
	public static final String MESSAGE_EMPTY = "There is nothing to display";
	public static final String DISPLAY_SUCCESS_MESSAGE = "Display successful.";
	public static final Integer TIME_STRING_START_INDEX = 0;
	public static final Integer TIME_STRING_END_INDEX = 5;
	public static final String SPACE = " ";

	public DisplayHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		ArrayList<Task> tasks = getCurrentTaskList();
		if (tasks.isEmpty()) {
			System.out.println(MESSAGE_EMPTY);
		} else {
			for (int i = 0; i < tasks.size(); i++) {
				displayTask(i,tasks.get(i));
			}
		}
		return DISPLAY_SUCCESS_MESSAGE;
	}
	
	public static void displayTask(int number, Task task) {
		String result = "";
		result += getTaskNumber(number);
		result += getDescription(task);
		result += getVenue(task);
		// if a task has a start date, it will definitely have an end date, it
		// just depends whether they are equal
		if (task.getHasStartDate() && !startDateEqualsEndDate(task)) { // append "from.. ..to"
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
		result.trim();
		System.out.println(result);
	}

	private static String getTaskNumber(int number) {
		return number + 1 + "." + SPACE;
	}

		private static String getDescription(Task task) {
		return task.getDescription() + SPACE;
	}

	private static String getVenue(Task task) {
		String venue = task.getVenue();
		if (venue != null) {
			return "at " + task.getVenue() + SPACE;
		} else {
			return "";
		}
	}

	private static String addFrom() {
		return "from" + SPACE;
	}

	private static String addTo() {
		return "to" + SPACE;
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
		return task.getStartDate() + SPACE;
	}

	private static String addStartTime(Task task) {
		return task.getStartTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ SPACE;
	}

	private static String addEndDate(Task task) {
		return task.getEndDate() + SPACE;
	}

	private static String addEndTime(Task task) {
		return task.getEndTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ SPACE;
	}

	private static String addOn(Task task) {
		return "on" + SPACE;
	}

	private static String addAt(Task task) {
		return "at" + SPACE;
	}

	private static String addRecurrence(Task task) {
		return "(" + task.getRecurrence() + ")" + SPACE;
	}

	private static String addCompleted(Task task) {
		return "(" + task.getCompleted() + ")" + SPACE;
	}

}