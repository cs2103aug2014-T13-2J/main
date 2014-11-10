package main.googlecalendar;

import java.io.IOException;

import main.storage.Task;

import com.google.api.services.tasks.Tasks;

//@author A0072744A
/**
 * This class handles the synchronisation of floating tasks to Google Tasks.
 */
public class SyncFloatingTasks {

	private static String ID_GOOGLE_TASKS = "@default";

	private static LoginToGoogle loginToGoogle = LoginToGoogle.getInstance();
	private static Tasks googleTasksClient = loginToGoogle
			.getGoogleTasksClient();

	/**
	 * This method synchronises the adding of a floating task to Google Tasks.
	 * 
	 * @param task
	 * @return the task ID provided by Google Tasks
	 */
	protected static String syncAddTask(Task task) {
		String id = null;
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setTitle(task.getDescription());
		try {
			googleTask = googleTasksClient.tasks()
					.insert(ID_GOOGLE_TASKS, googleTask).execute();
			id = googleTask.getId();
		} catch (IOException e) {

		}
		return id;
	}

	/**
	 * This method synchronises the deleting of a floating task to Google Tasks.
	 * 
	 * @param task
	 * @throws IOException
	 */
	protected static void syncDeleteTask(Task task) throws IOException {
		String id = task.getId();
		googleTasksClient.tasks().delete(ID_GOOGLE_TASKS, id).execute();
	}

	/**
	 * This method synchronises the updating of a floating task to Google Tasks.
	 * 
	 * @param googleTask
	 * @throws IOException
	 */
	protected static void syncPatchTask(
			com.google.api.services.tasks.model.Task googleTask)
			throws IOException {
		String taskId = googleTask.getId();
		if (taskId != null) {
			googleTasksClient.tasks()
					.patch(ID_GOOGLE_TASKS, taskId, googleTask).execute();
		}
	}

	/**
	 * This method synchronises the updating of the description of a floating
	 * task to Google Tasks.
	 * 
	 * @param task
	 * @param newDescription
	 * @throws IOException
	 */
	public static void syncUpdateTaskDescription(Task task,
			String newDescription) throws IOException {
		com.google.api.services.tasks.model.Task googleTask = convertFloatingTaskToGoogleTask(task);
		googleTask.setTitle(newDescription);
		syncPatchTask(googleTask);
	}

	/**
	 * This method converts a floating task to a Google Task.
	 * 
	 * @param task
	 * @return the converted Google Task object
	 */
	protected static com.google.api.services.tasks.model.Task convertFloatingTaskToGoogleTask(
			Task task) {
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setId(task.getId());
		googleTask.setTitle(task.getDescription());
		return googleTask;
	}

}
