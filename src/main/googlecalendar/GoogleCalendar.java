package main.googlecalendar;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;

import main.storage.Storage;
import main.storage.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.fasterxml.uuid.Generators;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;

public class GoogleCalendar {

	private static String CLIENT_ID = "101595183122-6l80mvk11dn0o476g77773fi73mev60c.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "0qES2e4j6ob4WlgekNRCESzR";
	private static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	private static String MESSAGE_LOGIN_SUCCESS = "You have successfully logged in to Google.";
	private static String MESSAGE_LOGIN_FAIL = "Sorry, an error has occurred while logging in to Google. Please try again.";
	private static String MESSAGE_OFFLINE_FIRST_TIME = "You are currently offline. Please provide your Google account ID to continue: ";
	private static String MESSAGE_OFFLINE_CONFIRM_ID = "Are you sure %s is the correct ID? [Y/N]\n";
	private static String MESSAGE_OFFLINE = "You are currently still offline. Please check your Internet connection.";
	private static String MESSAGE_SYNC_SUCCESS = "You have successfully synchronised all changes to Google.";
	private static String MESSAGE_SYNC_FAIL = "Sorry, an error has occurred while synchronising to Google. Please try again.";
	private static String MESSAGE_SYNC_EMPTY = "You have no changes to synchronise.";
	private static String MESSAGE_CALENDAR_ID_SET = "Google ID registered. You may now proceed.";
	private static String MESSAGE_INVALID_ARGUMENT = "Sorry, the argument must either be 'Y' or 'N'.";
	private static String MESSAGE_INVALID_EMAIL = "Sorry, the argument must be a valid email address.";
	private static String MESSAGE_SYNC_ADD_FAIL = "Sorry, added tasks failed to sync.";
	private static String MESSAGE_SYNC_DELETE_FAIL = "Sorry, deleted tasks failed to sync.";
	private static String MESSAGE_SYNC_UPDATE_FAIL = "Sorry, updated tasks failed to sync.";

	private static String NAME_APPLICATION = "Tasker/0.4";
	/** Directory to store user credentials. */
	private static final File CREDENTIAL_STORE_DIR = new File(
			System.getProperty("user.dir"), ".store/Tasker");
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	private static Calendar googleCalendarClient;
	private static Tasks googleTasksClient;
	private static BatchRequest addEventBatch;
	private static BatchRequest deleteEventBatch;
	private static BatchRequest updateEventBatch;
	private static BatchRequest addTaskBatch;
	private static BatchRequest updateTaskBatch;
	private static BatchRequest deleteTaskBatch;

	private static GoogleCalendar theOne = null;
	private static String googleId = null;
	private static Scanner scanner = new Scanner(System.in);
	private static Storage storage = Storage.getInstance();

	private GoogleCalendar() {

	}

	public static GoogleCalendar getInstance() {
		if (theOne == null) {
			theOne = new GoogleCalendar();
		}
		return theOne;
	}

	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorise() throws Exception {
		// load client secrets
		GoogleClientSecrets.Details installedDetails = new GoogleClientSecrets.Details();
		installedDetails.setClientId(CLIENT_ID);
		installedDetails.setClientSecret(CLIENT_SECRET);
		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
		clientSecrets.setInstalled(installedDetails);

		// set up authorization code flow
		ArrayList<String> scopes = new ArrayList<>();
		scopes.add(CalendarScopes.CALENDAR);
		scopes.add(TasksScopes.TASKS);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets, scopes)
				.setDataStoreFactory(dataStoreFactory).build();

		// authorise
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	public String logInToGoogleCalendar() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(CREDENTIAL_STORE_DIR);
			Credential credential = authorise();
			googleCalendarClient = new com.google.api.services.calendar.Calendar.Builder(
					httpTransport, JSON_FACTORY, credential)
					.setApplicationName(NAME_APPLICATION).build();
			googleTasksClient = new Tasks.Builder(httpTransport, JSON_FACTORY,
					credential).setApplicationName(NAME_APPLICATION).build();
			initialiseGoogleCalendarBatch();
			initialiseGoogleTasksBatch();
		} catch (GeneralSecurityException e) {
			return MESSAGE_LOGIN_FAIL;
		} catch (Exception e) {
			return MESSAGE_LOGIN_FAIL;
		}

		try {
			googleId = googleCalendarClient.calendars().get("primary")
					.execute().getId();
		} catch (IOException e) {
			if (googleId == null) {
				return getCalendarIdFromUser();
			} else {
				return MESSAGE_OFFLINE;
			}
		}
		return MESSAGE_LOGIN_SUCCESS;
	}

	private void initialiseGoogleTasksBatch() {
		if (addTaskBatch == null) {
			addTaskBatch = googleTasksClient.batch();
		}
		if (updateTaskBatch == null) {
			updateTaskBatch = googleTasksClient.batch();
		}
		if (deleteTaskBatch == null) {
			deleteTaskBatch = googleTasksClient.batch();
		}
	}

	private void initialiseGoogleCalendarBatch() {
		if (addEventBatch == null) {
			addEventBatch = googleCalendarClient.batch();
		}
		if (updateEventBatch == null) {
			updateEventBatch = googleCalendarClient.batch();
		}
		if (deleteEventBatch == null) {
			deleteEventBatch = googleCalendarClient.batch();
		}
	}

	private String getCalendarIdFromUser() {
		String message = "";
		System.out.println(MESSAGE_OFFLINE_FIRST_TIME);
		googleId = scanner.nextLine();
		if (!isValidEmail(googleId)) {
			System.out.println(MESSAGE_INVALID_EMAIL);
			return getCalendarIdFromUser();
		} else {
			if (confirmCalendarIdFromUser()) {
				message = MESSAGE_CALENDAR_ID_SET;
			} else {
				message = getCalendarIdFromUser();
			}
		}
		return message;
	}

	private boolean confirmCalendarIdFromUser() {
		System.out.printf(MESSAGE_OFFLINE_CONFIRM_ID, googleId);
		String userReply = scanner.nextLine();
		if (userReply.equalsIgnoreCase("y")) {
			return true;
		} else if (userReply.equalsIgnoreCase("n")) {
			return false;
		} else {
			System.out.println(MESSAGE_INVALID_ARGUMENT);
			return confirmCalendarIdFromUser();
		}
	}

	private boolean isValidEmail(String calendarId) {
		boolean isValid = calendarId.matches(EMAIL_REGEX);
		return isValid;
	}

	public String syncAddTask(Task task) {
		String id = null;
		if (isFloatingTask(task)) {
			id = syncAddFloatingTask(task);
		} else {
			id = generateTimeBasedUUID();
			syncAddNonFloatingTask(task, id);
		}
		return id;
	}

	private String syncAddFloatingTask(Task task) {
		String id = null;
		com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
		googleTask.setTitle(task.getDescription());
		try {
			googleTask = googleTasksClient.tasks()
					.insert("@default", googleTask).execute();
			id = googleTask.getId();
		} catch (IOException e) {
			// perform sync only when online
		}
		return id;
	}

	private void syncAddNonFloatingTask(Task task, String eventId) {
		Event event = convertNonFloatingTaskToEvent(task);
		event.setId(eventId);
		try {
			googleCalendarClient.events().insert(googleId, event).execute();
		} catch (IOException e) {
			try {
				googleCalendarClient.events().insert(googleId, event)
						.queue(addEventBatch, new JsonBatchCallback<Event>() {

							@Override
							public void onSuccess(Event event, HttpHeaders arg1)
									throws IOException {

							}

							@Override
							public void onFailure(GoogleJsonError arg0,
									HttpHeaders arg1) throws IOException {
								System.out.println(MESSAGE_SYNC_ADD_FAIL);
							}
						});
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_ADD_FAIL);
			}
		}
	}

	public void syncDeleteTask(Task task, String id) {
		if (isFloatingTask(task)) {
			syncDeleteFloatingTask(id);
		} else {
			if (id != null) {
				syncDeleteNonFloatingTask(id);
			}
		}
	}

	private void syncDeleteFloatingTask(String taskId) {
		try {
			googleTasksClient.tasks().delete("@default", taskId).execute();
		} catch (IOException e) {
			try {
				googleTasksClient.tasks().delete("@default", taskId)
						.queue(deleteTaskBatch, new JsonBatchCallback<Void>() {

							@Override
							public void onSuccess(Void arg0, HttpHeaders arg1)
									throws IOException {

							}

							@Override
							public void onFailure(GoogleJsonError arg0,
									HttpHeaders arg1) throws IOException {
								System.out.println(MESSAGE_SYNC_DELETE_FAIL);
							}
						});
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_DELETE_FAIL);
			}
		}
	}

	private void syncDeleteNonFloatingTask(String eventId) {
		try {
			googleCalendarClient.events().delete(googleId, eventId).execute();
		} catch (IOException e) {
			try {
				googleCalendarClient.events().delete(googleId, eventId)
						.queue(deleteEventBatch, new JsonBatchCallback<Void>() {

							@Override
							public void onSuccess(Void arg0, HttpHeaders arg1)
									throws IOException {

							}

							@Override
							public void onFailure(GoogleJsonError arg0,
									HttpHeaders arg1) throws IOException {
								System.out.println(MESSAGE_SYNC_DELETE_FAIL);
							}

						});
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_DELETE_FAIL);
			}
		}
	}

	private void syncPatchTaskToGoogleCalendar(Event event) {
		String eventId = event.getId();
		try {
			googleCalendarClient.events().patch(googleId, eventId, event)
					.execute();
		} catch (IOException e) {
			try {
				googleCalendarClient
						.events()
						.patch(googleId, eventId, event)
						.queue(updateEventBatch,
								new JsonBatchCallback<Event>() {

									@Override
									public void onSuccess(Event event,
											HttpHeaders arg1)
											throws IOException {

									}

									@Override
									public void onFailure(GoogleJsonError arg0,
											HttpHeaders arg1)
											throws IOException {
										System.out
												.println(MESSAGE_SYNC_UPDATE_FAIL);
									}
								});
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_UPDATE_FAIL);
			}
		}
	}

	public void syncUpdateTaskDescription(Task task, String newDescription) {
		if (task.hasId() && isFloatingTask(task)) {
			com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
			String id = task.getId();
			googleTask.setId(id);
			googleTask.setTitle(newDescription);
			try {
				googleTasksClient.tasks().patch("@default", id, googleTask)
						.execute();
			} catch (IOException e) {
				try {
					googleTasksClient
							.tasks()
							.patch("@default", id, googleTask)
							.queue(updateTaskBatch,
									new JsonBatchCallback<com.google.api.services.tasks.model.Task>() {

										@Override
										public void onSuccess(
												com.google.api.services.tasks.model.Task arg0,
												HttpHeaders arg1)
												throws IOException {

										}

										@Override
										public void onFailure(
												GoogleJsonError arg0,
												HttpHeaders arg1)
												throws IOException {
											System.out
													.println(MESSAGE_SYNC_UPDATE_FAIL);
										}
									});
				} catch (IOException e1) {
					System.out.println(MESSAGE_SYNC_UPDATE_FAIL);
				}
			}
		} else {
			Event event = convertNonFloatingTaskToEvent(task);
			event.setSummary(newDescription);
			syncPatchTaskToGoogleCalendar(event);
		}
	}

	public void syncUpdateTaskVenue(Task task, String newVenue) {
		Event event = convertNonFloatingTaskToEvent(task);
		event.setLocation(newVenue);
		syncPatchTaskToGoogleCalendar(event);
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
		syncPatchTaskToGoogleCalendar(event);
	}

	public void syncUpdateTaskStartTime(Task task, LocalTime newStartTime) {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = event.getStart();
		LocalDateTime taskDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate taskDate = taskDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(taskDate, newStartTime);
		event.setStart(eventDateTime);
		syncPatchTaskToGoogleCalendar(event);
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
		syncPatchTaskToGoogleCalendar(event);
	}

	public void syncUpdateTaskEndTime(Task task, LocalTime newEndTime) {
		Event event = convertNonFloatingTaskToEvent(task);
		EventDateTime eventDateTime = event.getEnd();
		LocalDateTime taskDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate taskDate = taskDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(taskDate, newEndTime);
		event.setEnd(eventDateTime);
		syncPatchTaskToGoogleCalendar(event);
	}

	public String syncToGoogle() {
		String message = MESSAGE_SYNC_EMPTY;
		try {
			if (addEventBatch.size() != 0) {
				addEventBatch.execute();
				message = MESSAGE_SYNC_SUCCESS;
			}
			if (updateEventBatch.size() != 0) {
				updateEventBatch.execute();
				message = MESSAGE_SYNC_SUCCESS;
			}
			if (deleteEventBatch.size() != 0) {
				deleteEventBatch.execute();
				message = MESSAGE_SYNC_SUCCESS;
			}
			if (addTaskBatch.size() != 0) {
				addTaskBatch.execute();
				message = MESSAGE_SYNC_SUCCESS;
			}
			if (updateTaskBatch.size() != 0) {
				updateTaskBatch.execute();
				message = MESSAGE_SYNC_SUCCESS;
			}
			if (deleteTaskBatch.size() != 0) {
				deleteTaskBatch.execute();
				message = MESSAGE_SYNC_SUCCESS;
			}
			message = setIdForFloatingTasksWhenOnline(message);
		} catch (IOException e) {
			message = MESSAGE_SYNC_FAIL;
		}
		return message;
	}

	private String setIdForFloatingTasksWhenOnline(String message) {
		ArrayList<Task> taskList = storage.getTasks();
		for (Task task : taskList) {
			if (!task.hasId()) {
				String taskId = syncAddFloatingTask(task);
				if (taskId != null) {
					task.setId(taskId);
					storage.saveCurrentState();
					message = MESSAGE_SYNC_SUCCESS;
				} else {
					message = MESSAGE_SYNC_FAIL;
				}
			}
		}
		return message;
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

	private String generateTimeBasedUUID() {
		UUID uuid = Generators.timeBasedGenerator().generate();
		String uuidString = uuid.toString().replace("-", "");
		return uuidString;
	}

	public boolean hasUnsyncedTasks() {
		if (addEventBatch.size() != 0 || updateEventBatch.size() != 0
				|| deleteEventBatch.size() != 0 || addTaskBatch.size() != 0
				|| updateTaskBatch.size() != 0 || deleteTaskBatch.size() != 0) {
			return true;
		}
		ArrayList<Task> taskList = storage.getTasks();
		for (Task task : taskList) {
			if (!task.hasId()) {
				return true;
			}
		}
		return false;
	}

}
