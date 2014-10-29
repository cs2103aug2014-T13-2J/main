package main.ui;

import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;

public class UI {
	public static String filename;

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	private static TabCompletion tab = new TabCompletion();
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();

	public static void initializeEnvironment() {
		AnsiConsole.systemInstall();
		System.out.println(MESSAGE_WELCOME);
		System.out.println(googleCalendar.initialiseGoogleCalendar(true));
		Storage.readFromFile();
		TaskerLog.logSystemInfo("Tasker initialized.");
	}

	public static void readAndExecuteCommands() throws IOException {
		tab.run();
	}

}
