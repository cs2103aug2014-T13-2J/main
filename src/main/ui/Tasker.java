package main.ui;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Tasker {
	public static String filename;
	public static ArrayList<String> textArray = new ArrayList<>();

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	public static final String MESSAGE_PROMPT = "Enter Command:";
	public static final String MESSAGE_EMPTY = "File is empty.";
	
	public static void initializeEnvironment() {
		System.out.println(MESSAGE_WELCOME);
	}
	
	public static void readAndExecuteCommands() throws IOException{
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.println(MESSAGE_PROMPT);
			String userCommand=scanner.nextLine();
			//CommandParser.getCommandDetails(userCommand);
		}
	}
	
	/*public static String readCmd(Scanner sc) {
		return null;
	}*/
	
	public static void initializeFileForIO(String userArgument) throws IOException{
		filename = userArgument;
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
			}
			FileReader fileReader = new FileReader(filename); 
			BufferedReader reader = new BufferedReader(fileReader); 
			String textLine;
			while ((textLine = reader.readLine()) != null) {
			textArray.add(textLine);
			}
			reader.close(); 
			fileReader.close();
	}
	
	public static void displayResult(){
		if(textArray.isEmpty()) {
			System.out.println(MESSAGE_EMPTY);
		}
		else {
			for (int i=0; i < textArray.size(); i++) { 
				System.out.println((i+1) + "." + textArray.get(i));
			} 
		}
	}	

}
