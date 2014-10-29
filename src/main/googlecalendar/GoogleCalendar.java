package main.googlecalendar;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import main.storage.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

public class GoogleCalendar {

	private static String MESSAGE_ALREADY_LOGGED_IN = "Sorry, you are already logged in to Google Calendar.";
	private static String MESSAGE_ASK_TO_LOGIN = "Log in to Google Calendar? [Y/N]";
	private static String MESSAGE_NO_LOGIN = "Cancelled logging in to Google Calendar.";
	private static String MESSAGE_PRE_LOGIN = "Tasker will now open your default browser and direct you to a page for an authorisation code.\n\n"
			+ "Please copy the authorisation code and paste it back here.\n\n"
			+ "Press ENTER to continue...";
	private static String MESSAGE_LOGIN_SUCCESS = "You have successfully logged in to Google Calendar!";
	private static String MESSAGE_INVALID_ARGUMENT = "Sorry, the argument must either be 'Y' or 'N'.";
	private static String NAME_APPLICATION = "Tasker";

	private static GoogleCalendar theOne = null;
	private static boolean isLoggedIn;
	private static Calendar service;
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
		return message;
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

	private String logInToGoogleCalendar() {
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		// The clientId and clientSecret can be found in Google Developers
		// Console
		String clientId = "101595183122-6l80mvk11dn0o476g77773fi73mev60c.apps.googleusercontent.com";
		String clientSecret = "0qES2e4j6ob4WlgekNRCESzR";

		// Or your redirect URL for web based applications.
		String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";

		// Step 1: Authorize -->
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientId, clientSecret,
				Arrays.asList(CalendarScopes.CALENDAR)).setAccessType("online")
				.setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(redirectUrl)
				.build();
		System.out.println(MESSAGE_PRE_LOGIN);
		scanner.nextLine();

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (IOException e) {
				return e.getMessage();
			} catch (URISyntaxException e) {
				return e.getMessage();
			}
		}

		System.out.print("Authorisation code: ");
		String code = scanner.nextLine();

		GoogleTokenResponse response;
		try {
			response = flow.newTokenRequest(code).setRedirectUri(redirectUrl)
					.execute();
		} catch (IOException e) {
			return e.getMessage();
		}
		GoogleCredential googleCredential = new GoogleCredential()
				.setFromTokenResponse(response);
		// Create a new authorized API client
		service = new Calendar.Builder(httpTransport, jsonFactory,
				googleCredential).setApplicationName(NAME_APPLICATION).build();

		isLoggedIn = true;

		return MESSAGE_LOGIN_SUCCESS;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public String syncAddNonFloatingTask(Event event) throws IOException {
		Event createdEvent = service.events().insert("primary", event)
				.execute();
		String eventId = createdEvent.getId();
		return eventId;
	}

	public void syncDeleteTask(String eventId) throws IOException {
		service.events().delete("primary", eventId).execute();
	}
	
	public void syncUpdateTaskDescription(String eventId, String newDescription) throws IOException {
		Event event = service.events().get("primary", eventId).execute();
		event.setSummary(newDescription);
		service.events().update("primary", eventId, event).execute();
	}
	
	public void syncUpdateTaskVenue(String eventId, String newVenue) throws IOException {
		Event event = service.events().get("primary", eventId).execute();
		event.setLocation(newVenue);
		service.events().update("primary", eventId, event).execute();
	}

	public Event convertNonFloatingTaskToEvent(Task task) {
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
		org.joda.time.DateTime jodaStartDateTime = taskDate
				.toDateTime(taskTime);
		Date startDate = jodaStartDateTime.toDate();
		DateTime startDateTime = new DateTime(startDate,
				TimeZone.getTimeZone("UTC"));
		EventDateTime eventStartDateTime = new EventDateTime();
		eventStartDateTime.setDateTime(startDateTime);
		return eventStartDateTime;
	}
}
