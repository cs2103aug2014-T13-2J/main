package main.googlecalendar;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

public class GoogleCalendar {
	
	private static String NAME_APPLICATION = "Tasker";
	private static String MESSAGE_LOGIN_SUCCESS = "You have successfully logged in to Google Calendar!";
	private static String MESSAGE_SYNC_SUCCESS = "Task successfully synchronised!";
	
	private static Calendar service;

	@SuppressWarnings("resource")
	public static String logInToGoogleCalendar() throws IOException, URISyntaxException {
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
		System.out.println("Logging in to Google Calendar...\n");
		System.out
				.println("Tasker will now open your default browser and direct you to a page for an authorisation code.\n");
		System.out
				.println("Copy the authorisation code and paste it back here once you are done!");
		System.out.println("Press ENTER to continue...");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(url));
		}
		
		System.out.print("Authorisation code: ");
		String code = scanner.nextLine();
		
		GoogleTokenResponse response = flow.newTokenRequest(code)
				.setRedirectUri(redirectUrl).execute();
		GoogleCredential googleCredential = new GoogleCredential()
				.setFromTokenResponse(response);
		// Create a new authorized API client
		service = new Calendar.Builder(httpTransport, jsonFactory,
				googleCredential).setApplicationName(NAME_APPLICATION).build();

		return MESSAGE_LOGIN_SUCCESS;
	}
	
	public static String syncTask(Event event) throws IOException {
		service.events().insert("primary", event);
		return MESSAGE_SYNC_SUCCESS;
	}
}
