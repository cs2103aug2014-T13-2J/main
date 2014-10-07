package main.ui;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

import main.logic.CommandParser;

public class Tasker {
	
	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	public static final String MESSAGE_PROMPT = "Enter Command:";
	public static String[] args = null;
	public static String filename = args[0];

	public static void initializeEnvironment() throws IOException {
		System.out.println(MESSAGE_WELCOME);
		initializeFileForIO(filename);
	}
	
	public static void readAndExecuteCommands() throws IOException{
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.println(MESSAGE_PROMPT);
			String userCommand=scanner.nextLine();
			CommandParser.parse(userCommand);
		}
	}
	
	/*public static String readCmd(Scanner sc) {
		return null;
	}*/
	
	public static void initializeFileForIO(String userArgument) throws IOException{
		File file = new File(userArgument);
		if (!file.exists()) {
			file.createNewFile();
			}
			FileReader fileReader = new FileReader(filename); 
			BufferedReader reader = new BufferedReader(fileReader); 
			String textLine;
			ArrayList<String> textArray = new ArrayList<>();
			while ((textLine = reader.readLine()) != null) {
			textArray.add(textLine);
			}
			reader.close(); 
			fileReader.close();
	}
	

}
