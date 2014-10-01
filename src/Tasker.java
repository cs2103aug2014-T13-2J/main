import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Tasker {
	
	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	
	public static void main(String[] args) {
		initializeEnvironment();
		readAndExecuteCommands();
	}
	
	public static void initializeEnvironment() {
		System.out.println(MESSAGE_WELCOME);
	}
	
	public static void readAndExecuteCommands() {
		
	}
	
	public static String readCmd(Scanner sc) {
		return null;
	}
	
	public static String interpretCmd(String userInput) {
		return null;
	}
	
	public static void executeCmd(String cmdType, String details) {
		
	}
	
	public static String addTask(String details) {
		return null;
	}

	public static String deleteTask(String number) {
		return null;
	}
	
	public static String updateTask(String details) {
		return null;
	}
	
	public static String undo() {
		return null;
	}
	
	public static String markAsComplete(String number) {
		return null;
	}
	
	public static String searchTask(String searchString) {
		return null;
	}
	
	public static String writeToFile() {
		return null;
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
