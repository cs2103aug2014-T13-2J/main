package main.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.rits.cloning.Cloner;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Storage {
	private static final int EVENT_ID_INDEX = 0;
	private static final int DESCRIPTION_INDEX = 1;
	private static final int VENUE_INDEX = 2;
	private static final int START_DATE_INDEX = 3;
	private static final int START_TIME_INDEX = 4;
	private static final int END_DATE_INDEX = 5;
	private static final int END_TIME_INDEX = 6;
	private static final int REMINDER_INDEX = 7;
	private static final int RECURRENCE_INDEX = 8;
	private static final int COMPLETED_INDEX = 9;
	private static final String MESSAGE_READ_FROM_FILE_SUCCESS = "Data read from storage.";
	private static final String MESSAGE_WRITE_FROM_FILE_SUCCESS = "Tasks added.";
	private static final String MESSAGE_NO_MORE_COMMANDS_TO_UNDO = "There are no more commands to undo.";
	private static final String MESSAGE_FILE_NOT_FOUND = "File does not exist";
	private static final String FILENAME = "database.csv";
	private static final int DATE_INDEX = 0;
	private static final int TIME_INDEX = 1;
	private static final int YEAR_INDEX = 0;
	private static final int MONTH_INDEX = 1;
	private static final int DAY_INDEX = 2;
	private static final int HOUR_INDEX = 0;
	private static final int MINUTE_INDEX = 1;

	/***************************** Data Members ************************/
	private ArrayList<Task> tasks;
	private static Storage theOne = null;
	private LinkedList<ArrayList<Task>> taskHistory;

	/***************************** Constructors ************************/
	private Storage() {
		tasks = new ArrayList<Task>();
		taskHistory = new LinkedList<ArrayList<Task>>();
	}

	public static Storage getInstance() {
		if (theOne == null) {
			theOne = new Storage();
		}
		return theOne;
	}

	/***************************** Accessors ************************/
	public ArrayList<Task> getTasks() {
		return this.tasks;
	}

	public LinkedList<ArrayList<Task>> getTaskHistory() {
		return this.taskHistory;
	}

	/****************************************************************/
	public void addTask(Task task) {
		tasks.add(task);
	}

	public void deleteTask(int index) {
		tasks.remove(index);
	}

	public void clearAllTasks() {
		tasks.clear();
	}

	private void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public void updateTaskHistory() {
		Cloner cloner = new Cloner();
		ArrayList<Task> currentTasks = this.getTasks();
		ArrayList<Task> clone = cloner.deepClone(currentTasks);
		this.getTaskHistory().push(clone);
	}

	public void revertTaskHistory() {
		if (this.taskHistory.isEmpty()) {
			throw new IllegalArgumentException(MESSAGE_NO_MORE_COMMANDS_TO_UNDO);
		} else {
			LinkedList<ArrayList<Task>> taskHistory = this.getTaskHistory();
			taskHistory.pop();
			if (taskHistory.peek() == null) {
				this.setTasks(new ArrayList<Task>());
			} else {
				this.setTasks(taskHistory.peek());
			}
		}
	}

	public static String readFromFile() {
		Storage storage = Storage.getInstance();
		ArrayList<Task> tasks = storage.getTasks();
		File file = new File(FILENAME);

		if (!file.exists()) {
			return MESSAGE_READ_FROM_FILE_SUCCESS;
		}
		try {
			String[] nextLine;
			CSVReader reader = new CSVReader(new FileReader(FILENAME));

			String eventId;
			String description;
			String venue;
			LocalDate startDate;
			LocalTime startTime;
			LocalDate endDate;
			LocalTime endTime;
			DateTime reminder;
			String recurrence;
			boolean completed;

			while ((nextLine = reader.readNext()) != null) {
				eventId = nextLine[EVENT_ID_INDEX];
				description = nextLine[DESCRIPTION_INDEX];
				venue = nextLine[VENUE_INDEX];
				startDate = convertToDate(nextLine[START_DATE_INDEX]);
				startTime = convertToTime(nextLine[START_TIME_INDEX]);
				endDate = convertToDate(nextLine[END_DATE_INDEX]);
				endTime = convertToTime(nextLine[END_TIME_INDEX]);
				reminder = convertToDateTime(nextLine[REMINDER_INDEX]);
				recurrence = nextLine[RECURRENCE_INDEX];
				completed = convertToBoolean(nextLine[COMPLETED_INDEX]);

				tasks.add(new Task(eventId, description, venue, startDate,
						startTime, endDate, endTime, reminder, recurrence,
						completed));
			}

			reader.close();
		} catch (FileNotFoundException e) {
			return MESSAGE_FILE_NOT_FOUND;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return MESSAGE_READ_FROM_FILE_SUCCESS;
	}

	public static LocalDate convertToDate(String date) {
		// Date format example: 2014-12-25
		if (date.equals("null"))
			return null;
		String[] yearMonthDay = date.split("-");

		int yearInt = Integer.parseInt(yearMonthDay[YEAR_INDEX]);
		int monthInt = Integer.parseInt(yearMonthDay[MONTH_INDEX]);
		String dayString = yearMonthDay[DAY_INDEX];
		if (dayString.startsWith("0")) {
			dayString = dayString.substring(1, dayString.length());
		}
		int dayInt = Integer.parseInt(dayString);

		LocalDate result = new LocalDate(yearInt, monthInt, dayInt);
		return result;
	}

	public static LocalTime convertToTime(String time) {
		// Time format example: 20:59:00.000
		if (time.equals("null"))
			return null;
		String[] hourMinute = time.split(":");

		String hourString = hourMinute[HOUR_INDEX];
		String minuteString = hourMinute[MINUTE_INDEX];

		if (hourString.startsWith("0")) {
			hourString = hourString.substring(1, hourString.length());
		}
		if (minuteString.startsWith("0")) {
			minuteString = minuteString.substring(1, minuteString.length());
		}

		int hourInt = Integer.parseInt(hourString);
		int monthInt = Integer.parseInt(minuteString);

		LocalTime result = new LocalTime(hourInt, monthInt);
		return result;
	}

	public static DateTime convertToDateTime(String dateTime) {
		// DateTime format example: 2014-10-20T09:00:00.000+08:00
		if (dateTime.equals("null"))
			return null;
		String[] parameters = dateTime.split("T");
		String date = parameters[DATE_INDEX];
		String[] yearMonthDay = date.split("-");
		String time = parameters[TIME_INDEX];
		String[] hourMinuteSecond = time.split(":");

		int year = Integer.parseInt(yearMonthDay[YEAR_INDEX]);
		int month = Integer.parseInt(yearMonthDay[MONTH_INDEX]);
		String dayString = yearMonthDay[DAY_INDEX];
		if (dayString.substring(1) == "0") {
			dayString = dayString.substring(1, dayString.length() - 1);
		}
		int dayInt = Integer.parseInt(dayString);
		int hour = Integer.parseInt(hourMinuteSecond[HOUR_INDEX]);
		int minute = Integer.parseInt(hourMinuteSecond[MINUTE_INDEX]);

		DateTime result = new DateTime(year, month, dayInt, hour, minute);
		return result;
	}

	public static boolean convertToBoolean(String completed) {
		if (completed.equals("false")) {
			return false;
		} else if (completed.equals("true")) {
			return true;
		} else {
			System.out.println("Unexpected input in convertToBoolean method");
			return false;
		}
	}

	public static String writeToFile() {
		// this function assumes that the ArrayList containing tasks is fully
		// updated
		Storage storage = Storage.getInstance();
		ArrayList<Task> tasks = storage.getTasks();

		File file = new File(FILENAME);

		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			CSVWriter writer = new CSVWriter(new FileWriter(FILENAME));
			String[] currentEntry;
			for (Task task : tasks) {
				currentEntry = task.convertToCSVFormat().split("#!");
				writer.writeNext(currentEntry);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return MESSAGE_WRITE_FROM_FILE_SUCCESS;
	}

}
