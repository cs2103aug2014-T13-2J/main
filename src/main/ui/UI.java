package main.ui;

import java.io.IOException;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;

public class UI {
	public static String filename;

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	private static TabCompletion tab = new TabCompletion();

	public static void initializeEnvironment() {
		TaskerLog.logSystemInfo("Tasker initialized.");
		// WelcomeScreen.screen();
		System.out.println(MESSAGE_WELCOME);
		System.out.println(GoogleCalendar.initialiseGoogleCalendar(true));
	}

	public static void readAndExecuteCommands() throws IOException {
		tab.run();
	}

}
