package main.googlecalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import main.storage.Storage;
import main.storage.Task;

import com.google.api.services.calendar.model.Event;

public class GoogleCalendar {

	private static String MESSAGE_SYNC_SUCCESS = "You have successfully synchronised all changes to Google.";
	private static String MESSAGE_SYNC_FAIL = "Sorry, an error has occurred while synchronising to Google. Please check your Internet connection.";
	private static String MESSAGE_SYNC_EMPTY = "Your Google Calendar is in sync.";

	private static GoogleCalendar theOne = null;
	private static LoginToGoogle loginToGoogle;
	private static Storage storage;

	private GoogleCalendar() {
		storage = Storage.getInstance();
		loginToGoogle = LoginToGoogle.getInstance();
	}

	public static GoogleCalendar getInstance() {
		if (theOne == null) {
			theOne = new GoogleCalendar();
		}
		return theOne;
	}

	public String syncAddTask(Task task) {
		String id = null;
		if (isFloatingTask(task)) {
			id = SyncFloatingTasks.syncAddTask(task);
		} else {
			id = SyncNonFloatingTasks.syncAddTask(task);
		}
		return id;
	}

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

	public void syncAddPreviousTasks() {
		ArrayList<Task> tasks = storage.getTasks();
		for (Task task : tasks) {
			task.setId(null);
		}
		syncToGoogle();
	}

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
						SyncFloatingTasks
								.syncPatchTask(updatedTask);
					} else {
						Event updatedEvent = SyncNonFloatingTasks
								.convertNonFloatingTaskToEvent(task);
						SyncNonFloatingTasks
								.syncPatchTask(updatedEvent);
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
	
	private void postSyncToGoogle(ArrayList<Task> tasks,
			ArrayList<Task> deletedTasks) {
		deletedTasks.clear();
		Storage.writeToFile(Storage.DATABASE_FILENAME, tasks);
		Storage.writeToFile(Storage.DELETED_TASKS_FILENAME,
				deletedTasks);
	}

	private boolean isFloatingTask(Task task) {
		if (task.hasStartDate()) {
			return false;
		} else {
			return true;
		}
	}

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

}
