import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.model.EventDateTime;

class TaskBuilder {
	
	/***************************** Data Members ************************/
	private String description;
	private String venue;
	private EventDateTime startTime=null;
	private EventDateTime endTime=null;
	private EventDateTime startDate=null;
	private EventDateTime endDate=null;
	private boolean hasReminder=false;
	private Reminders reminder=null;
	private String recurringEventId=null;
	private boolean hasRecurrence=false;
	private String recurrence=null;
	private boolean completed=false;

	/***************************** Constructors ************************/
	public TaskBuilder() {
		
	}
	
	public TaskBuilder setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public TaskBuilder setVenue(String venue) {
		this.venue = venue;
		return this;
	}
	
	public TaskBuilder setStartTime(EventDateTime startTime) {
		this.startTime = startTime;
		return this;
	}
	
	public TaskBuilder setEndTime(EventDateTime endTime) {
		this.endTime = endTime;
		return this;
	}
	
	public TaskBuilder setStartDate(EventDateTime startDate) {
		this.startDate = startDate;
		return this;
	}
	
	public TaskBuilder setEndDate(EventDateTime endDate) {
		this.endDate = endDate;
		return this;
	}
	
	public TaskBuilder setHasReminder(boolean hasReminder) {
		this.hasReminder = hasReminder;
		return this;
	}
	
	public TaskBuilder setReminder(Reminders reminder) {
		this.reminder = reminder;
		return this;
	}
	
	public TaskBuilder setRecurringEventId(String recurringEventId) {
		this.recurringEventId = recurringEventId;
		return this;
	}
	
	public TaskBuilder setHasRecurrence(boolean hasRecurrence) {
		this.hasRecurrence = hasRecurrence;
		return this;
	}
	
	public TaskBuilder setRecurrence(String recurrence) {
		this.recurrence = recurrence;
		return this;
	}
	
	public TaskBuilder setCompleted(boolean completed) {
		this.completed = completed;
		return this;
	}
	
	public Task buildTask() {
		return new Task(description, venue, startTime, endTime, startDate, endDate, hasReminder, 
				reminder, recurringEventId, hasRecurrence, recurrence, completed);
}

class Task {
	
	/***************************** Data Members ************************/
	private String description;
	private String venue;
	private EventDateTime startTime;
	private EventDateTime endTime;
	private EventDateTime startDate;
	private EventDateTime endDate;
	private boolean hasReminder;
	private Reminders reminder;
	private String recurringEventId;
	private boolean hasRecurrence;
	private String recurrence;
	private boolean completed;

	/***************************** Constructors ************************/
	public Task (
			String description,
			String venue,
			EventDateTime startTime,
			EventDateTime endTime,
			EventDateTime startDate,
			EventDateTime endDate,
			boolean hasReminder,
			Reminders reminder,
			String recurringEventId,
			boolean hasRecurrence,
			String recurrence,
			boolean completed
			) {
		this.description = description;
		this.venue = venue;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hasReminder = hasReminder;
		this.reminder = reminder;
		this.recurringEventId = recurringEventId;
		this.hasRecurrence = hasRecurrence;
		this.recurrence = recurrence;
		this.completed = completed;
	}
	
	/***************************** Accessors ************************/
	
	
	/***************************** Mutators ************************/
	
}

public class Tasker {
	
	public static void main(String[] args) {
		initializeEnvironment();
		readAndExecuteCommands();
	}
	
	public static void initializeEnvironment() {
		
	}
	
	public static void readAndExecuteCommands() {
		
	}
	
	public static String readCmd(Scanner sc) {
		
	}
	
	public static String interpretCmd(String userInput) {
		
	}
	
	public static void executeCmd(String cmdType, String details) {
		
	}
	
	public static String addTask(String details) {
		
	}

	public static String deleteTask(String number) {
		
	}
	
	public static String updateTask(String details) {
		
	}
	
	public static String undo() {
		
	}
	
	public static String markAsComplete(String number) {
		
	}
	
	public static String searchTask(String searchString) {
		
	}
	
	public static String writeToFile() {
		
	}
	
	public static void writeToFile(String fileName, ArrayList<Task> tasks) {
		File file = new File(fileName);

		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			PrintWriter printWriter = new PrintWriter(file);
			String text;
			for (int i = 0; i < tasks.size(); i++) {
				text = i + 1 + ". " + tasks.get(i);
				printWriter.println(text);
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
