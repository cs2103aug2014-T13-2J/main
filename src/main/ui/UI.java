package main.ui;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;
import main.logic.Logic;

public class UI {
	public static String filename;

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	public static final String MESSAGE_PROMPT = "Enter Command:";
	public static final String MESSAGE_EMPTY = "File is empty.";
	
	public static void initializeEnvironment() throws IOException, URISyntaxException {
		TaskerLog.logSystemInfo("Tasker initialized.");
		System.out.println(MESSAGE_WELCOME);
		System.out.println(GoogleCalendar.logInToGoogleCalendar());
	}
	
	@SuppressWarnings("resource")
	public static void readAndExecuteCommands() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.println(MESSAGE_PROMPT);
			String userCommand = scanner.nextLine();
			TaskerLog.logSystemInfo("User entered:" + userCommand);
			System.out.println(Logic.uiToLogic(userCommand));
		}
	}

}
