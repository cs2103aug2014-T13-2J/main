package main.logic;

import java.util.Arrays;
import java.util.LinkedList;

import org.joda.time.DateTime;

public class AddParser extends CommandParser {

	final static String INVALID_DESCRIPTION_MESSAGE = "Sorry, we did not capture your description. Please try again.";
	final static String INVALID_TIME_FORMAT_MESSAGE = "Sorry we did not manage to capture the time. Please ensure you entered it in the correct format.";
	final static String INVALID_VENUE_MESSAGE = "Sorry we did not manage to capture the venue. Please try again.";
	final static String INVALID_DATE_FORMAT_MESSAGE = "Sorry we did not manage to capture the date. Please ensure you entered it in the correct format.";
	final static String DEFAULT_END_TIME = "2359";
	final static String PARSE_SUCCESS_MESSAGE = "Parse successful.";
	
	private String userInput;

	public AddParser(String arguments) {
		super(arguments);
		userInput = arguments;
		this.parse();
	}

	public String getUserInput() {
		return this.userInput;
	}

	public String parse() {
		String description = null, venue = null, startDate = null, startTime = null, endDate = null, endTime = null;

		String[] userInput = this.getUserInput().split(" ");
		LinkedList<String> wordsList = new LinkedList<String>(
				Arrays.asList(userInput));

		description = getDescriptionAndTrimUserInput(wordsList);

		while (!wordsList.isEmpty()) {
			String currentReservedWord = wordsList.poll();
			// insert an assert that currentReservedWord is indeed a reserved
			// word
			if (currentReservedWord.equals("at")) {
				String nextWord = wordsList.peek();
				// determine if next word is time or venue
				if (representsTime(nextWord)) {
					// if we have already parsed through start date or start
					// time
					if (startTime == null) {
						startTime = getTime(wordsList);
					} else {
						// find endTime
						endTime = getTime(wordsList);
					}
				} else {
					// if it's not time, then the next word must represent venue
					venue = getVenueAndTrimUserInput(wordsList);
				}
			} else if (currentReservedWord.equals("on")) {
				// get date
				startDate = getDateAndTrimUserInput(wordsList);
				endDate = startDate;
			} else if (currentReservedWord.equals("from")) {
				startDate = getDateAndTrimUserInput(wordsList);
				startTime = getTime(wordsList);
				//remove the word "to"
				wordsList.poll();
				endDate = getDateAndTrimUserInput(wordsList);
				endTime = getTime(wordsList);
			} else if (currentReservedWord.equals("next")) {
				startDate = getDateAndTrimUserInput(wordsList);
				startTime = getTime(wordsList);
				endDate = startDate;
				endTime = DEFAULT_END_TIME;
			}
		}
		
		this.setDescription(description);
		this.setVenue(venue);

		return PARSE_SUCCESS_MESSAGE;
	}

	// This function assumes that any of the reserved words will not be part of
	// the description
	public static String getDescriptionAndTrimUserInput(
			LinkedList<String> wordsList) throws IllegalArgumentException {
		String currentWord, description = "";
		while (!wordsList.isEmpty()) {
			currentWord = wordsList.poll();
			if (!isReservedWord(currentWord)) {
				description += currentWord;
			}
		}
		if (description.equals("")) {
			throw new IllegalArgumentException(INVALID_DESCRIPTION_MESSAGE);
		}
		description = description.trim();
		return description;
	}

	public static boolean isReservedWord(String word) {
		if (word.equals("at") || word.equals("on") || word.equals("from")
				|| word.equals("next")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean representsTime(String word) {
		if (word.matches("\\d")
				&& (word.substring(word.length() - 2, word.length()).equals(
						"am") || word.substring(word.length() - 2,
						word.length()).equals("pm"))) {
			return true;
		} else {
			return false;
		}
	}

	// This function assumes that word will be of the format HH.MM(am/pm) or
	// HH(am/pm)
	public static String getTime(LinkedList<String> wordList) {
		String word = wordList.poll();
		if(word.equals("at")) {
			word = wordList.poll();
		}
		boolean isPm;
		if (word.substring(word.length() - 2, word.length()).equals("pm")) {
			isPm = true;
		} else {
			isPm = false;
		}
		word = word.substring(0, word.length() - 2);
		Integer numTime = Integer.parseInt(word);
		if (isPm == true) {
			numTime += 12;
		}

		String stringTime = numTime.toString();
		return stringTime;

	}

	public static String getVenueAndTrimUserInput(LinkedList<String> wordsList) {
		String currentWord, venue = "";
		while (!wordsList.isEmpty()) {
			currentWord = wordsList.poll();
			if (!isReservedWord(currentWord)) {
				venue += currentWord;
			}
		}
		if (venue.equals("")) {
			throw new IllegalArgumentException(INVALID_VENUE_MESSAGE);
		}
		venue = venue.trim();
		return venue;
	}

	public static String getDateAndTrimUserInput(LinkedList<String> wordsList) {
		String date = "";
		String currentWord = wordsList.poll();
		String[] arr = currentWord.split("/");
		// if the format is DD/MM/YYYY or D/MM/YYYY
		if (arr.length == 3) {
			return currentWord;
		}
		// if the format is DD/MM or D/MM
		else if (arr.length == 2) {
			// get current year and append to D/MM
			DateTime current = new DateTime();
			Integer year = current.getYear();
			date = currentWord + "/" + year.toString();
			return date;
		}
		// if the format is D M or D M Y
		else {
			//get day
			if (isInteger(currentWord)) {
				date += currentWord;
			}
			else {
				throw new IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE);
			}
			//get month
			if (!wordsList.isEmpty()) {
				currentWord = wordsList.poll();
				if (isMonth(currentWord)) {
					date += "/" + convertMonthToNumberStringFormat(currentWord);
				}
			}
			else {
				throw new IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE);
			}
			//get year
			if (!wordsList.isEmpty()) {
				currentWord = wordsList.peek();
				if (isYear(currentWord)) {
					date += "/" + currentWord;
					wordsList.poll();
				}
			} else {
				//append the current year
				DateTime current = new DateTime();
				Integer year = current.getYear();
				date = currentWord + "/" + year.toString();
				return date;
				}
			return date;
		}
		// if the format is along the lines of Thursday or next Thursday (to be implemented)
	}
	private static boolean isMonth(String word) {
		word = word.toLowerCase();
		if (word.equals("january") || word.equals("february")
				|| word.equals("march") || word.equals("may")
				|| word.equals("june") || word.equals("july")
				|| word.equals("august") || word.equals("september")
				|| word.equals("october") || word.equals("november")
				|| word.equals("december")) {
			return true;
		} else {
			return false;
		}
	}

	private static String convertMonthToNumberStringFormat(String word) {
		word = word.toLowerCase();
		if (word.equals("january")) {
			return "1";
		} else if (word.equals("february")) {
			return "2";
		} else if (word.equals("march")) {
			return "3";
		} else if (word.equals("april")) {
			return "4";
		} else if (word.equals("may")) {
			return "5";
		} else if (word.equals("june")) {
			return "6";
		} else if (word.equals("july")) {
			return "7";
		} else if (word.equals("august")) {
			return "8";
		} else if (word.equals("september")) {
			return "9";
		} else if (word.equals("october")) {
			return "10";
		} else if (word.equals("november")) {
			return "11";
		} else {
			return "12";
		}
	}

	private static boolean isYear(String word) {
		if (word.length() == 4 && isInteger(word)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isInteger(String number) {
		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
