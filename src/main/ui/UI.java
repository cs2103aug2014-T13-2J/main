package main.ui;

import java.io.IOException;

import main.TaskerLog;
import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;

import org.fusesource.jansi.AnsiConsole;

//@author A0100239W
/**
 * This class initializes Tasker to sync with Google Calendar and read in user
 * input.
 * 
 */
@SuppressWarnings("unused")
public class UI {

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	public static final String MESSAGE_PROMPT = "Enter command:";

	private static GoogleCalendar googleCalendar;
	private static TabCompletion tab = new TabCompletion();

	/**
	 * This method initializes Tasker's synchronization with Google Calendar
	 */
	public static void initializeEnvironment() {
		AnsiConsole.systemInstall();
		System.out.println(MESSAGE_WELCOME);
		readFromStorage();
		googleCalendar = GoogleCalendar.getInstance();
		TaskerLog.logSystemInfo("Tasker initialized.");
	}

	/**
	 * This method calls the tab completion class. The commented portion of the
	 * code is for running Tasker without tab completion.
	 * 
	 * @throws IOException
	 */
	public static void readAndExecuteCommands() throws IOException {
		// Comment the line below to disable tab completion.
		tab.run();

		/*
		 * @SuppressWarnings("resource") Scanner scanner = new
		 * Scanner(System.in); while (true) {
		 * System.out.println(MESSAGE_PROMPT); String userCommand =
		 * scanner.nextLine(); TaskerLog.logSystemInfo("User entered:" +
		 * userCommand); System.out.println(Logic.uiToLogic(userCommand)); }
		 */
	}

	/**
	 * This method reads from storage for previously entered tasks or deleted
	 * tasks.
	 */
	private static void readFromStorage() {
		Storage.readFromFile(Storage.DATABASE_FILENAME, Storage.getInstance()
				.getTasks());
		Storage.readFromFile(Storage.DELETED_TASKS_FILENAME, Storage
				.getInstance().getDeletedTasks());
		Storage storage = Storage.getInstance();
		storage.saveCurrentState();
	}

}
