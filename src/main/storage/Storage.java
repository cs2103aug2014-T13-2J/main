package main.storage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class Storage {
	
	private static final int DESCRIPTION_INDEX = 0;
	private static final int VENUE_INDEX = 1;
	private static final int START_DATE_TIME_INDEX = 2;
	private static final int END_DATE_TIME_INDEX = 3;
	private static final int REMINDER_INDEX = 4;
	private static final int RECURRENCE_INDEX = 5;
	private static final int COMPLETED_INDEX = 6;
	private static final String DATA_OVERWRITTEN_MESSAGE = "Storage arraylist is not empty. Data will be overwritten. Operation discontinued.";
	private static final String READ_FROM_FILE_SUCCESS_MESSAGE = "Data read from storage.";
	private static final String WRITE_FROM_FILE_SUCCESS_MESSAGE = "Tasks added."; 
	private static final int DATE_INDEX = 0;
	private static final int TIME_INDEX = 1;
	private static final int YEAR_INDEX = 0;
	private static final int MONTH_INDEX = 1;
	private static final int DAY_INDEX = 2;
	private static final int HOUR_INDEX = 0;
	private static final int MINUTE_INDEX = 1;
	
	private ArrayList<Task> tasks;
	private static Storage theOne = null;
	
	public static Storage getInstance() {
		if (theOne == null) {
			theOne = new Storage();
		}

		return theOne;
	}
	
	public Storage () {
		tasks = new ArrayList<Task>();
	}
	
	public String readFromFile(String fileName) {
		if(!tasks.isEmpty()) {
			return DATA_OVERWRITTEN_MESSAGE;
		} else {
		    try {
		    	String[] nextLine;
				CSVReader reader = new CSVReader(new FileReader(fileName));
				
				String description;
				String venue;
				DateTime startDateTime;
				DateTime endDateTime;
				DateTime reminder;
				String recurrence;
				boolean completed;
				
				while((nextLine = reader.readNext()) != null) {
					description = nextLine[DESCRIPTION_INDEX];
					venue = nextLine[VENUE_INDEX];
					startDateTime = convertToDateTime(nextLine[START_DATE_TIME_INDEX]);
					endDateTime = convertToDateTime(nextLine[END_DATE_TIME_INDEX]);
					reminder = convertToDateTime(nextLine[REMINDER_INDEX]);
					recurrence = nextLine[RECURRENCE_INDEX];
					completed = convertToBoolean(nextLine[COMPLETED_INDEX]);
					
					tasks.add(new Task(description, venue, startDateTime, endDateTime, reminder, recurrence, completed));
				}
				
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    return READ_FROM_FILE_SUCCESS_MESSAGE;
		}
	}
	
	public static DateTime convertToDateTime(String dateTime) {
		//DateTime format example: 2014-10-20T09:00:00.000+08:00
		if(dateTime.equals("null"))
			return null;
		String[] parameters = dateTime.split("T");
		String date = parameters[DATE_INDEX];
		String[] yearMonthDay = date.split("-");
		String time = parameters[TIME_INDEX];
		String[] hourMinuteSecond = time.split(":");

		int year = Integer.parseInt(yearMonthDay[YEAR_INDEX]);
		int month = Integer.parseInt(yearMonthDay[MONTH_INDEX]);
		String dayString = yearMonthDay[DAY_INDEX];
		if(dayString.substring(1) == "0") {
			dayString = dayString.substring(1, dayString.length()-1);
		}
		int dayInt = Integer.parseInt(dayString); 
		int hour = Integer.parseInt(hourMinuteSecond[HOUR_INDEX]);
		int minute = Integer.parseInt(hourMinuteSecond[MINUTE_INDEX]);
		
		DateTime result = new DateTime(year, month, dayInt, hour, minute);
		return result;
	}
	
	public static boolean convertToBoolean(String completed) {
		if(completed.equals("false")) {
			return false;
		} else if (completed.equals("true")) {
			return true;
		} else {
			System.out.println("Unexpected input in convertToBoolean method");
			return false;
		}
	}
	
	public String writeToFile(String fileName) {
		//this function assumes that the ArrayList containing tasks is fully updated
		File file = new File(fileName);

		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			CSVWriter writer = new CSVWriter(new FileWriter(fileName));
			String[] currentEntry;
			for(Task task: tasks) {
				currentEntry = task.convertToCSVFormat().split("#!"); 
				writer.writeNext(currentEntry);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return WRITE_FROM_FILE_SUCCESS_MESSAGE;
	}
	
	public ArrayList<Task> getTasks() {
		return this.tasks;
	}
	
	public static String add(String details) {
		return null;
	}
	
	public static String delete(String number) {
		return null;
	}
	
	public static String update(String details) {
		return null;
	}
	
	public static String search(String searchString) {
		return null;
	}
}
