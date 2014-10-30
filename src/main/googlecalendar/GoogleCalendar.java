package main.googlecalendar;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import main.storage.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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

	private static String MESSAGE_ALREADY_LOGGED_IN = "Sorry, you are already logged in to Google Calendar.";
	private static String MESSAGE_ASK_TO_LOGIN = "Log in to Google Calendar? [Y/N]";
	private static String MESSAGE_NO_LOGIN = "Cancelled logging in to Google Calendar.";
	private static String MESSAGE_LOGIN_SUCCESS = "You have successfully logged in to Google Calendar.";
	private static String MESSAGE_INVALID_ARGUMENT = "Sorry, the argument must either be 'Y' or 'N'.";
	private static String MESSAGE_SYNC_FAILURE = "Task failed to sync.";

	private static String NAME_APPLICATION = "Tasker/0.4";
	/** Directory to store user credentials. */
	private static final File CREDENTIAL_STORE_DIR = new File(
			System.getProperty("user.dir"), ".store/Tasker");
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	private static Calendar client;

	private static GoogleCalendar theOne = null;
	private static boolean isLoggedIn;
	private static String calendarId = null;
	private static Scanner scanner = new Scanner(System.in);

	private GoogleCalendar() {

	}

	public static GoogleCalendar getInstance() {
		if (theOne == null) {
			theOne = new GoogleCalendar();
		}
		return theOne;
	}

	public String initialiseGoogleCalendar(boolean isFirstTime) {
		String message = "";
		if (!isLoggedIn) {
			try {
				if (isFirstTime) {
					if (isLoggingInToGoogleCalendar()) {
						message = logInToGoogleCalendar();
					}
				} else {
					message = logInToGoogleCalendar();
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				message = initialiseGoogleCalendar(isFirstTime);
			}
		} else {
			message = MESSAGE_ALREADY_LOGGED_IN;
		}
		setUserCalendarId();
		return message;
	}

	private void setUserCalendarId() {
		try {
			com.google.api.services.calendar.model.Calendar calendar = client
					.calendars().get("primary").execute();
			calendarId = calendar.getId();
		} catch (IOException e) {
			// do nothing
		} catch (NullPointerException e) {
			// do nothing, for the case that we aren't able to instantiate the
			// calendar instance
		}
	}

	private String getUserCalendarId() {
		if (calendarId == null) {
			setUserCalendarId();
			return calendarId;
		} else {
			return calendarId;
		}
	}

	private boolean isLoggingInToGoogleCalendar() {
		System.out.println(MESSAGE_ASK_TO_LOGIN);
		String userReplyToLogIn = scanner.nextLine();
		if (userReplyToLogIn.equalsIgnoreCase("y")) {
			return true;
		} else if (userReplyToLogIn.equalsIgnoreCase("n")) {
			System.out.println(MESSAGE_NO_LOGIN);
			isLoggedIn = false;
			return false;
		} else {
			throw new IllegalArgumentException(MESSAGE_INVALID_ARGUMENT);
		}
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
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(CalendarScopes.CALENDAR))
				.setDataStoreFactory(dataStoreFactory).build();
		
		// authorise
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	private String logInToGoogleCalendar() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(CREDENTIAL_STORE_DIR);
			Credential credential = authorise();
			client = new com.google.api.services.calendar.Calendar.Builder(
					httpTransport, JSON_FACTORY, credential)
					.setApplicationName(NAME_APPLICATION).build();
			isLoggedIn = true;
		} catch (GeneralSecurityException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
		return MESSAGE_LOGIN_SUCCESS;
	}

	public String syncAddTask(Task task) {
		String id;
		if (isLoggedIn) {
			if (isNonFloatingTask(task)) {
				try {
					id = syncAddNonFloatingTask(task);
				} catch (IOException e) {
					id = MESSAGE_SYNC_FAILURE;
				}
			} else {
				id = "Floating task"; // To be added: id =
										// syncAddFloatingTask(task);
			}
		} else {
			id = "Offline";
		}
		return id;
	}

	private String syncAddNonFloatingTask(Task task) throws IOException {
		Event event = convertNonFloatingTaskToEvent(task);
		Event createdEvent = client.events().insert(getUserCalendarId(), event)
				.execute();
		String eventId = createdEvent.getId();
		return eventId;
	}

	private boolean isNonFloatingTask(Task task) {
		if (task.getHasStartDate()) {
			return true;
		} else {
			return false;
		}
	}

	public void syncDeleteTask(String eventId) {
		if (isLoggedIn) {
			try {
				client.events().delete(getUserCalendarId(), eventId).execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	public void syncUpdateTaskDescription(String eventId, String newDescription) {
		if (isLoggedIn) {
			Event event;
			try {
				event = client.events().get(getUserCalendarId(), eventId)
						.execute();
				event.setSummary(newDescription);
				client.events().update(getUserCalendarId(), eventId, event)
						.execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	public void syncUpdateTaskVenue(String eventId, String newVenue) {
		if (isLoggedIn) {
			try {
				Event event = client.events().get(getUserCalendarId(), eventId)
						.execute();
				event.setLocation(newVenue);
				client.events().update(getUserCalendarId(), eventId, event)
						.execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	public void syncUpdateTaskStartDate(String eventId, LocalDate newStartDate) {
		if (isLoggedIn) {
			try {
				Event event = client.events().get(getUserCalendarId(), eventId)
						.execute();
				EventDateTime eventDateTime = event.getStart();
				LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
				LocalTime localTime = localDateTime.toLocalTime();
				eventDateTime = convertToEventDateTime(newStartDate, localTime);
				event.setStart(eventDateTime);
				client.events().update(getUserCalendarId(), eventId, event)
						.execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	public void syncUpdateTaskStartTime(String eventId, LocalTime newStartTime) {
		if (isLoggedIn) {
			try {
				Event event = client.events().get(getUserCalendarId(), eventId)
						.execute();
				EventDateTime eventDateTime = event.getStart();
				LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
				LocalDate localDate = localDateTime.toLocalDate();
				eventDateTime = convertToEventDateTime(localDate, newStartTime);
				event.setStart(eventDateTime);
				client.events().update(getUserCalendarId(), eventId, event)
						.execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	public void syncUpdateTaskEndDate(String eventId, LocalDate newEndDate) {
		if (isLoggedIn) {
			try {
				Event event = client.events().get(getUserCalendarId(), eventId)
						.execute();
				EventDateTime eventDateTime = event.getEnd();
				LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
				LocalTime localTime = localDateTime.toLocalTime();
				eventDateTime = convertToEventDateTime(newEndDate, localTime);
				event.setEnd(eventDateTime);
				client.events().update(getUserCalendarId(), eventId, event)
						.execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	public void syncUpdateTaskEndTime(String eventId, LocalTime newEndTime) {
		if (isLoggedIn) {
			try {
				Event event = client.events().get(getUserCalendarId(), eventId)
						.execute();
				EventDateTime eventDateTime = event.getEnd();
				LocalDateTime localDateTime = convertToLocalDateTime(eventDateTime);
				LocalDate localDate = localDateTime.toLocalDate();
				eventDateTime = convertToEventDateTime(localDate, newEndTime);
				event.setEnd(eventDateTime);
				client.events().update(getUserCalendarId(), eventId, event)
						.execute();
			} catch (IOException e) {
				System.out.println(MESSAGE_SYNC_FAILURE);
			}
		}
	}

	private Event convertNonFloatingTaskToEvent(Task task) {
		Event event = new Event();
		event.setSummary(task.getDescription());
		event.setLocation(task.getVenue());

		LocalDate taskStartDate = task.getStartDate();
		LocalTime taskStartTime = task.getStartTime();
		EventDateTime eventStartDateTime = convertToEventDateTime(
				taskStartDate, taskStartTime);
		event.setStart(eventStartDateTime);

		LocalDate taskEndDate = task.getEndDate();
		LocalTime taskEndTime = task.getEndTime();
		EventDateTime eventEndDateTime = convertToEventDateTime(taskEndDate,
				taskEndTime);
		event.setEnd(eventEndDateTime);

		return event;
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
}
