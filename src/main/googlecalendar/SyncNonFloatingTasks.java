package main.googlecalendar;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import main.storage.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

//@author A0072744A
/**
 * This class handles the synchronisation of non-floating tasks to Google
 * Calendar.
 */
public class SyncNonFloatingTasks {

	private static LoginToGoogle loginToGoogle = LoginToGoogle.getInstance();
	private static Calendar googleCalendarClient = loginToGoogle
			.getGoogleCalendarClient();
	private static String googleId = loginToGoogle.getGoogleId();

	/**
	 * This method synchronises the adding of a non-floating task to Google
	 * Calendar.
	 * 
	 * @param task
	 * @return the task ID provided by Google Calendar
	 */
	protected static String syncAddTask(Task task) {
		String id = null;
		Event event = convertNonFloatingTaskToEvent(task);
		try {
			event = googleCalendarClient.events().insert(googleId, event)
					.execute();
			id = event.getId();
		} catch (IOException e) {

		}
		return id;
	}

	/**
	 * This method synchronises the deleting of a non-floating task to Google
	 * Calendar.
	 * 
	 * @param task
	 * @throws IOException
	 */
	protected static void syncDeleteTask(Task task) throws IOException {
		String id = task.getId();
		googleCalendarClient.events().delete(googleId, id).execute();
	}

	/**
	 * This method synchronises the updating of a non-floating task to Google
	 * Calendar.
	 * 
	 * @param event
	 * @throws IOException
	 */
	protected static void syncPatchTask(Event event) throws IOException {
		String eventId = event.getId();
		if (eventId != null) {
			googleCalendarClient.events().patch(googleId, eventId, event)
					.execute();
		}
	}

	/**
	 * This method synchronises the updating of the description of a
	 * non-floating task to Google Calendar.
	 * 
	 * @param task
	 * @param newDescription
	 * @throws IOException
	 */
	protected static void syncUpdateTaskDescription(Task task,
			String newDescription) throws IOException {
		Event event = convertNonFloatingTaskToEvent(task);
		event.setSummary(newDescription);
		syncPatchTask(event);
	}

	/**
	 * This method synchronises the updating of the venue of a non-floating task
	 * to Google Calendar.
	 * 
	 * @param task
	 * @param newVenue
	 */
	public static void syncUpdateTaskVenue(Task task, String newVenue) {
		Event event = convertNonFloatingTaskToEvent(task);
		event.setLocation(newVenue);
		try {
			syncPatchTask(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
		}
	}

	/**
	 * This method synchronises the updating of the start date of a non-floating
	 * task to Google Calendar.
	 * 
	 * @param task
	 * @param newStartDate
	 */
	public static void syncUpdateTaskStartDate(Task task, LocalDate newStartDate) {
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
			syncPatchTask(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
		}
	}

	/**
	 * This method synchronises the updating of the start time of a non-floating
	 * task to Google Calendar.
	 * 
	 * @param task
	 * @param newStartTime
	 * @throws IOException
	 */
	public static void syncUpdateTaskStartTime(Task task, LocalTime newStartTime)
			throws IOException {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = event.getStart();
		LocalDateTime taskDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate taskDate = taskDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(taskDate, newStartTime);
		event.setStart(eventDateTime);
		try {
			syncPatchTask(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
		}
	}

	/**
	 * This method synchronises the updating of the end date of a non-floating
	 * task to Google Calendar.
	 * 
	 * @param task
	 * @param newEndDate
	 */
	public static void syncUpdateTaskEndDate(Task task, LocalDate newEndDate) {
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
			syncPatchTask(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
		}
	}

	/**
	 * This method synchronises the updating of the end time of a non-floating
	 * task to Google Calendar.
	 * 
	 * @param task
	 * @param newEndTime
	 * @throws IOException
	 */
	public static void syncUpdateTaskEndTime(Task task, LocalTime newEndTime)
			throws IOException {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = event.getEnd();
		LocalDateTime taskDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate taskDate = taskDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(taskDate, newEndTime);
		event.setEnd(eventDateTime);
		try {
			syncPatchTask(event);
		} catch (IOException e) {
			task.setHasBeenUpdated(true);
		}
	}

	/**
	 * This method converts a non-floating task to a Google Calendar Event.
	 * 
	 * @param task
	 * @return the converted Google Calendar Event object
	 */
	protected static Event convertNonFloatingTaskToEvent(Task task) {
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

	/**
	 * This method converts a LocalDate to a EventDateTime.
	 * 
	 * @param localDate
	 * @return the converted EventDateTime object
	 */
	private static EventDateTime convertToEventDateTime(LocalDate localDate) {
		String temp = localDate.toString(); // of the format YYYY-MM-DD
		DateTime date = new DateTime(temp);
		EventDateTime eventDateTime = new EventDateTime();
		eventDateTime.setDate(date);
		return eventDateTime;
	}

	/**
	 * This method converts a LocalDate and LocalTime to a EventDateTime.
	 * 
	 * @param taskDate
	 * @param taskTime
	 * @return the converted EventDateTime object
	 */
	private static EventDateTime convertToEventDateTime(LocalDate taskDate,
			LocalTime taskTime) {
		org.joda.time.DateTime jodaDateTime = taskDate.toDateTime(taskTime);
		Date date = jodaDateTime.toDate();
		DateTime dateTime = new DateTime(date, TimeZone.getTimeZone("UTC"));
		EventDateTime eventDateTime = new EventDateTime();
		eventDateTime.setDateTime(dateTime);
		return eventDateTime;
	}

	/**
	 * This method converts a EventDateTime to LocalDateTime.
	 * 
	 * @param eventDateTime
	 * @return the converted LocalDateTime object
	 */
	private static LocalDateTime convertToLocalDateTime(
			EventDateTime eventDateTime) {
		DateTime dateTime = eventDateTime.getDateTime();
		LocalDateTime localDateTime = new LocalDateTime(dateTime.getValue());
		return localDateTime;
	}
}
