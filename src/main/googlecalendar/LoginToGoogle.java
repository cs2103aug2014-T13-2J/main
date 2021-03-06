package main.googlecalendar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;

//@author A0072744A
/**
 * This class handles the logging in to Google Calendar and Google Tasks.
 */
public class LoginToGoogle {

	private static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static String CLIENT_ID = "101595183122-6l80mvk11dn0o476g77773fi73mev60c.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "0qES2e4j6ob4WlgekNRCESzR";
	private static String NAME_APPLICATION = "Tasker/0.5";
	private static String GOOGLE_CALENDAR_TYPE = "primary";

	private static String MESSAGE_LOGIN_SUCCESS = "You have successfully logged in to Google.";
	private static String MESSAGE_LOGIN_FAIL = "Sorry, an error has occurred while logging in to Google. Please restart Tasker.";
	private static String MESSAGE_OFFLINE_FIRST_TIME = "You are currently offline. Please provide your Google account ID to continue: ";
	private static String MESSAGE_OFFLINE_CONFIRM_ID = "Are you sure %s is the correct ID? [Y/N]\n";
	private static String MESSAGE_CALENDAR_ID_SET = "Google ID registered. You may now proceed.";
	private static String MESSAGE_INVALID_ARGUMENT = "Sorry, the argument must either be 'Y' or 'N'.";
	private static String MESSAGE_INVALID_EMAIL = "Sorry, the argument must be a valid email address.";

	private static final File CREDENTIAL_STORE_DIR = new File(
			System.getProperty("user.dir"), ".store/Tasker"); // Directory to
																// store user
																// credentials.
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	private static LoginToGoogle theOne = null;
	private static Calendar googleCalendarClient;
	private static Tasks googleTasksClient;
	private static String googleId = null;
	private static Scanner scanner;

	/**
	 * This constructor calls the login() method.
	 */
	private LoginToGoogle() {
		scanner = new Scanner(System.in);
		System.out.println(login());
	}

	/**
	 * This method creates the singleton for LoginToGoogle class and ensures
	 * that the LoginToGoogle constructor is not called more than once.
	 * 
	 * @return
	 */
	public static LoginToGoogle getInstance() {
		if (theOne == null) {
			theOne = new LoginToGoogle();
		}
		return theOne;
	}

	public Calendar getGoogleCalendarClient() {
		return googleCalendarClient;
	}

	public Tasks getGoogleTasksClient() {
		return googleTasksClient;
	}

	public String getGoogleId() {
		return googleId;
	}

	/**
	 * This method initiates the logging in to Google Calendar and Google Tasks.
	 * 
	 * @return the relevant login result for user
	 */
	private String login() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(CREDENTIAL_STORE_DIR);
			Credential credential = authorise();
			googleCalendarClient = new com.google.api.services.calendar.Calendar.Builder(
					httpTransport, JSON_FACTORY, credential)
					.setApplicationName(NAME_APPLICATION).build();
			googleTasksClient = new Tasks.Builder(httpTransport, JSON_FACTORY,
					credential).setApplicationName(NAME_APPLICATION).build();
		} catch (Exception e) {
			System.out.println(MESSAGE_LOGIN_FAIL);
			System.exit(0);
		}

		try {
			googleId = googleCalendarClient.calendars()
					.get(GOOGLE_CALENDAR_TYPE).execute().getId();
		} catch (IOException e) {
			if (googleId == null) {
				return getGoogleIdFromUser();
			}
		}
		return MESSAGE_LOGIN_SUCCESS;
	}

	/**
	 * This method authorises Tasker to access the user's protected data in
	 * Google Calendar and Google Tasks.
	 * 
	 * @return the credentials
	 * @throws Exception
	 */
	private static Credential authorise() throws Exception {
		// load client secrets
		GoogleClientSecrets.Details installedDetails = new GoogleClientSecrets.Details();
		installedDetails.setClientId(CLIENT_ID);
		installedDetails.setClientSecret(CLIENT_SECRET);
		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
		clientSecrets.setInstalled(installedDetails);

		// set up authorisation code flow
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

	/**
	 * This method asks and retrieves the Google ID from the user.
	 * 
	 * @return the relevant success message of retrieving the Google ID
	 */
	private String getGoogleIdFromUser() {
		String message = "";
		System.out.println(MESSAGE_OFFLINE_FIRST_TIME);
		googleId = scanner.nextLine();
		if (!isValidEmail(googleId)) {
			System.out.println(MESSAGE_INVALID_EMAIL);
			return getGoogleIdFromUser();
		} else {
			if (confirmGoogleIdFromUser()) {
				message = MESSAGE_CALENDAR_ID_SET;
			} else {
				message = getGoogleIdFromUser();
			}
		}
		return message;
	}

	/**
	 * This method confirms with the user that the Google ID provided is
	 * correct.
	 * 
	 * @return true if user enters Y
	 */
	private boolean confirmGoogleIdFromUser() {
		System.out.printf(MESSAGE_OFFLINE_CONFIRM_ID, googleId);
		String userReply = scanner.nextLine();
		if (userReply.equalsIgnoreCase("y")) {
			return true;
		} else if (userReply.equalsIgnoreCase("n")) {
			return false;
		} else {
			System.out.println(MESSAGE_INVALID_ARGUMENT);
			return confirmGoogleIdFromUser();
		}
	}

	/**
	 * This method determines whether the Google ID provided by the user is of
	 * the correct email format.
	 * 
	 * @param calendarId
	 * @return true if the Google ID is of the correct email format
	 */
	private boolean isValidEmail(String calendarId) {
		boolean isValid = calendarId.matches(EMAIL_REGEX);
		return isValid;
	}

}
