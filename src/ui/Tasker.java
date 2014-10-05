package ui;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import logic.CommandParser;


public class Tasker {
	
	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	
	public static void main(String[] args) {
		initializeEnvironment();
		readAndExecuteCommands();
	}
	
	
	public static void initializeEnvironment() {
		System.out.println(MESSAGE_WELCOME);
		filename=args[0];
		initializeFileForIO(filename);
	}
	
	public static void readAndExecuteCommands() throws IOException{
		scanner = new Scanner(System.in);
		while(true){
			System.out.println(MESSAGE_PROMPT);
			String userCommand=scanner.nextLine();
			CommandParser.parse(userCommand);
		}
	}
	
	/*public static String readCmd(Scanner sc) {
		return null;
	}*/
	
	public static void initializeFileForIO(String userArgument){
		File file = new File(userArgument);
		if (!file.exists()) {
			file.createNewFile();
			}
			FileReader fileReader = new FileReader(filename); 
			BufferedReader reader = new BufferedReader(fileReader); 
			String textLine;
			textArray = new ArrayList<>();
			while ((textLine = reader.readLine()) != null) {
			textArray.add(addedText);
			}
			reader.close(); 
			fileReader.close();
	}
	

}
