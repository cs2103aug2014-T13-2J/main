package main.googlecalendar;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
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

public class GoogleCalendar {

	private static String CLIENT_ID = "101595183122-6l80mvk11dn0o476g77773fi73mev60c.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "0qES2e4j6ob4WlgekNRCESzR";
	private static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

//	private static String MESSAGE_ALREADY_LOGGED_IN = "Sorry, you are already logged in to Google Calendar.";
//	private static String MESSAGE_ASK_TO_LOGIN = "Log in to Google Calendar? [Y/N]";
//	private static String MESSAGE_NO_LOGIN = "Log in cancelled. Please note that your tasks will not be synchronised to Google Calendar.";
	private static String MESSAGE_LOGIN_SUCCESS = "You have successfully logged in to Google Calendar.";
	private static String MESSAGE_LOGIN_FAIL = "Sorry, an error has occurred while logging in to Google Calendar. Please try again.";
	private static String MESSAGE_OFFLINE_FIRST_TIME = "You are currently offline. Please provide your Google account ID to continue: ";
	private static String MESSAGE_OFFLINE_CONFIRM_ID = "Are you sure %s is the correct ID? [Y/N]\n";
	private static String MESSAGE_OFFLINE = "You are currently still offline. Please check your Internet connection.";
	private static String MESSAGE_SYNC_SUCCESS = "You have successfully synchronised all changes to Google Calendar.";
	private static String MESSAGE_SYNC_FAIL = "Sorry, an error has occurred while synchronising to Google Calendar. Please try again.";
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
	private static Calendar client;
	private static BatchRequest addBatch;
	private static BatchRequest deleteBatch;
	private static BatchRequest updateBatch;

	private static GoogleCalendar theOne = null;
//	private static boolean isLoggedIn;
	private static String calendarId = null;
	private static Scanner scanner = new Scanner(System.in);
	private static Storage storage = Storage.getInstance();

	private JsonBatchCallback<Event> callbackForAdd = new JsonBatchCallback<Event>() {

		@Override
		public void onSuccess(Event event, HttpHeaders arg1) throws IOException {

		}

		@Override
		public void onFailure(GoogleJsonError arg0, HttpHeaders arg1)
				throws IOException {
			System.out.println(MESSAGE_SYNC_ADD_FAIL);
		}
	};

	private JsonBatchCallback<Void> callbackForDelete = new JsonBatchCallback<Void>() {

		@Override
		public void onSuccess(Void arg0, HttpHeaders arg1) throws IOException {

		}

		@Override
		public void onFailure(GoogleJsonError arg0, HttpHeaders arg1)
				throws IOException {
			System.out.println(MESSAGE_SYNC_DELETE_FAIL);
		}

	};

	private JsonBatchCallback<Event> callbackForUpdate = new JsonBatchCallback<Event>() {

		@Override
		public void onSuccess(Event event, HttpHeaders arg1) throws IOException {

		}

		@Override
		public void onFailure(GoogleJsonError arg0, HttpHeaders arg1)
				throws IOException {
			System.out.println(MESSAGE_SYNC_UPDATE_FAIL);
		}
	};

	private GoogleCalendar() {

	}

	public static GoogleCalendar getInstance() {
		if (theOne == null) {
			theOne = new GoogleCalendar();
		}
		return theOne;
	}

//	public String initialiseGoogleCalendar() {
//		String message = "";
//		if (!isLoggedIn) {
//			try {
//				if (isLoggingInToGoogleCalendar()) {
//					message = logInToGoogleCalendar();
//				}
//			} catch (IllegalArgumentException e) {
//				System.out.println(e.getMessage());
//				message = initialiseGoogleCalendar();
//			}
//		} else {
//			message = MESSAGE_ALREADY_LOGGED_IN;
//		}
//		return message;
//	}

//	private boolean isLoggingInToGoogleCalendar() {
//		System.out.println(MESSAGE_ASK_TO_LOGIN);
//		String userReplyToLogIn = scanner.nextLine();
//		if (userReplyToLogIn.equalsIgnoreCase("y")) {
//			return true;
//		} else if (userReplyToLogIn.equalsIgnoreCase("n")) {
//			System.out.println(MESSAGE_NO_LOGIN);
//			isLoggedIn = false;
//			return false;
//		} else {
//			throw new IllegalArgumentException(MESSAGE_INVALID_ARGUMENT);
//		}
//	}

	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorise() throws Exception {
		// load client secrets
		GoogleClientSecrets.Details installedDetails = new GoogleClientSecrets.Details();
		installedDetails.setClientId(CLIENT_ID);
		installedDetails.setClientSecret(CLIENT_SECRET);
		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
		clientSecrets.setInstalled(installedDetails);

		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(CalendarScopes.CALENDAR))
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
			client = new com.google.api.services.calendar.Calendar.Builder(
					httpTransport, JSON_FACTORY, credential)
					.setApplicationName(NAME_APPLICATION).build();
			if (addBatch == null) {
				addBatch = client.batch();
			}
			if (updateBatch == null) {
				updateBatch = client.batch();
			}
			if (deleteBatch == null) {
				deleteBatch = client.batch();
			}
//			isLoggedIn = true;
		} catch (GeneralSecurityException e) {
			return MESSAGE_LOGIN_FAIL;
		} catch (Exception e) {
			return MESSAGE_LOGIN_FAIL;
		}

		try {
			calendarId = client.calendars().get("primary").execute().getId();
		} catch (IOException e) {
//			isLoggedIn = false;
			if (calendarId == null) {
				return getCalendarIdFromUser();
			} else {
				return MESSAGE_OFFLINE;
			}
		}
		return MESSAGE_LOGIN_SUCCESS;
	}

	public String syncToGoogleCalendar() {
		try {
			if (addBatch.size() != 0) {
				addBatch.execute();
				Storage.writeToFile();
			}
			if (updateBatch.size() != 0) {
				updateBatch.execute();
			}
			if (deleteBatch.size() != 0) {
				deleteBatch.execute();
			}
		} catch (IOException e) {
			return MESSAGE_SYNC_FAIL;
		}
		return MESSAGE_SYNC_SUCCESS;
	}

	private String getCalendarIdFromUser() {
		String message = "";
		System.out.println(MESSAGE_OFFLINE_FIRST_TIME);
		calendarId = scanner.nextLine();
		if (!isValidEmail(calendarId)) {
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
		System.out.printf(MESSAGE_OFFLINE_CONFIRM_ID, calendarId);
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

	public String syncAddNonFloatingTask(Task task) {
		Event event = convertNonFloatingTaskToEvent(task);
		String eventId = generateTimeBasedUUID();
		event.setId(eventId);
		try {
			client.events().insert(calendarId, event).execute();
		} catch (IOException e) {
			try {
				client.events().insert(calendarId, event)
						.queue(addBatch, callbackForAdd);
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_ADD_FAIL);
			}
		}
		return eventId;
	}

	private boolean isNonFloatingTask(Task task) {
		if (task.getHasStartDate()) {
			return true;
		} else {
			return false;
		}
	}

	public void syncDeleteNonFloatingTask(String eventId) {
		try {
			client.events().delete(calendarId, eventId).execute();
		} catch (IOException e) {
			try {
				client.events().delete(calendarId, eventId)
						.queue(deleteBatch, callbackForDelete);
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_DELETE_FAIL);
			}
		}
	}

	private void syncUpdateTask(String eventId, Event event) {
		try {
			client.events().update(calendarId, eventId, event).execute();
		} catch (IOException e) {
			try {
				client.events().update(calendarId, eventId, event)
						.queue(updateBatch, callbackForUpdate);
			} catch (IOException e1) {
				System.out.println(MESSAGE_SYNC_UPDATE_FAIL);
			}
		}
	}

	public void syncUpdateTaskDescription(String eventId, String newDescription) {
		Event event = findTaskToUpdate(eventId);
		event.setSummary(newDescription);
		syncUpdateTask(eventId, event);
	}

	public void syncUpdateTaskVenue(String eventId, String newVenue) {
		Event event = findTaskToUpdate(eventId);
		event.setLocation(newVenue);
		syncUpdateTask(eventId, event);
	}

	public void syncUpdateTaskStartDate(String eventId, LocalDate newStartDate) {
		Event event = findTaskToUpdate(eventId);
		EventDateTime eventDateTime = event.getStart();
		LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
		LocalTime localTime = localDateTime.toLocalTime();
		eventDateTime = convertToEventDateTime(newStartDate, localTime);
		event.setStart(eventDateTime);
		syncUpdateTask(eventId, event);
	}

	public void syncUpdateTaskStartTime(String eventId, LocalTime newStartTime) {
		Event event = findTaskToUpdate(eventId);
		EventDateTime eventDateTime = event.getStart();
		LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate localDate = localDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(localDate, newStartTime);
		event.setStart(eventDateTime);
		syncUpdateTask(eventId, event);
	}

	public void syncUpdateTaskEndDate(String eventId, LocalDate newEndDate) {
		Event event = findTaskToUpdate(eventId);
		EventDateTime eventDateTime = event.getEnd();
		LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
		LocalTime localTime = localDateTime.toLocalTime();
		eventDateTime = convertToEventDateTime(newEndDate, localTime);
		event.setEnd(eventDateTime);
		syncUpdateTask(eventId, event);
	}

	public void syncUpdateTaskEndTime(String eventId, LocalTime newEndTime) {
		Event event = findTaskToUpdate(eventId);
		EventDateTime eventDateTime = event.getEnd();
		LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
		LocalDate localDate = localDateTime.toLocalDate();
		eventDateTime = convertToEventDateTime(localDate, newEndTime);
		event.setEnd(eventDateTime);
		syncUpdateTask(eventId, event);
	}

	private Event findTaskToUpdate(String eventId) {
		Event event = null;
		ArrayList<Task> taskList = storage.getTasks();
		for (Task task : taskList) {
			String taskEventId = task.getEventId();
			if (taskEventId.equals(eventId)) {
				event = convertNonFloatingTaskToEvent(task);
				break;
			}
		}
		return event;
	}

	private Event convertNonFloatingTaskToEvent(Task task) {
		Event event = new Event();
		event.setSummary(task.getDescription());
		event.setLocation(task.getVenue());

		LocalDate taskStartDate = task.getStartDate();
		LocalTime taskStartTime = task.getStartTime();

		LocalDate taskEndDate = task.getEndDate();
		LocalTime taskEndTime = task.getEndTime();

		if (taskStartTime == null && taskEndTime == null) {
			EventDateTime eventStartDateTime = convertToEventDateTime(taskStartDate);
			event.setStart(eventStartDateTime);
			EventDateTime eventEndDateTime = convertToEventDateTime(taskEndDate);
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

	private String generateTimeBasedUUID() {
		UUID uuid = Generators.timeBasedGenerator().generate();
		String uuidString = uuid.toString().replace("-", "");
		return uuidString;
	}

}
