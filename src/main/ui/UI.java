package main.ui;

import java.io.IOException;
import java.net.URISyntaxException;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;

public class UI {
	public static String filename;

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	private static TabCompletion tab = new TabCompletion();

	public static void initializeEnvironment() throws IOException,
			URISyntaxException {
		TaskerLog.logSystemInfo("Tasker initialized.");
		System.out.println(MESSAGE_WELCOME);
		System.out.println(GoogleCalendar.logInToGoogleCalendar());
	}

	public static void readAndExecuteCommands() throws IOException {
		tab.run();
	}

}
