import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.model.EventReminder;

class Task {
	
	/***************************** Data Members ************************/
	String description;
	String venue;
	DateTime startTime;
	DateTime endTime;
	DateTime startDate;
	DateTime endDate;
	EventReminder reminder;
	repeat;
	boolean completed;

	/***************************** Constructors ************************/
	
	
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
