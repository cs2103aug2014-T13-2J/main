package main.googlecalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.TimeZone;

import main.storage.Storage;
import main.storage.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.tasks.Tasks;

public class GoogleCalendar {

	private static String ID_GOOGLE_TASKS = "@default";
	private static String MESSAGE_SYNC_SUCCESS = "You have successfully synchronised all changes to Google.";
	private static String MESSAGE_SYNC_FAIL = "Sorry, an error has occurred while synchronising to Google. Please check your Internet connection.";
	private static String MESSAGE_SYNC_EMPTY = "Your Google Calendar is in sync.";

	public static String MESSAGE_OFFLINE = "You are currently offline and your changes will only be synchronised to Google when you are online.";

	private static GoogleCalendar theOne = null;
	private static Storage storage;
	private static LoginToGoogle loginToGoogle;
	private static Calendar googleCalendarClient;
	private static Tasks googleTasksClient;
	private static String googleId = null;

	private GoogleCalendar() {
		storage = Storage.getInstance();
		loginToGoogle = LoginToGoogle.getInstance();
		googleId = loginToGoogle.getGoogleId();
		googleCalendarClient = loginToGoogle.getGoogleCalendarClient();
		googleTasksClient = loginToGoogle.getGoogleTasksClient();
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
			id = syncAddFloatingTask(task);
		} else {
			id = syncAddNonFloatingTask(task);
		}
		return id;
	}

	private String syncAddFloatingTask(Task task) {
		String id = null;
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setTitle(task.getDescription());
		try {
			googleTask = googleTasksClient.tasks()
					.insert(ID_GOOGLE_TASKS, googleTask).execute();
			id = googleTask.getId();
		} catch (IOException e) {
			// perform sync only when online
		}
		return id;
	}

	private String syncAddNonFloatingTask(Task task) {
		String id = null;
		Event event = convertNonFloatingTaskToEvent(task);
		try {
			event = googleCalendarClient.events().insert(googleId, event)
					.execute();
			id = event.getId();
		} catch (IOException e) {
			// perform sync only when online
		}
		return id;
	}

	public void syncDeleteTask(Task task) throws IOException {
		if (task.hasId()) {
			if (isFloatingTask(task)) {
				syncDeleteFloatingTask(task);
			} else {
				syncDeleteNonFloatingTask(task);
			}
		} else {
			// do nothing for unsynced tasks
		}
	}

	private void syncDeleteFloatingTask(Task task) throws IOException {
		String id = task.getId();
		googleTasksClient.tasks().delete(ID_GOOGLE_TASKS, id).execute();
	}

	private void syncDeleteNonFloatingTask(Task task) throws IOException {
		String id = task.getId();
		googleCalendarClient.events().delete(googleId, id).execute();
	}

	private void syncPatchTaskToGoogleTasks(
			com.google.api.services.tasks.model.Task googleTask)
			throws IOException {
		String taskId = googleTask.getId();
		if (taskId != null) {
			googleTasksClient.tasks()
					.patch(ID_GOOGLE_TASKS, taskId, googleTask).execute();
		}
	}

	private void syncPatchTaskToGoogleCalendar(Event event) throws IOException {
		String eventId = event.getId();
		if (eventId != null) {
			googleCalendarClient.events().patch(googleId, eventId, event)
					.execute();
		}
	}

	public void syncUpdateTaskDescription(Task task, String newDescription) {
		try {
			if (isFloatingTask(task)) {
				com.google.api.services.tasks.model.Task googleTask = convertFloatingTaskToGoogleTask(task);
				googleTask.setTitle(newDescription);
				syncPatchTaskToGoogleTasks(googleTask);
			} else {
				Event event = convertNonFloatingTaskToEvent(task);
				event.setSummary(newDescription);
				syncPatchTaskToGoogleCalendar(event);
			}
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
			System.out.println(MESSAGE_OFFLINE);
		}
	}

	public void syncUpdateTaskVenue(Task task, String newVenue) {
		Event event = convertNonFloatingTaskToEvent(task);
		event.setLocation(newVenue);
		try {
			syncPatchTaskToGoogleCalendar(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
			System.out.println(MESSAGE_OFFLINE);
		}
	}

	public void syncUpdateTaskStartDate(Task task, LocalDate newStartDate) {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = null;
		if (task.hasStartTime()) {
			LocalTime taskTime = task.getStartTime();
			eventDateTime = convertToEventDateTime(newStartDate, taskTime);
		} else {
			eventDateTime = convertToEventDateTime(newStartDate);
		}
		event.setStart(eventDateTime);
		if (task.startDateEqualsEndDate()) {
			LocalDate newEndDate = newStartDate.plusDays(1);
			eventDateTime = convertToEventDateTime(newEndDate);
			event.setEnd(eventDateTime);
		}
		try {
			syncPatchTaskToGoogleCalendar(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
			System.out.println(MESSAGE_OFFLINE);
		}
	}

	public void syncUpdateTaskStartTime(Task task, LocalTime newStartTime)
			throws IOException {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = event.getStart();
		LocalDateTime taskDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate taskDate = taskDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(taskDate, newStartTime);
		event.setStart(eventDateTime);
		try {
			syncPatchTaskToGoogleCalendar(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
			throw new IOException(MESSAGE_OFFLINE);
		}
	}

	public void syncUpdateTaskEndDate(Task task, LocalDate newEndDate) {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = null;
		if (task.hasEndTime()) {
			LocalTime taskTime = task.getEndTime();
			eventDateTime = convertToEventDateTime(newEndDate, taskTime);
		} else {
			eventDateTime = convertToEventDateTime(newEndDate.plusDays(1));
		}
		event.setEnd(eventDateTime);
		try {
			syncPatchTaskToGoogleCalendar(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
			System.out.println(MESSAGE_OFFLINE);
		}
	}

	public void syncUpdateTaskEndTime(Task task, LocalTime newEndTime)
			throws IOException {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = event.getEnd();
		LocalDateTime taskDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate taskDate = taskDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(taskDate, newEndTime);
		event.setEnd(eventDateTime);
		try {
			syncPatchTaskToGoogleCalendar(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
			throw new IOException(MESSAGE_OFFLINE);
		}
	}

	public boolean syncDeleteAllTasks() {
		ArrayList<Task> tasks = storage.getTasks();
		try {
			for (Task task : tasks) {
				if (task.hasId()) {
					String id = task.getId();
					if (isFloatingTask(task)) {
						googleTasksClient.tasks().delete(ID_GOOGLE_TASKS, id)
								.execute();
					} else {
						googleCalendarClient.events().delete(googleId, id)
								.execute();
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
			String taskId;
			for (Task task : tasks) {
				if (!task.hasId()) {
					taskId = syncAddTask(task);
					if (taskId == null) {
						message = MESSAGE_SYNC_FAIL;
					} else {
						task.setId(taskId);
						message = MESSAGE_SYNC_SUCCESS;
					}
				} else if (task.hasBeenUpdated()) {
					try {
						if (isFloatingTask(task)) {
							com.google.api.services.tasks.model.Task updatedTask = convertFloatingTaskToGoogleTask(task);
							syncPatchTaskToGoogleTasks(updatedTask);
						} else {
							Event updatedEvent = convertNonFloatingTaskToEvent(task);
							syncPatchTaskToGoogleCalendar(updatedEvent);
						}
						task.setHasBeenUpdated(false);
						message = MESSAGE_SYNC_SUCCESS;
					} catch (IOException e) {
						message = MESSAGE_SYNC_FAIL;
					}
				}
			}

			ArrayList<Task> deletedTasks = storage.getDeletedTasks();
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
			if (message.equals(MESSAGE_SYNC_SUCCESS)) {
				deletedTasks.clear();
				Storage.writeToFile(Storage.DATABASE_FILENAME, tasks);
				Storage.writeToFile(Storage.DELETED_TASKS_FILENAME,
						deletedTasks);
			}
			return message;
		} else {
			return MESSAGE_SYNC_EMPTY;
		}
	}

	private com.google.api.services.tasks.model.Task convertFloatingTaskToGoogleTask(
			Task task) {
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setId(task.getId());
		googleTask.setTitle(task.getDescription());
		return googleTask;
	}

	private Event convertNonFloatingTaskToEvent(Task task) {
		Event event = new Event();
		event.setId(task.getId());
		event.setSummary(task.getDescription());
		event.setLocation(task.getVenue());

		LocalDate taskStartDate = task.getStartDate();
		LocalTime taskStartTime = task.getStartTime();

		LocalDate taskEndDate = task.getEndDate();
		LocalTime taskEndTime = task.getEndTime();

		if (taskStartTime == null && taskEndTime == null) {
			EventDateTime eventStartDateTime = convertToEventDateTime(taskStartDate);
			event.setStart(eventStartDateTime);
			EventDateTime eventEndDateTime = convertToEventDateTime(taskEndDate
					.plusDays(1));
			event.setEnd(eventEndDateTime);
		} else {
			EventDateTime eventStartDateTime = convertToEventDateTime(
					taskStartDate, taskStartTime);
			event.setStart(eventStartDateTime);

			EventDateTime eventEndDateTime = convertToEventDateTime(
					taskEndDate, taskEndTime);
			event.setEnd(eventEndDateTime);
		}

		return event;
	}

	private EventDateTime convertToEventDateTime(LocalDate localDate) {
		String temp = localDate.toString(); // of the format YYYY-MM-DD
		DateTime date = new DateTime(temp);
		EventDateTime eventDateTime = new EventDateTime();
		eventDateTime.setDate(date);
		return eventDateTime;
	}

	private EventDateTime convertToEventDateTime(LocalDate taskDate,
			LocalTime taskTime) {
		org.joda.time.DateTime jodaDateTime = taskDate.toDateTime(taskTime);
		Date date = jodaDateTime.toDate();
		DateTime dateTime = new DateTime(date, TimeZone.getTimeZone("UTC"));
		EventDateTime eventDateTime = new EventDateTime();
		eventDateTime.setDateTime(dateTime);
		return eventDateTime;
	}

	private LocalDateTime convertToLocalDateTime(EventDateTime eventDateTime) {
		DateTime dateTime = eventDateTime.getDateTime();
		LocalDateTime localDateTime = new LocalDateTime(dateTime.getValue());
		return localDateTime;
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
