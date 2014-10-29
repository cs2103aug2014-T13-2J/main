package main.ui;

import java.io.IOException;
import java.util.Scanner;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;
import main.logic.Logic;

public class UI {
	public static String filename;

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	public static final String MESSAGE_PROMPT = "Enter command:";

	public static void initializeEnvironment() {
		TaskerLog.logSystemInfo("Tasker initialized.");
		System.out.println(MESSAGE_WELCOME);
		System.out.println(GoogleCalendar.initialiseGoogleCalendar(true));
	}

	public static void readAndExecuteCommands() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println(MESSAGE_PROMPT);
			String userCommand = scanner.nextLine();
			TaskerLog.logSystemInfo("User entered:" + userCommand);
			System.out.println(Logic.uiToLogic(userCommand));
		}
	}

}
