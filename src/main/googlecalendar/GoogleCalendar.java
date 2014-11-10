package main.googlecalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import main.storage.Storage;
import main.storage.Task;

import com.google.api.services.calendar.model.Event;

//@author A0072744A
/**
 * This class represents the Google Calendar component. The synchronisation of
 * all tasks is handled here.
 */
@SuppressWarnings("unused")
public class GoogleCalendar {

	private static String MESSAGE_SYNC_SUCCESS = "You have successfully synchronised all changes to Google.";
	private static String MESSAGE_SYNC_FAIL = "Sorry, an error has occurred while synchronising to Google. Please check your Internet connection.";
	private static String MESSAGE_SYNC_EMPTY = "Your Google Calendar is in sync.";
	private static String OPERATING_SYSTEM = System.getProperty("os.name")
			.toLowerCase();

	private static long SLEEP_DURATION_IF_ONLINE = 10000; // 10 seconds
	private static long SLEEP_DURATION_IF_OFFLINE = 5000; // 5 seconds

	private static GoogleCalendar theOne = null;
	private static LoginToGoogle loginToGoogle;
	private static Storage storage;
	private Runnable syncUnsyncedTasksRunnable;
	private boolean isRunning;

	/**
	 * This constructor initalises LoginToGoogle and the thread for background
	 * synchronisation.
	 */
	private GoogleCalendar() {
		storage = Storage.getInstance();
		loginToGoogle = LoginToGoogle.getInstance();
		setupSyncRunnable();
		Thread autoSyncThread = new Thread(syncUnsyncedTasksRunnable);
		autoSyncThread.setDaemon(true);
		autoSyncThread.start();
	}

	/**
	 * This method creates the singleton for GoogleCalendar class and ensures
	 * that the GoogleCalendar constructor is not called more than once.
	 * 
	 * @return GoogleCalendar constructor
	 */
	public static GoogleCalendar getInstance() {
		if (theOne == null) {
			theOne = new GoogleCalendar();
		}
		return theOne;
	}

	/**
	 * This method sets up the Runnable to run the synchronisation in the
	 * background thread.
	 */
	private void setupSyncRunnable() {
		syncUnsyncedTasksRunnable = new Runnable() {

			@Override
			public void run() {
				isRunning = true;
				while (isRunning) {
					long sleepDuration;
					if (isConnectedToInternet()) {
						sleepDuration = SLEEP_DURATION_IF_ONLINE;
						syncToGoogle();
					} else {
						sleepDuration = SLEEP_DURATION_IF_OFFLINE;
					}
					try {
						Thread.sleep(sleepDuration);
					} catch (InterruptedException e) {
						isRunning = false;
					}
				}
			}
		};
	}

	/**
	 * This method terminates the background thread.
	 */
	public void killSyncThread() {
		isRunning = false;
	}

	/**
	 * This method synchronises the adding of a task to Google Calendar.
	 * 
	 * @param task
	 * @return the task ID provided by Google Calendar or Google Tasks
	 */
	public String syncAddTask(Task task) {
		String id = null;
		if (isFloatingTask(task)) {
			id = SyncFloatingTasks.syncAddTask(task);
		} else {
			id = SyncNonFloatingTasks.syncAddTask(task);
		}
		return id;
	}

	/**
	 * This method synchronises the deleting of a task to Google Calendar.
	 * 
	 * @param task
	 * @throws IOException
	 */
	public void syncDeleteTask(Task task) throws IOException {
		if (task.hasId()) {
			if (isFloatingTask(task)) {
				SyncFloatingTasks.syncDeleteTask(task);
			} else {
				SyncNonFloatingTasks.syncDeleteTask(task);
			}
		} else {
			// do nothing for unsynced tasks
		}
	}

	/**
	 * This method synchronises the updating of the task description to Google
	 * Calendar.
	 * 
	 * @param task
	 * @param newDescription
	 */
	public void syncUpdateTaskDescription(Task task, String newDescription) {
		try {
			if (isFloatingTask(task)) {
				SyncFloatingTasks.syncUpdateTaskDescription(task,
						newDescription);
			} else {
				SyncNonFloatingTasks.syncUpdateTaskDescription(task,
						newDescription);
			}
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
		}
	}

	/**
	 * This method synchronises the first step of undo by deleting all created
	 * tasks in Google Calendar.
	 * 
	 * @return true if all tasks in Google Calendar are deleted
	 */
	public boolean syncDeleteAllTasks() {
		ArrayList<Task> tasks = storage.getTasks();
		try {
			for (Task task : tasks) {
				if (task.hasId()) {
					if (isFloatingTask(task)) {
						SyncFloatingTasks.syncDeleteTask(task);
					} else {
						SyncNonFloatingTasks.syncDeleteTask(task);
					}
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * This method synchronises the second step of undo by adding back all
	 * previous tasks to Google Calendar.
	 */
	public void syncAddPreviousTasks() {
		ArrayList<Task> tasks = storage.getTasks();
		for (Task task : tasks) {
			task.setId(null);
		}
		syncToGoogle();
	}

	/**
	 * This method synchronises any unsynchronised changes to Google Calendar.
	 * 
	 * @return the relevant synchronisation result for user
	 */
	public String syncToGoogle() {
		if (hasUnsyncedTasks()) {
			String message = "";

			ArrayList<Task> tasks = storage.getTasks();
			message = syncUnsyncedAddedAndUpdatedTasks(message, tasks);

			ArrayList<Task> deletedTasks = storage.getDeletedTasks();
			message = syncUnsyncedDeletedTasks(message, deletedTasks);

			if (message.equals(MESSAGE_SYNC_SUCCESS)) {
				postSyncToGoogle(tasks, deletedTasks);
			}
			return message;
		} else {
			return MESSAGE_SYNC_EMPTY;
		}
	}

	/**
	 * This method synchronises any unsynchronised adding and updating of tasks
	 * to Google Calendar.
	 * 
	 * @param message
	 * @param tasks
	 * @return the relevant synchronisation result for user
	 */
	private String syncUnsyncedAddedAndUpdatedTasks(String message,
			ArrayList<Task> tasks) {
		for (Task task : tasks) {
			if (!task.hasId()) {
				String taskId = syncAddTask(task);
				if (taskId == null) {
					message = MESSAGE_SYNC_FAIL;
				} else {
					task.setId(taskId);
					message = MESSAGE_SYNC_SUCCESS;
				}
			} else if (task.hasBeenUpdated()) {
				try {
					if (isFloatingTask(task)) {
						com.google.api.services.tasks.model.Task updatedTask = SyncFloatingTasks
								.convertFloatingTaskToGoogleTask(task);
						SyncFloatingTasks.syncPatchTask(updatedTask);
					} else {
						Event updatedEvent = SyncNonFloatingTasks
								.convertNonFloatingTaskToEvent(task);
						SyncNonFloatingTasks.syncPatchTask(updatedEvent);
					}
					task.setHasBeenUpdated(false);
					message = MESSAGE_SYNC_SUCCESS;
				} catch (IOException e) {
					message = MESSAGE_SYNC_FAIL;
				}
			}
		}
		return message;
	}

	/**
	 * This method synchronises any unsynchronised deleting of tasks to Google
	 * Calendar.
	 * 
	 * @param message
	 * @param deletedTasks
	 * @return the relevant synchronisation result for user
	 */
	private String syncUnsyncedDeletedTasks(String message,
			ArrayList<Task> deletedTasks) {
		ListIterator<Task> iterator = deletedTasks.listIterator();
		if (!deletedTasks.isEmpty()) {
			try {
				while (iterator.hasNext()) {
					Task task = iterator.next();
					syncDeleteTask(task);
					iterator.remove();
					message = MESSAGE_SYNC_SUCCESS;
				}
			} catch (IOException e) {
				message = MESSAGE_SYNC_FAIL;
			}
		}
		return message;
	}

	/**
	 * This method locally saves any changes made after synchronisation.
	 * 
	 * @param tasks
	 * @param deletedTasks
	 */
	private void postSyncToGoogle(ArrayList<Task> tasks,
			ArrayList<Task> deletedTasks) {
		deletedTasks.clear();
		Storage.writeToFile(Storage.DATABASE_FILENAME, tasks);
		Storage.writeToFile(Storage.DELETED_TASKS_FILENAME, deletedTasks);
	}

	/**
	 * This method determines whether the task is a floating task.
	 * 
	 * @param task
	 * @return true if the task is a floating task
	 */
	private boolean isFloatingTask(Task task) {
		if (task.hasStartDate()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method determines whether there are any unsynchronised changes.
	 * 
	 * @return true if there are unsynchronised changes
	 */
	public boolean hasUnsyncedTasks() {
		ArrayList<Task> tasks = storage.getTasks();
		ArrayList<Task> deletedTasks = storage.getDeletedTasks();
		if (!deletedTasks.isEmpty()) {
			return true;
		}
		for (Task task : tasks) {
			if (!task.hasId() || task.hasBeenUpdated()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method determines whether the user is connected to the Internet.
	 * 
	 * @return true if user is online
	 */
	private boolean isConnectedToInternet() {
		int returnVal = -1;
		Process p1;
		try {
			if (OPERATING_SYSTEM.indexOf("win") >= 0) {
				p1 = java.lang.Runtime.getRuntime().exec(
						"ping -n 1 www.google.com");
			} else {
				p1 = java.lang.Runtime.getRuntime().exec(
						"ping -c 1 www.google.com");
			}
			returnVal = p1.waitFor();
		} catch (IOException | InterruptedException e) {

		}

		return (returnVal == 0);
	}

}
