package main.googlecalendar;

import java.io.IOException;

import main.storage.Task;

import com.google.api.services.tasks.Tasks;

public class SyncFloatingTasks {

	private static String ID_GOOGLE_TASKS = "@default";

	private static LoginToGoogle loginToGoogle = LoginToGoogle.getInstance();
	private static Tasks googleTasksClient = loginToGoogle
			.getGoogleTasksClient();

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

	protected static void syncDeleteTask(Task task) throws IOException {
		String id = task.getId();
		googleTasksClient.tasks().delete(ID_GOOGLE_TASKS, id).execute();
	}

	protected static void syncPatchTask(
			com.google.api.services.tasks.model.Task googleTask)
			throws IOException {
		String taskId = googleTask.getId();
		if (taskId != null) {
			googleTasksClient.tasks()
					.patch(ID_GOOGLE_TASKS, taskId, googleTask).execute();
		}
	}

	public static void syncUpdateTaskDescription(Task task,
			String newDescription) throws IOException {
		com.google.api.services.tasks.model.Task googleTask = convertFloatingTaskToGoogleTask(task);
		googleTask.setTitle(newDescription);
		syncPatchTask(googleTask);
	}

	protected static com.google.api.services.tasks.model.Task convertFloatingTaskToGoogleTask(
			Task task) {
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setId(task.getId());
		googleTask.setTitle(task.getDescription());
		return googleTask;
	}

}
