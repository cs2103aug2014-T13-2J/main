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
	private static final int VENUE_INDEX = 0;
	private static final int START_DATE_TIME_INDEX = 0;
	private static final int END_DATE_TIME_INDEX = 0;
	private static final int REMINDER_INDEX = 0;
	private static final int RECURRENCE_INDEX = 0;
	private static final int COMPLETED_INDEX = 0;

	public static String readFromFile(String fileName, ArrayList<Task> tasks) {
		if(!tasks.isEmpty()) {
			return "Storage arraylist is not empty. Data will be overwritten. Operation discontinued.";
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
		    
		    return "Data read from storage.";
		}
	}
	
	public static DateTime convertToDateTime(String dateTime) {
		return null;
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
	
	public static String writeToFile(String fileName, ArrayList<Task> tasks) {
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
		
		return "Tasks added.";
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
